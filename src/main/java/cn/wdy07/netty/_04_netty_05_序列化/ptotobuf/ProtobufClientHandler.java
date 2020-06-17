package cn.wdy07.netty._04_netty_05_序列化.ptotobuf;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author wdy
 * @create 2020-06-17 13:08
 */
public class ProtobufClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf = (ByteBuf) msg;
        String res = byteBuf.toString(CharsetUtil.UTF_8);
        System.out.println("Client accept:"+res);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("准备生成数据！========>");

        //生成实体类 （Protobuf的方式）
        PersonProto.Person.Builder builder = PersonProto.Person.newBuilder();
        builder.setName("王丹阳");
        builder.setId(7);
        builder.setEmail("1150828564@qq.com");

        System.out.println("发送数据=======》"+builder.getName());
        ctx.writeAndFlush(builder.build());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
