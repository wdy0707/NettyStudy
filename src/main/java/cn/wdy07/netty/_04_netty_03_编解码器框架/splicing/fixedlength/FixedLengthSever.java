package cn.wdy07.netty._04_netty_03_编解码器框架.splicing.fixedlength;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.net.InetSocketAddress;

/**
 * @author wdy
 * @create 2020-06-15 12:54
 */
public class FixedLengthSever {

    public static String resStr = "Welcome to Netty!";

    private int port;

    public FixedLengthSever(int port) {
        this.port = port;
    }

    public void start(){

        FixedLengthSeverHandler handler = new FixedLengthSeverHandler();

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {//ChannelInitializer也是一个Handler，当完成对pipeLine的初始化之后会将自己从PileLine中移除掉
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new FixedLengthFrameDecoder(FixedLengthClient.reqStr.length()));
                            ch.pipeline().addLast(handler);
                        }
                    })
                    .childOption(ChannelOption.TCP_NODELAY,true);//禁用Negle算法

            ChannelFuture future = serverBootstrap.bind().sync();

            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        FixedLengthSever echoSever = new FixedLengthSever(NetConfig.SERVER_PORT);
        System.out.println("服务器启动！");
        echoSever.start();
        System.out.println("服务器关闭！");
    }
}
