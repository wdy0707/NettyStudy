package cn.wdy07.netty._04_netty_05_序列化.msgpack;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.net.InetSocketAddress;

/**
 * @author wdy
 * @create 2020-06-17 15:40
 */
public class MsgPackClient {

    private String IP;
    private int port;

    public MsgPackClient(String IP, int port) {
        this.IP = IP;
        this.port = port;
    }

    public void start(){
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(IP,port))
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        //首先还是设置包头长度，避免粘包半包
                        ch.pipeline().addLast(new LengthFieldPrepender(2));
                        //设置用于序列化的MsgPackage（msgpack不是netty内置的，需要自己定义Handler）
                        ch.pipeline().addLast(new MsgPackEncoder());
                        //添加LineBaseDecoder用于处理应答
                        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                        //添加业务处理Handler
                        ch.pipeline().addLast(new MsgPackClientHandler(10));
                    }
                });

        try {
            ChannelFuture future = bootstrap.connect().sync();
            System.out.println("Client Connect....");
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
        MsgPackClient client = new MsgPackClient(NetConfig.SERVER_IP, NetConfig.SERVER_PORT);
        client.start();
    }
}
