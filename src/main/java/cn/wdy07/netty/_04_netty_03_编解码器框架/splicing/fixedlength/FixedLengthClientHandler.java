package cn.wdy07.netty._04_netty_03_编解码器框架.splicing.fixedlength;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author wdy
 * @create 2020-06-15 12:22
 */
public class FixedLengthClientHandler extends SimpleChannelInboundHandler<ByteBuf> {//处理某一特定类型的数据



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("Client accept "+msg.toString(CharsetUtil.UTF_8));

    }

    @Override//重写指定方法
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client 开始写数据了!");
//        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello Netty", CharsetUtil.UTF_8));
        /*ByteBuf buffer = ctx.alloc().buffer();//分配池化的ByteBuf
        ByteBuf buffer1 = ctx.channel().alloc().buffer();//两种方式*/

        //ByteBufAllocator alloc = ctx.alloc();//ByteBufAllocator是一个接口，有两种实现，UnpooledByteBufAllocator/ PooledByteBufAllocator 默认是池化的
        //所以如果我们不需要池化的直接使用Unpooled去分配一个ByteBuf就行

        //测试粘包和半包
        ByteBuf msg = null;

        for(int i=0;i<100;i++){
            msg = Unpooled.buffer(FixedLengthClient.reqStr.length());
            msg.writeBytes(FixedLengthClient.reqStr.getBytes());
            ctx.writeAndFlush(msg);
//            Thread.sleep(50);
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
