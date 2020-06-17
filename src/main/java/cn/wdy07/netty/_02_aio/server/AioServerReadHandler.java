package cn.wdy07.netty._02_aio.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @author wdy
 * @create 2020-06-12 17:08
 */
public class AioServerReadHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel socketChannel;
    private CountDownLatch countDownLatch;

    public AioServerReadHandler(AsynchronousSocketChannel socketChannel, CountDownLatch countDownLatch) {
        this.socketChannel = socketChannel;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void completed(Integer result, ByteBuffer byteBuffer) {
        byteBuffer.flip();//转换成读模式
        byte[] bytes = new byte[byteBuffer.remaining()];
        String msg = null;
        try {
            msg = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("服务端接收到消息："+msg);
        String resMsg = processMsg(msg);
        byte[] resMsgBytes = resMsg.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(resMsgBytes.length);
        writeBuffer.put(resMsgBytes);
        writeBuffer.flip();
        socketChannel.write(writeBuffer,writeBuffer,new AioServerWriteHandler(socketChannel,countDownLatch));

    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        System.out.println("读数据失败！");
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        countDownLatch.countDown();
    }

    private String processMsg(String msg){
        return "Server Response Msg:"+msg+"; Accept Time: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
