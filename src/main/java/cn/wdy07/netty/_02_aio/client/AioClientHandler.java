package cn.wdy07.netty._02_aio.client;

import cn.wdy07.netty._02_aio.NetConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @author wdy
 * @create 2020-06-12 10:03
 */
public class AioClientHandler implements CompletionHandler<Void,InetSocketAddress>,Runnable {

    private AsynchronousSocketChannel socketChannel;
    private String IP;
    private Integer port;

    private CountDownLatch countDownLatch;

    public AioClientHandler(){
        this.IP = NetConfig.SERVER_IP;
        this.port = NetConfig.SERVER_PORT;
        try {
            socketChannel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);

        socketChannel.connect(new InetSocketAddress(IP, port),new InetSocketAddress(IP, port),new AioClientHandler());

        try {
            countDownLatch.await();//防止异步程序直接终止
            socketChannel.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void completed(Void result, InetSocketAddress attachment) {

        System.out.println("客户端连接成功！服务端地址是："+attachment.getAddress()+":"+attachment.getPort());
    }

    @Override
    public void failed(Throwable exc, InetSocketAddress attachment) {

        System.out.println("连接服务端："+attachment.getAddress()+":"+attachment.getPort()+" 失败！");
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        countDownLatch.countDown();
    }

    public boolean sendMsg(String msg){
        byte[] msgBytes = msg.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(msgBytes.length);
        writeBuffer.put(msgBytes);
        writeBuffer.flip();
        socketChannel.write(writeBuffer,writeBuffer,new AioClientWriteHandler(socketChannel,countDownLatch));
        return true;
    }

}
