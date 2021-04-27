package com.testDemo.serializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** 序列化反序列化测试
 * @author zhuangqingdian
 * @date 2021/4/26
 */
public class SerializableTest {
    public static void main(String[] args) throws Exception{
        //序列化到磁盘
        FileOutputStream fos = new FileOutputStream("E://test.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        Worker worker = new Worker();
        worker.setName("张三");
        worker.setSalary(13);//transient修饰的变量无法被序列化
        oos.writeObject(worker);
        oos.flush();
        oos.close();
        //反序列化
        FileInputStream fis = new FileInputStream("E://test.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Worker w = (Worker) ois.readObject();
        System.out.println(w.toString());
    }


}
