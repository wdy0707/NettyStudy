package cn.wdy07.netty._04_netty_07_udp.uniquecast;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * @author wdy
 * @create 2020-06-17 23:01
 */
public class UdpQuestionSide {

    private int port;
    private String IP;

    public static final String QUESTION = "请告诉我现在几点了？";


    public UdpQuestionSide( String IP,int port) {
        this.port = port;
        this.IP = IP;
    }

    public void start(){
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .handler(new UdpQuestionHandler());


        //UDP只需要bind端口，不需要建立连接
        try {
            ChannelFuture future = bootstrap.bind(0).sync();//inetPort:表示任意分配一个端口即可
            System.out.println("QuestionSide started.....");

            //然后发送消息
            future.channel().writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer(QUESTION.getBytes(CharsetUtil.UTF_8)),
                    new InetSocketAddress(IP,port)
            ));

            //等待超时
            if(!future.channel().closeFuture().await(15000)){
                System.out.println("查询超时！");
            }

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

        UdpQuestionSide udpQuestionSide = new UdpQuestionSide(NetConfig.SERVER_IP, NetConfig.SERVER_PORT);

        udpQuestionSide.start();
    }
}
