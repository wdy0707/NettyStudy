package cn.wdy07.netty._03_nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author wdy
 * @create 2020-06-13 11:22
 */
public class NioClientHandler implements Runnable{

    private String IP;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;

    private volatile boolean started;

    public NioClientHandler(String IP, int port) {
        this.IP = IP;
        this.port = port;
        //创建选择器并打开通道
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            //将通道设置为非阻塞模式
            socketChannel.configureBlocking(false);
            started = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        this.started = false;
    }

    @Override
    public void run() {
        //首先进行连接操作（或者连接成功，或者在selector上注册OP_Connect）
        doConnect();
        //下面一直循环遍历selector
        while(started){
            try {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                //遍历获得的SelectionKey
                while(iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    //下面处理从其中获得的Key
                    //只处理connect和read事件(所以说也是事件驱动)，写事件不用，因为写事件的含义是：当写缓存有空闲的时候返回key,没啥用
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

        if(selector!=null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleEvent(SelectionKey selectionKey) throws Exception{
        if(selectionKey.isValid()){
            //获取与SselectionKey相关连的
            SocketChannel channel = (SocketChannel)selectionKey.channel();
            //如果是连接事件
            if(selectionKey.isConnectable()){
                if(channel.finishConnect()){
                    System.out.println("客户端连接成功！");
                }else{
                    System.out.println("客户端连接失败！");
                    System.exit(1);
                }
            }
            //如果是可读时间
            if(selectionKey.isReadable()){
                //处理对应的读取逻辑
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int num = channel.read(readBuffer);
                if(num > 0){
                    readBuffer.flip();//由写模式转换为读模式
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String msg = new String(bytes,"UTF-8");
                    System.out.println("客户端接收到消息："+msg);
                }else if(num <0){//如果读取的字节数为负，说明读取失败，该通信出错，直接关闭
                    selectionKey.cancel();
                    channel.close();
                }
            }

        }
    }

    private void doConnect(){
        try {
            //注意connect方法含义：如果立刻连接成功，则直接返回true，否则返回false
            if(socketChannel.connect(new InetSocketAddress(IP,port))){
                return;
            }else{//返回false之后(表示不能立刻连接)，则需要在Selector上进行注册
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doWrite(String msg){
        byte[] msgBytes = msg.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(msgBytes.length);
        writeBuffer.put(msgBytes);
        writeBuffer.flip();
        try {
            socketChannel.write(writeBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //单独写一个写消息的方法(对外暴露)
    public boolean sendMsg(String msg){
        //在写之前首先注册一个读监听,用于后面对应答消息的读取
        try {
            socketChannel.register(selector,SelectionKey.OP_READ);
            doWrite(msg);
            return true;

        } catch (ClosedChannelException e) {
            e.printStackTrace();
            return false;
        }
    }
}
