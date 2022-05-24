package github.beeclimb.remoting.transport.netty;

import github.beeclimb.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author jun.ma
 * @date 2022/5/23 17:36:00
 */
@AllArgsConstructor
@Slf4j
public class KryoDecoder extends ByteToMessageDecoder {
    private final Serializer serializer;
    private final Class<?> genericClass;
    private static final int BODY_LENGTH = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() >= BODY_LENGTH) {
            in.markReaderIndex();
            int dataLength = in.readInt();
            if (dataLength < 0 || in.readableBytes() < 0) {
                log.error("data length or byteBuf readableBytes is not valid");
                return;
            }
            if (in.readableBytes() < dataLength) {
                in.resetReaderIndex();
                return;
            }
            byte[] body = new byte[dataLength];
            in.readBytes(body);
            Object object = serializer.deserialize(body, genericClass);
            out.add(object);
            log.info("successful decode ByteBuf to object");
        }
    }
}
