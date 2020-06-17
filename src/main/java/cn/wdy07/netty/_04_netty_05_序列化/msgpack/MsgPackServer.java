package cn.wdy07.netty._04_netty_05_序列化.msgpack;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.net.InetSocketAddress;

/**
 * @author wdy
 * @create 2020-06-17 17:00
 */
public class MsgPackServer {

    private int port;

    public MsgPackServer(int port) {
        this.port = port;
    }

    public void start(){
        NioEventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        //首先对长度进行对应的解码，防止粘包半包
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(
                                65535,0,2,0,2));

                        //然后使用msgpack进行对应的反序列化，因为不是内置的，第三方引用的编解码框架都需要自定义
                        ch.pipeline().addLast(new MsgPackDecoder());

                        //然后执行业务Handler
                        ch.pipeline().addLast(new MsgPackServerHandler());
                    }
                });

        try {
            ChannelFuture future = serverBootstrap.bind().sync();
            System.out.println("Server started....");
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
        MsgPackServer server = new MsgPackServer(NetConfig.SERVER_PORT);
        server.start();
    }
}
