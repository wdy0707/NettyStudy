package cn.wdy07.netty._04_netty_05_序列化.ptotobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.net.InetSocketAddress;

/**
 * @author wdy
 * @create 2020-06-17 12:39
 */
public class ProtobufClient {

    private String IP;
    private int port;

    public ProtobufClient(String IP, int port) {
        this.IP = IP;
        this.port = port;
    }

    public void start(){
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(IP,port))
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        //首先在数据前面加上报文长度字段，防止粘包半包
                        ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                        //加上Protobuf的序列化Handler
                        ch.pipeline().addLast(new ProtobufEncoder());
                        //然后加上自定义的Handler
                        ch.pipeline().addLast(new ProtobufClientHandler());
                    }
                });

        try {
            ChannelFuture future = bootstrap.connect().sync();
            System.out.println("Client started.....");
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
        ProtobufClient client = new ProtobufClient(NetConfig.SERVER_IP, NetConfig.SERVER_PORT);
        client.start();
    }
}
