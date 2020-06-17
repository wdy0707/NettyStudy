package cn.wdy07.netty._03_nio;

import java.util.Scanner;

/**
 * @author wdy
 * @create 2020-06-13 11:21
 */
public class NioClient {

    private static NioClientHandler clientHandler;

    public static void main(String[] args) {
        if(clientHandler!=null){
            clientHandler.stop();
        }

        clientHandler = new NioClientHandler(NetConfig.SERVER_IP,NetConfig.SERVER_PORT);
        new Thread(clientHandler).start();

        Scanner scanner = new Scanner(System.in);
        while (clientHandler.sendMsg(scanner.nextLine()));
    }
}
