package cn.wdy07.ws_netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

/**
 * @author wdy
 * @create 2020-06-19 16:14
 */
public class WSNServer {

    //用于保存所有连接的Channel
    private static ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    private int port;

    public WSNServer(int port) {
        this.port = port;
    }

    public void start(){
        NioEventLoopGroup mainGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup subGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(mainGroup,subGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WSNServerInitHandler(channelGroup));
    }
}
