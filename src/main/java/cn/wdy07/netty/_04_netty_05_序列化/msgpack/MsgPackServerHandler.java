package cn.wdy07.netty._04_netty_05_序列化.msgpack;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author wdy
 * @create 2020-06-17 17:19
 */
public class MsgPackServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //因为前面MsgPackHandler已经处理过了，所以直接就是User对象，可以强转
        User user = (User) msg;
        System.out.println("Sever accept msg: "+user);
        String userInfo = user.toString()+System.getProperty("line.separator");//因为要发送10次，所以必须要加分隔符
        ctx.writeAndFlush(Unpooled.copiedBuffer(userInfo.getBytes(CharsetUtil.UTF_8)));
    }

    /*@Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }*/

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
