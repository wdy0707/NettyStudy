package cn.wdy07.netty._04_netty_02_粘包半包;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author wdy
 * @create 2020-06-15 12:16
 */
public class EchoClient {
    private final String IP;
    private final int port;

    public EchoClient(String IP, int port) {
        this.IP = IP;
        this.port = port;
    }

    public void start(){
            EventLoopGroup group = new NioEventLoopGroup();//线程组
        try {
            Bootstrap bootstrap = new Bootstrap();//启动类
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(IP,port))
                    .handler(new EchoClientHandler()).option(ChannelOption.TCP_NODELAY,true);

            ChannelFuture future = bootstrap.connect().sync();//连接到远程节点，阻塞直到连接成功

            future.channel().closeFuture().sync();//一直阻塞到某一个方法关闭了通道  //阻塞，直到channel关闭
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
        EchoClient echoClient = new EchoClient(NetConfig.SERVER_IP, NetConfig.SERVER_PORT);
        echoClient.start();
    }

}
