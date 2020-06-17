package cn.wdy07.netty._02_aio.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @author wdy
 * @create 2020-06-12 10:35
 */
public class AioClientWriteHandler implements CompletionHandler<Integer,ByteBuffer> {

    private AsynchronousSocketChannel socketChannel;
    private CountDownLatch countDownLatch;

    public AioClientWriteHandler(AsynchronousSocketChannel socketChannel, CountDownLatch countDownLatch) {
        this.socketChannel = socketChannel;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if(attachment.hasRemaining()){
            socketChannel.write(attachment,attachment,this);
        }
        //写完之后读数据
        ByteBuffer readbuffer = ByteBuffer.allocate(1024);
        //read中是把读到的结果readbuffer放在attachment中
        socketChannel.read(readbuffer,readbuffer,new AioClientReadHandler(socketChannel,countDownLatch));
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        countDownLatch.countDown();
    }
}
