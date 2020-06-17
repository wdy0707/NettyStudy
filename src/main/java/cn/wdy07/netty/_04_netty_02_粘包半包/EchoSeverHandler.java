package cn.wdy07.netty._04_netty_02_粘包半包;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wdy
 * @create 2020-06-15 13:02
 */
@ChannelHandler.Sharable//标识为线程共享的Handler（需要保证类线程安全）
public class EchoSeverHandler extends ChannelInboundHandlerAdapter {
    private AtomicInteger count = new AtomicInteger();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf msgByteBuf = (ByteBuf) msg; //Netty实现的缓冲区
        String msgStr = msgByteBuf.toString(CharsetUtil.UTF_8);

        System.out.println("Sever accept :"+ msgStr +", the count is "+count.incrementAndGet());

        String resStr = "Hello," + msgStr + ",welcome!"+System.getProperty("line.separator");
        ctx.write(Unpooled.copiedBuffer(resStr.getBytes(CharsetUtil.UTF_8)));//直接将数据应答回去

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)//发完数据，服务器端关闭了连接，客户端就会自动关闭！
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
