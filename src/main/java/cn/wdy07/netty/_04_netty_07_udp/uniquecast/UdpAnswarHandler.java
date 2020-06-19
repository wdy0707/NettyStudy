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

        //这个地方有点不能理解，前面TCP的都是在channelRead()方法中将对象强转成ByteBuf，
        // 因为接收到的所有字节都缓存在了ByteBuf对象中，别的对象不能直接强转，必须使用序列化工具（内置的ProtoBuf或者第三方messagePack等）
        //这里直接将[Object]msg强转成DatagramPacket，应该是NIODataFrameChannel的作用

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
