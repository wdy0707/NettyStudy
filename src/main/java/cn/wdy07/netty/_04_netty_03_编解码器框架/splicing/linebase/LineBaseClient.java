package cn.wdy07.netty._04_netty_03_编解码器框架.splicing.linebase;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.net.InetSocketAddress;

/**
 * @author wdy
 * @create 2020-06-15 12:16
 */
public class LineBaseClient {
    private final String IP;
    private final int port;

    public LineBaseClient(String IP, int port) {
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
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            //注意，一定先加编解码器，再加自定义的Handler
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024))
                                    .addLast(new LineBaseClientHandler());
                        }
                    })
                    .option(ChannelOption.TCP_NODELAY,true);

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

    /*private static class ChannelInitializerImp extends ServerhandlerInit<Channel> {

        @Override
        protected void initChannel(Channel ch) throws Exception {
            //	加分割符：系统回车符
            ch.pipeline().addLast(new LineBasedFrameDecoder(1024*100000));
            ch.pipeline().addLast(new DelimitorClientHandler());
        }
    }*/

    public static void main(String[] args) {
        LineBaseClient echoClient = new LineBaseClient(NetConfig.SERVER_IP, NetConfig.SERVER_PORT);
        echoClient.start();
    }

}
