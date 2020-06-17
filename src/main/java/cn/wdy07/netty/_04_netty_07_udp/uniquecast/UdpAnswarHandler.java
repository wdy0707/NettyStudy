package cn.wdy07.netty._04_netty_07_udp.uniquecast;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wdy
 * @create 2020-06-17 23:29
 */
public class UdpAnswarHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        DatagramPacket packet = (DatagramPacket) msg;
        String question = packet.content().toString(CharsetUtil.UTF_8);
        if(UdpQuestionSide.QUESTION.equals(question)){
            ctx.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer(getTime().getBytes(CharsetUtil.UTF_8)),
                    packet.sender()
            ));
        }else{
            ctx.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("该问题无法回答！".getBytes(CharsetUtil.UTF_8)),
                    packet.sender()
            ));
        }
    }

    private String getTime(){
        return "Now Time is : "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
