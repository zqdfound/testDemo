package com.testDemo.io.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author zhuangqingdian
 * @date 2021/4/7
 */
public class ClientDemo {

    public static void main(String[] args) throws IOException {
        System.out.println("client start");
        Socket socket = new Socket("localhost",8000);
        OutputStream outputStream = socket.getOutputStream();
        PrintStream ps = new PrintStream(outputStream);
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("input:");
            String s = scanner.nextLine();
            ps.println(s);
            ps.flush();
        }
    }
}
