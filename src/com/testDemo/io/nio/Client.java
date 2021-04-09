package com.testDemo.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author zhuangqingdian
 * NIO聊天室 Client
 * @date 2021/4/9
 */
public class Client{
    private final String HOST = "127.0.0.1";//server host
    private final int PORT = 8000;//server port
    private SocketChannel socketChannel;
    private Selector selector;
    private String username;

    public Client() throws IOException {
        selector = Selector.open();
        //连接服务器
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST,PORT));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //注册channel到selector
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString().substring(1);
    }

    //发送消息到服务器
    public void sendMsg(String msg){
        msg = username + "说: " + msg;
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //读取消息
    public void readMsg(){
        try {
            int readChannels = selector.select();
            //有可用通道
            if(readChannels > 0){
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while(it.hasNext()){
                    SelectionKey key = it.next();
                    if(key.isReadable()){
                        //获取通道
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        socketChannel.read(buffer);
                        //将读取到的buffer转为字符串
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }
                }
                it.remove();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        //3s接收一次数据
        new Thread(){
            @Override
            public void run() {
                while(true){
                    client.readMsg();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        //发送数据
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()){
            String s = scanner.nextLine();
            client.sendMsg(s);
        }

    }

}
