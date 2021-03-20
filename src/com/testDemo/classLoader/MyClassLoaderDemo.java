package com.testDemo.classLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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

}
