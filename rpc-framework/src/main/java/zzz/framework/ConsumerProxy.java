package zzz.framework;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-05 20:34
 * @Version: 1.0
 *
 * 服务消费者代理类
 */
public class ConsumerProxy {

    /**
     * 服务消费代理接口
     * @param interfaceClass
     * @param host
     * @param port
     * @param <T>
     * @return
     * @throws Exception
     */
    @SuppressWarnings("all")
    public static <T> T consume(final Class<T> interfaceClass,final String host,final int port) throws Exception{
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = new Socket(host, port);
                try{
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    try{
                        output.writeUTF(method.getName());
                        output.writeObject(args);
                        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                        try{
                            Object result = input.readObject();
                            if (result instanceof Throwable){
                                throw (Throwable) result;
                            }
                            return result;
                        }finally {
                            input.close();
                        }
                    }finally {
                        output.close();
                    }
                }finally {
                    socket.close();
                }
            }
        });
    }
}
