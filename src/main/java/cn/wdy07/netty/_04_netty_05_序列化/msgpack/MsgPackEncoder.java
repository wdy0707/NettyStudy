package cn.wdy07.netty._04_netty_05_序列化.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * @author wdy
 * @create 2020-06-17 16:27
 */

//因为msgpack不是netty内置的，需要第三方集成，所以没有对应的Handler，需要自己来实现
public class MsgPackEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        MessagePack messagePack = new MessagePack();
        byte[] bytes = messagePack.write(msg);
        System.out.println("序列化成功！写入开始~");
        out.writeBytes(bytes);
    }
}
