package cn.wdy07.netty._04_netty_04_httpserver.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author wdy
 * @create 2020-06-16 19:31
 */
public class HTTPServer {
    private int port;

    public HTTPServer(int port) {
        this.port = port;
    }

    public void start() {
        EventLoopGroup group = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY,true)
                .childHandler(new ServerhandlerInit());

        try {
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        HTTPServer httpServer = new HTTPServer(NetConfig.SERVER_PORT);
        System.out.println("服务器已启动~");
        httpServer.start();

    }
}
