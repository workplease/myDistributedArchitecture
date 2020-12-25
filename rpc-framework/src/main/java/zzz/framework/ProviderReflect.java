package zzz.framework;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-05 20:41
 * @Version: 1.0
 *
 * 服务发布
 */
public class ProviderReflect {

    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * 服务的发布
     * @param service
     * @param port
     * @throws Exception
     */
    public static void provider(final Object service,int port) throws Exception{
        final ServerSocket serverSocket = new ServerSocket(port);
        while (true){
            final Socket socket = serverSocket.accept();
            executorService.execute(new Runnable() {
                public void run() {
                    try{
                        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                        try{
                            try{
                                //方法名称
                                String methodName = input.readUTF();
                                //方法参数
                                Object[] arguments = (Object[]) input.readObject();
                                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                                try{
                                    //方法引用
                                    Object result = MethodUtils.invokeExactMethod(service,methodName,arguments);
                                    output.writeObject(result);
                                }catch (Throwable t){
                                    output.writeObject(t);
                                }finally {
                                    output.close();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }finally {
                                input.close();
                            }
                        }finally {
                            socket.close();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
