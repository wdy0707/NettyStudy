package cn.wdy07.netty._04_netty_07_udp.uniquecast;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * @author wdy
 * @create 2020-06-17 23:26
 */
public class UdpAnswarSide {

    private int port;

    public UdpAnswarSide(int port) {
        this.port = port;
    }

    public void start(){
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .handler(new UdpAnswarHandler());

        try {
            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("应答服务器已启动~");
            future.channel().closeFuture().sync();
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
        UdpAnswarSide answarSide = new UdpAnswarSide(NetConfig.SERVER_PORT);
        answarSide.start();
    }
}
