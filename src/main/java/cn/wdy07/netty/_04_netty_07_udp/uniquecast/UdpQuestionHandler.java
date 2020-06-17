package cn.wdy07.netty._04_netty_07_udp.uniquecast;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

/**
 * @author wdy
 * @create 2020-06-17 23:05
 */
public class UdpQuestionHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //直接强转为DatagramPacket
        DatagramPacket packet = (DatagramPacket) msg;
        String request = packet.content().toString(CharsetUtil.UTF_8);
        System.out.println(request);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
