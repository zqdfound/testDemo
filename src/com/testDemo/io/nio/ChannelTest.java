package com.testDemo.io.nio;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author zhuangqingdian
 * NIO 文件操作
 * @date 2021/4/9
 */
public class ChannelTest {
    //测试写入
    @Test
    public void testWrite() {
        try {
            //字节流目标文件
            FileOutputStream fo = new FileOutputStream("E://test.txt");
            //获取channel
            FileChannel fc = fo.getChannel();
            //分配缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.put("桃李春风一杯酒".getBytes(StandardCharsets.UTF_8));
            //切换缓冲区为写入模式
            byteBuffer.flip();
            fc.write(byteBuffer);
            fc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //测试读取
    @Test
    public void testRead() {
        try {
            FileInputStream fi = new FileInputStream("E://test.txt");
            FileChannel channel = fi.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            channel.read(byteBuffer);
            byteBuffer.flip();
            String s = new String(byteBuffer.array(), 0, byteBuffer.remaining());
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //从目标通道复制原通道数据
    @Test
    public void transferFromTo() {
        try {
            FileInputStream fi = new FileInputStream("E://1.txt");
            FileOutputStream fo = new FileOutputStream("E://2.txt");
            FileChannel inChannel = fi.getChannel();
            FileChannel outChannel = fo.getChannel();
            //从目标通道复制原通道数据
            outChannel.transferFrom(inChannel, inChannel.position(), inChannel.size());
            //将原通道数据复制到目标通道
            //inChannel.transferTo(inChannel.position(),inChannel.size(),outChannel);
            inChannel.close();
            outChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
