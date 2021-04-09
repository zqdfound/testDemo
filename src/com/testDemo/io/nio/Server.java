package com.testDemo.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @author zhuangqingdian
 * NIO聊天室Server
 * @date 2021/4/9
 */
public class Server {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private static final int PORT = 8000;

    public Server() {
        try {
            //获取通道
            serverSocketChannel = ServerSocketChannel.open();
            //切换为非阻塞
            serverSocketChannel.configureBlocking(false);
            //绑定端口
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            //获取selector
            selector = Selector.open();
            //将通道注册到selector上并开始指定接受事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听
    public void listen() {
        System.out.println("监听线程：" + Thread.currentThread().getName());
        try {
            while (!(selector.select() > 0)) {
                System.out.println("开始处理事件");
                //获取Selector中所有注册好的通道中已经就绪的事件
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                //遍历事件
                while (it.hasNext()) {
                    //提取事件
                    SelectionKey selectionKey = it.next();
                    if (selectionKey.isAcceptable()) {
                        //进入当前客户端Channel
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        //切换为非阻塞
                        socketChannel.configureBlocking(false);
                        //将该channel注册到selector
                        System.out.println(socketChannel.getRemoteAddress() + "上线");
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        readData(selectionKey);
                    }
                    //处理完毕移除当前事件
                    it.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //读取客户端消息
    private void readData(SelectionKey key){
        SocketChannel channel = null;
        try {
            //获取channel
            channel = (SocketChannel) key.channel();
            //创建Buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = 0;
            count = channel.read(buffer);
            if(count > 0){
                //转为字符串
                String s = new String(buffer.array());
                System.out.println("客户端消息："+ s);
                //发送消息给其他客户端
                sendMsgToOtherClients(s,channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress()+"已离线");
                e.printStackTrace();
                //取消注册
                key.cancel();
                channel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    private void sendMsgToOtherClients(String s, SocketChannel channel) throws IOException {
        System.out.println("Server转发消息给客户端线程:" + Thread.currentThread().getName());
        //遍历所有Selector上的channel并排除自己
        for (SelectionKey key: selector.keys()) {
            Channel targetChannel = key.channel();
            if(targetChannel instanceof SocketChannel && targetChannel != channel){
                SocketChannel dest = (SocketChannel) targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8));
                //将buffer写入通道
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        while(true){
            server.listen();
        }
    }
}
