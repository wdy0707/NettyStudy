package cn.wdy07.netty._04_netty_03_编解码器框架.splicing.delimiter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.net.InetSocketAddress;

/**
 * @author wdy
 * @create 2020-06-15 12:54
 */
public class DelimitorBaseSever {

    private int port;

    public DelimitorBaseSever(int port) {
        this.port = port;
    }

    public void start(){

        DelimitorSeverHandler handler = new DelimitorSeverHandler();

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {//ChannelInitializer也是一个Handler，当完成对pipeLine的初始化之后会将自己从PileLine中移除掉
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            String ds = NetConfig.DELIMITER;
                            byte[] bytes = ds.getBytes();
                            ByteBuf delimiter = Unpooled.copiedBuffer(bytes);
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(
                                    1024,delimiter));
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
        DelimitorBaseSever echoSever = new DelimitorBaseSever(NetConfig.SERVER_PORT);
        System.out.println("服务器启动！");
        echoSever.start();
        System.out.println("服务器关闭！");
    }
}
