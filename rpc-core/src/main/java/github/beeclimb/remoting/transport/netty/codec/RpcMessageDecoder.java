package github.beeclimb.remoting.transport.netty.codec;

import github.beeclimb.compress.Compress;
import github.beeclimb.enums.CompressTypeEnum;
import github.beeclimb.enums.SerializationTypeEnum;
import github.beeclimb.extension.ExtensionLoader;
import github.beeclimb.remoting.constants.RpcConstants;
import github.beeclimb.remoting.dto.RpcMessage;
import github.beeclimb.remoting.dto.RpcRequest;
import github.beeclimb.remoting.dto.RpcResponse;
import github.beeclimb.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * <p>
 * custom protocol codec
 * </p>
 * <pre>
 * 0    1    2    3    4    5    6    7    8    9    10   11   12   13   14   15   16
 * +----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+
 * |(1)                |(2) |(3)                |(4) |(5) |(6) |(7)                |
 * +-------------------------------------------------------------------------------+
 * |                             body                                              |
 * |                           ... ...                                             |
 * +-------------------------------------------------------------------------------+
 * (1) magic code   魔法数    4B
 * (2) version      版本      1B
 * (3) full length  消息长度   4B
 * (4) messageType  消息类型   1B
 * (5) codec        序列化类型  1B
 * (6) compress     压缩类型   1B
 * (7) requestId    请求Id    4B
 * (8) body         消息主体   object类型数据
 * </pre>
 * <p>
 * {@link LengthFieldBasedFrameDecoder} is a length-based decoder, used to solve TCP unpacking and sticking problems.
 * </p>
 *
 * @author jun.ma
 * @date 2022/5/26 23:18:00
 * @see <a href="https://zhuanlan.zhihu.com/p/95621344">LengthFieldBasedFrameDecoder解码器</a>
 */
@Slf4j
public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {
    /**
     * @param maxFrameLength      Maximum frame length. It decides the maximum length of data that can be received.
     *                            If it exceeds, the data will be discarded.
     * @param lengthFieldOffset   Length field offset. The length field is the one that skips the specified length of byte.
     * @param lengthFieldLength   The number of bytes in the length field.
     * @param lengthAdjustment    The compensation value to add to the value of the length field.
     * @param initialBytesToStrip Number of bytes skipped.
     *                            if you need to received all the header+body data, this value is 0.
     *                            if you only want to receive the body data, then you need to skip the number of bytes consumed by header.
     */
    public RpcMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                             int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    public RpcMessageDecoder() {
        // lengthFieldOffset: magic code is 4B, and version is 1B, and then full length. so value is 5
        // lengthFieldLength: full length is 4B, so value is 4
        // lengthAdjustment: full length include all data and read 9 bytes before, so the left length is (fullLength-9). so value is -9
        // initialBytesToStrip: we will check magic code and version manually, so do not skip any bytes. so value is 0
        this(RpcConstants.MAX_FRAME_LENGTH, 5, 4, -9, 0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decoded;
            if (frame.readableBytes() >= RpcConstants.TOTAL_LENGTH) {
                try {
                    return decodeFrame(frame);
                } catch (Exception e) {
                    log.error("decode frame error!", e);
                    throw e;
                } finally {
                    frame.release();
                }
            }
        }
        return decoded;
    }

    private Object decodeFrame(ByteBuf in) {
        // note: must read ByteBuf in order
        checkMagicNumber(in);
        checkVersion(in);
        int fullLength = in.readInt();
        // build RpcMessage object
        byte messageType = in.readByte();
        byte codecType = in.readByte();
        byte compressType = in.readByte();
        int requestId = in.readInt();
        RpcMessage rpcMessage = RpcMessage.builder()
                .messageType(messageType)
                .codec(codecType)
                .compress(compressType)
                .requestId(requestId).build();
        if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
            rpcMessage.setData(RpcConstants.PING);
            return rpcMessage;
        }
        if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
            rpcMessage.setData(RpcConstants.PONG);
            return rpcMessage;
        }
        int bodyLength = fullLength - RpcConstants.HEAD_LENGTH;
        if (bodyLength > 0) {
            byte[] bs = new byte[bodyLength];
            in.readBytes(bs);
            // decompress the bytes
            String compressName = CompressTypeEnum.getName(compressType);
            log.info("compress name: [{}]", compressName);
            Compress compress = ExtensionLoader.getExtensionLoader(Compress.class)
                    .getExtension(compressName);
            bs = compress.decompress(bs);
            // deserialize the object
            String codecName = SerializationTypeEnum.getName(codecType);
            log.info("codec name: [{}]", codecName);
            Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class)
                    .getExtension(codecName);
            if (messageType == RpcConstants.REQUEST_TYPE) {
                RpcRequest tempValue = serializer.deserialize(bs, RpcRequest.class);
                rpcMessage.setData(tempValue);
            } else {
                RpcResponse tempValue = serializer.deserialize(bs, RpcResponse.class);
                rpcMessage.setData(tempValue);
            }
        }
        return rpcMessage;
    }

    private void checkVersion(ByteBuf in) {
        // read the version and compare
        byte version = in.readByte();
        if (version != RpcConstants.VERSION) {
            throw new RuntimeException("version isn't compatible " + version);
        }
    }

    private void checkMagicNumber(ByteBuf in) {
        // read the first 4 byte, which is the magic number, and compare
        int length = RpcConstants.MAGIC_NUMBER.length;
        byte[] temp = new byte[length];
        in.readBytes(temp);
        for (int i = 0; i < length; ++i) {
            if (temp[i] != RpcConstants.MAGIC_NUMBER[i]) {
                throw new IllegalArgumentException("unknown magic code: " + Arrays.toString(temp));
            }
        }
    }

}
















