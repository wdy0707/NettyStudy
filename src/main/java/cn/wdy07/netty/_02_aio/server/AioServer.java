package cn.wdy07.netty._02_aio.server;

/**
 * @author wdy
 * @create 2020-06-12 17:34
 */
public class AioServer {

    private static AioServerHandler serverHandler;


    public static void main(String[] args) {
        serverHandler = new AioServerHandler();
        new Thread(serverHandler).start();

    }
}
