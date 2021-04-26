package com.testDemo.serializable;

import java.io.Serializable;

/**序列化
 * @author zhuangqingdian
 * @date 2021/4/26
 */
public class Worker implements Serializable {
    private static final long serialVersionUID = 123456789L;
    //name会被序列化
    private String name;
    //transient修饰的变量不会被序列化 int类型反序列化后为0，对象类型为null
    private transient int salary;
    //静态变量属于类信息，不属于对象状态，不会被序列化
    static int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    }
}
