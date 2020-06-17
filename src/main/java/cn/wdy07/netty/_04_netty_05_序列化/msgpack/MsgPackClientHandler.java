package cn.wdy07.netty._04_netty_05_序列化.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author wdy
 * @create 2020-06-17 16:36
 */
public class MsgPackClientHandler extends ChannelInboundHandlerAdapter {

    private final int userNum;

    public MsgPackClientHandler(int userNum) {
        this.userNum = userNum;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf = (ByteBuf) msg;
        String msgInfo = byteBuf.toString(CharsetUtil.UTF_8);
        System.out.println("Client accept info :"+msgInfo);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        User[] users = makeUsers();
        for(User user:users){
            System.out.println(user);
            ctx.write(user);
        }
        ctx.flush();
    }

    private User[] makeUsers(){
        User[] users = new User[userNum];
        User user = null;
        for(int i=0;i<userNum;i++){
            user = new User("NO:"+i,"user"+i,20+i);
            user.setContact(new Contact("18548902733","1150828564@qq.com"));
            users[i]=user;
        }
        return users;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();;
        ctx.close();
    }
}
