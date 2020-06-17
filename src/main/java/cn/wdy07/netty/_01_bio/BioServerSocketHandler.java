package cn.wdy07.netty._01_bio;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wdy
 * @create 2020-06-11 21:58
 */
public class BioServerSocketHandler implements Runnable{
    private Socket socket;

    public BioServerSocketHandler(Socket socket){
        this.socket = socket;
    }


    @Override
    public void run() {
        String line;
        String resMsg;
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true)){
            while (true){
                line = reader.readLine();
                System.out.println("服务端接收到消息："+line);
                resMsg = processMsg(line);
                printWriter.println(resMsg);
            }

        }catch (SocketException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private static String processMsg(String msg){
        return "Server Response Msg:"+msg+"; Accept Time: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
