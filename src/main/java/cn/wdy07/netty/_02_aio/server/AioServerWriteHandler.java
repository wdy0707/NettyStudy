package cn.wdy07.netty._02_aio.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @author wdy
 * @create 2020-06-12 17:23
 */
public class AioServerWriteHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel socketChannel;
    private CountDownLatch countDownLatch;

    public AioServerWriteHandler(AsynchronousSocketChannel socketChannel, CountDownLatch countDownLatch) {
        this.socketChannel = socketChannel;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void completed(Integer result, ByteBuffer writeBuffer) {
        if(writeBuffer.hasRemaining()){
            socketChannel.write(writeBuffer,writeBuffer,this);
        }
        System.out.println("服务端返回数据成功！");

    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        System.out.println("服务端写回数据失败！");
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        countDownLatch.countDown();
    }
}
