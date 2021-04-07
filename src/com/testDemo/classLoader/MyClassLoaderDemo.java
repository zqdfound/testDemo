package com.testDemo.classLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhuangqingdian
 * 自定义类加载器
 * @date 2021/3/20
 */
public class MyClassLoaderDemo extends ClassLoader{
    protected  final String BaseUrl = "";
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
       String fileName = name.replace("\\.","/");
       fileName = BaseUrl + fileName + ".class";
        try {
            byte[] bytes = toByteArray(fileName);
            return defineClass(name,bytes,0,bytes.length);
        } catch (IOException e) {
           throw new ClassNotFoundException("找不到指定类"+name,e);
        }
    }



    /**
     * NIO way
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(String filename) throws IOException {

        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }

        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
                // System.out.println("reading");
            }
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /*
    对于同一个类，使用相同的自定义类加载器加载两次后将得到同一个类的不同对象，好处：
    1.实现隔离：例如Tomcat使用此机制隔离不同的Web应用
    2.热部署：使用ClassLoader，类只会加载一次，即使class改变了再次加载后还是原来的对象，而使用自定义classloader类，可以创建多个自定义classloader再用他加载class得到的class就是新的，从而实现动态更新。
     */
    public static void main(String[] args) throws ClassNotFoundException {
        MyClassLoaderDemo c1 = new MyClassLoaderDemo();
        String className = "";
        Class<?> class1 = c1.findClass(className);
        MyClassLoaderDemo c2 = new MyClassLoaderDemo();
        Class<?> class2 = c2.findClass(className);
        System.out.println(class1 == class2);//false
    }
}
