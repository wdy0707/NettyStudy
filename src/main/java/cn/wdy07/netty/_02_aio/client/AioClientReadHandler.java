package cn.wdy07.netty._02_aio.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @author wdy
 * @create 2020-06-12 10:48
 */
public class AioClientReadHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel socketChannel;
    private CountDownLatch countDownLatch;

    public AioClientReadHandler(AsynchronousSocketChannel socketChannel, CountDownLatch countDownLatch) {
        this.socketChannel = socketChannel;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void completed(Integer result, ByteBuffer readBuffer) {
        readBuffer.flip();//转成读模式
        byte[] bytes = new byte[readBuffer.remaining()];
        readBuffer.get(bytes);
        try {
            String msg = new String(bytes, "UTF-8");
            System.out.println("客户端接收到消息："+msg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
