package com.testDemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author zhuangqingdian
 * JDK 动态代理Demo （反射）
 * @date 2021/3/18
 */
public class GeneralProxyDemo {
    static interface IServiceA{
        public void sayHelloA();
    }
    static interface IServiceB{
        public void sayHelloB();
    }

    static class ServiceAImpl implements IServiceA{
        @Override
        public void sayHelloA() {
            System.out.println("HELLO A!");
        }
    }
    static class ServiceBImpl implements IServiceB{
        @Override
        public void sayHelloB() {
            System.out.println("HELLO B");
        }
    }

    //动态代理类
    static class SimpleInvocationHandler implements InvocationHandler{
        private Object object;
        public SimpleInvocationHandler(Object object) {
            this.object = object;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("entering "+ object.getClass().getSimpleName()+"::"+method.getName());
            Object result = method.invoke(object,args);
            System.out.println("leaving "+ object.getClass().getSimpleName()+"::"+method.getName());
            return result;
        }
    }

    private static <T> T getProxy(Class<T> intf, T realObj){
        return (T) Proxy.newProxyInstance(intf.getClassLoader(),new Class<?> []{intf},new SimpleInvocationHandler(realObj));
    }

    public static void main(String[] args) {
        IServiceA a = new ServiceAImpl();
        IServiceA aProxy = getProxy(IServiceA.class,a);
        aProxy.sayHelloA();

        IServiceB b = new ServiceBImpl();
        IServiceB bProxy = getProxy(IServiceB.class,b);
        bProxy.sayHelloB();

    }

}
