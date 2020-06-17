package cn.wdy07.netty._01_bio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author wdy
 * @create 2020-06-11 20:41
 */
public class BioClient {

    public static void main(String[] args) {

        try(Socket socket = new Socket(NetConfig.SERVER_IP,NetConfig.SERVER_PORT);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
            Scanner scanner = new Scanner(System.in)){

            new Thread(new ReadMsg(socket)).start();

            while (true){
                printWriter.println(scanner.next());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static class ReadMsg implements Runnable{
        private Socket socket;

        public ReadMsg(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {

            try(BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
                String line = null;

                while ((line = reader.readLine())!=null){
                    System.out.println(line);
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

}
