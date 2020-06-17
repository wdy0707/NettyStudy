package cn.wdy07.netty._02_aio.client;

import java.util.Scanner;

/**
 * @author wdy
 * @create 2020-06-12 10:57
 */
public class AioClient {

    private static AioClientHandler aioClientHandler;


    public static void main(String[] args) {

        aioClientHandler = new AioClientHandler();
        new Thread(aioClientHandler).start();

        System.out.println("请输入请求消息：");
        Scanner scanner = new Scanner(System.in);
        while (aioClientHandler.sendMsg(scanner.nextLine()));

    }
}
