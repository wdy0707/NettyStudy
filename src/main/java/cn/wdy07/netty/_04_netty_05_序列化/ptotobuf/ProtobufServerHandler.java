package cn.wdy07.netty._04_netty_05_序列化.ptotobuf;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import javax.swing.event.ChangeListener;

/**
 * @author wdy
 * @create 2020-06-17 14:31
 */
public class ProtobufServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //经过前面两个Handler处理后的msg就是已经反序列化好的Person对象了
        PersonProto.Person person = (PersonProto.Person) msg;
        String info = "Name: "+person.getName()+";Email: "+person.getEmail()+";Id:  "+person.getId();
        System.out.println("Server accept Person instance : " +info);
        ctx.writeAndFlush(Unpooled.copiedBuffer(info.getBytes(CharsetUtil.UTF_8)));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
