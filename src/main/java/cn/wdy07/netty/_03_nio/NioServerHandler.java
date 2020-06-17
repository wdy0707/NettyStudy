package cn.wdy07.netty._03_nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @author wdy
 * @create 2020-06-13 11:22
 */
public class NioServerHandler implements Runnable{

    private int port;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    private volatile boolean started;

    public NioServerHandler(int port) {
        this.port = port;
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            //通道打开之后需要配置成非阻塞的并绑定某一个端口
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务器已启动，监听端口："+port);
            started = true;
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        while(started){
            try {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    //从集合中移除该key并对对应的key进行事件处理
                    iterator.remove();
                    try {
                        handleEvent(selectionKey);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(selector != null){
            //关闭selector之后，会自动释放里面管理的资源
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        this.started = false;
    }

    private void handleEvent(SelectionKey selectionKey) throws Exception{
        if(selectionKey.isValid()){
            //对不同类型的key进行判断
            if(selectionKey.isAcceptable()){
                serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
                //这里accept()方法是阻塞的，返回时意味三次握手已经完成
                SocketChannel socketChannel = serverSocketChannel.accept();
                System.out.println("已与客户端建立连接！"+socketChannel.getRemoteAddress());

                //将该socketchannel注册到selector上
                socketChannel.configureBlocking(false);
                socketChannel.register(selector,SelectionKey.OP_READ);
            }
            if(selectionKey.isReadable()){
                SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                System.out.println("==============读事件：开始进行数据读取！");
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readNum = socketChannel.read(readBuffer);
                if(readNum>0){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String msg = new String(bytes, "UTF-8");
                    System.out.println("-------服务端接收到消息："+msg);
                    //进行消息应答
                    doWrite(socketChannel,msg);
                }else if(readNum<0){//读取失败，该通信通道损坏
                    socketChannel.close();
                    selectionKey.cancel();
                }
            }
        }
    }

    private void doWrite(SocketChannel socketChannel,String msg){

        String processMsg = processMsg(msg);

        byte[] msgBytes = processMsg.getBytes();
        ByteBuffer writeBuffer = null;
        try {
            writeBuffer = ByteBuffer.allocate(msgBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeBuffer.put(msgBytes);
        writeBuffer.flip();
        try {
            socketChannel.write(writeBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String processMsg(String msg){
        return "Server Response Msg:"+msg+"; Accept Time: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

}
