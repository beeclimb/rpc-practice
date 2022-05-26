package github.beeclimb.remoting.transport.netty.codec;

import github.beeclimb.compress.Compress;
import github.beeclimb.enums.CompressTypeEnum;
import github.beeclimb.enums.SerializationTypeEnum;
import github.beeclimb.extension.ExtensionLoader;
import github.beeclimb.remoting.constants.RpcConstants;
import github.beeclimb.remoting.dto.RpcMessage;
import github.beeclimb.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

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
 * </pre>
 *
 * @author jun.ma
 * @date 2022/5/24 21:27:00
 */
@Slf4j
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage rpcMessage, ByteBuf out) {
        try {
            out.writeBytes(RpcConstants.MAGIC_NUMBER);
            out.writeByte(RpcConstants.VERSION);
            // leave a place to write the value of full length
            out.writerIndex(out.writerIndex() + 4);
            byte messageType = rpcMessage.getMessageType();
            out.writeByte(messageType);
            out.writeByte(rpcMessage.getCodec());
            out.writeByte(CompressTypeEnum.GZIP.getCode());
            out.writeInt(ATOMIC_INTEGER.getAndIncrement());
            // build full length
            byte[] bodyBytes = null;
            int fullLength = RpcConstants.HEAD_LENGTH;
            // if messageType is not heartbeat message, fullLength = head length + body length
            if (messageType != RpcConstants.HEARTBEAT_REQUEST_TYPE
                    && messageType != RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
                // serialize thr object
                String codecName = SerializationTypeEnum.getName(rpcMessage.getCodec());
                log.info("codec name: [{}] ", codecName);
                Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class)
                        .getExtension(codecName);
                bodyBytes = serializer.serialize(rpcMessage.getData());

                // compress the bytes
                String compressName = CompressTypeEnum.getName(rpcMessage.getCompress());
                Compress compress = ExtensionLoader.getExtensionLoader(Compress.class)
                        .getExtension(compressName);
                bodyBytes = compress.compress(bodyBytes);
                fullLength += bodyBytes.length;
            }
            if (bodyBytes != null) {
                out.writeBytes(bodyBytes);
            }
            int writeIndex = out.writerIndex();
            out.writerIndex(writeIndex - fullLength + RpcConstants.MAGIC_NUMBER.length + 1);
            out.writeInt(fullLength);
            out.writerIndex(writeIndex);
        } catch (Exception e) {
            log.error("encode request error!", e);
        }
    }
}
