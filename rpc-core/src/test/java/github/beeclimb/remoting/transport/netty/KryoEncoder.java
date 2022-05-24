package github.beeclimb.remoting.transport.netty;

import github.beeclimb.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

/**
 * @author jun.ma
 * @date 2022/5/23 17:13:00
 */
@AllArgsConstructor
public class KryoEncoder extends MessageToByteEncoder<Object> {
    private final Serializer serializer;
    private final Class<?> genericClass;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) {
        if (genericClass.isInstance(o)) {
            byte[] body = serializer.serialize(o);
            int dataLength = body.length;
            byteBuf.writeInt(dataLength);
            byteBuf.writeBytes(body);
        }
    }
}
