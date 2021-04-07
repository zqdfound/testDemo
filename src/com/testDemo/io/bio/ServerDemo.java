package com.testDemo.io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zhuangqingdian
 * @date 2021/4/7
 */
public class ServerDemo {
    public static void main(String[] args) throws Exception {
        System.out.println("server start");
        ServerSocket serverSocket = new ServerSocket(8000);
        while (true) {
            //暂停等待客户端连接，得到一个socket管道
            Socket socket = serverSocket.accept();
            new ServerReadThread(socket).start();
            System.out.println(socket.getRemoteSocketAddress()+"is inline");
        }


    }
    //1.每个socket接收到都会创建一个线程，线程竞争切换会影响性能
    static class ServerReadThread extends Thread {
        private Socket socket;

        public ServerReadThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try {
                InputStream is = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader((is)));
                String line;
                while (true) {
                    if (!((line = br.readLine()) != null)){
                        System.out.println("server receive:"+ socket.getRemoteSocketAddress()+":"+line);
                    }
                }
            } catch (IOException e) {
                System.out.println(socket.getRemoteSocketAddress()+"is offline");
            }


        }
    }

}
