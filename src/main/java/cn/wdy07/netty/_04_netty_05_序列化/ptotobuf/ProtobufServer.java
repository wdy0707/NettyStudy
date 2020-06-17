package cn.wdy07.netty._04_netty_05_序列化.ptotobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

import java.net.InetSocketAddress;

/**
 * @author wdy
 * @create 2020-06-17 14:21
 */
public class ProtobufServer {

    private int port;

    public ProtobufServer(int port) {
        this.port = port;
    }

    public void start(){
        NioEventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .option(ChannelOption.TCP_NODELAY,true)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        //首先加上数据长度分割解码
                        ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                        //然后将字节反序列化为对象(注意参数)
                        ch.pipeline().addLast(new ProtobufDecoder(
                                PersonProto.Person.getDefaultInstance()
                        ));
                        //然后执行自定义Handler
                        ch.pipeline().addLast(new ProtobufServerHandler());
                    }
                });

        try {
            ChannelFuture future = bootstrap.bind().sync();
            System.out.println("Server started......");
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
        ProtobufServer server = new ProtobufServer(NetConfig.SERVER_PORT);
        server.start();

    }
}
