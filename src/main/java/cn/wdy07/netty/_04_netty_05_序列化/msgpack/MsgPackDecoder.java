package cn.wdy07.netty._04_netty_05_序列化.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * @author wdy
 * @create 2020-06-17 17:09
 */
//因为前面已经经过一个LengthFieldDecoderHandler了，所以用MsgToMsgDecode
public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        int lenght = msg.readableBytes();
        byte[] bytes = new byte[lenght];
        msg.readBytes(bytes);
        MessagePack messagePack = new MessagePack();
        User user = messagePack.read(bytes, User.class);
        out.add(user);
        System.out.println("MsgPack has decoded user :"+user);
    }
}
