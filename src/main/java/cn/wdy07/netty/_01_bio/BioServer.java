package cn.wdy07.netty._01_bio;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wdy
 * @create 2020-06-11 21:58
 */
public class BioServer {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        try(ServerSocket serverSocket = new ServerSocket(NetConfig.SERVER_PORT)){
            System.out.println("服务端已启动，监听端口号："+NetConfig.SERVER_PORT);

            while (true){
                Socket socket = serverSocket.accept();
                InetAddress inetAddress = socket.getInetAddress();
                System.out.println("已收到一个连接："+inetAddress.getHostAddress());
                executorService.execute(new BioServerSocketHandler(socket));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
