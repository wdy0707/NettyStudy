package cn.wdy07.netty._03_nio;

/**
 * @author wdy
 * @create 2020-06-13 11:22
 */
public class NioSever {

    private static NioServerHandler serverHandler;

    public static void main(String[] args) {
        //首先进行判断
        if(serverHandler != null){
            serverHandler.stop();//停止serverHandler，关闭其中的selector，包括释放了里面管理的连接
        }

        serverHandler = new NioServerHandler(NetConfig.SERVER_PORT);
        new Thread(serverHandler).start();
    }
}
