package cn.wdy07.netty._02_aio.server;

import cn.wdy07.netty._02_aio.NetConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @author wdy
 * @create 2020-06-12 16:44
 */
public class AioServerHandler implements CompletionHandler<AsynchronousSocketChannel, Void>,Runnable {

    private AsynchronousServerSocketChannel serverSocketChannel;
    private Integer port;

    private CountDownLatch countDownLatch;

    private Integer connectNum;

    public AioServerHandler(){
        this.port = NetConfig.SERVER_PORT;
        countDownLatch = new CountDownLatch(1);
        connectNum = 0;
        try {
            serverSocketChannel = AsynchronousServerSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.accept(null,new AioServerHandler());
            countDownLatch.await();
            serverSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }


    @Override
    public void completed(AsynchronousSocketChannel socketChannel, Void attachment) {
        System.out.println("服务端连接成功，当前连接数："+ ++connectNum);
        try {
            serverSocketChannel.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverSocketChannel.accept(null,this);
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        socketChannel.read(readBuffer,readBuffer,new AioServerReadHandler(socketChannel,countDownLatch));
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        System.out.println("服务端连接失败！");
        try {
            serverSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        countDownLatch.countDown();

    }


}
