package com.testDemo.thread;

import org.junit.jupiter.api.Test;

/**
 * 线程交替打印0-9
 * @author zhuangqingdian
 * @date 2021/4/27
 */
public class ThreadDemo {

    static volatile int i = 0;

    boolean flag = false;

    @Test
    public void test() {
        new Thread(()->{
            while(i < 10){
                synchronized (this){
                    if(flag){
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else{
                        System.out.println(Thread.currentThread().getName()+"- is print:" + i);
                        i++;
                        flag = !flag;
                        this.notifyAll();
                    }

                }
            }
        }).start();

        new Thread(()->{
            while(i < 10){
                synchronized (this){
                    if(!flag){
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else{
                        System.out.println(Thread.currentThread().getName()+"- is print:" + i);
                        i++;
                        flag = !flag;
                        this.notifyAll();
                    }

                }
            }
        }).start();
    }

}
