package zzz.ares.remoting.framework.revoker;

import zzz.ares.remoting.framework.cluster.ClusterStrategy;
import zzz.ares.remoting.framework.cluster.engine.ClusterEngine;
import zzz.ares.remoting.framework.model.AresRequest;
import zzz.ares.remoting.framework.model.AresResponse;
import zzz.ares.remoting.framework.model.ProviderService;
import zzz.ares.remoting.framework.zookeeper.IRegisterCenter4Invoker;
import zzz.ares.remoting.framework.zookeeper.RegisterCenter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-14 13:23
 * @Version: 1.0
 *
 * 远程服务在服务调用方的动态代理类实现
 */
public class RevokerProxyBeanFactory implements InvocationHandler {

    private ExecutorService fixedThreadPool = null;
    //服务接口
    private Class<?> targetInterface;
    //超时时间
    private int consumeTimeout;
    //调用者线程数
    private static int threadWorkerNumber = 10;
    //负载均衡策略
    private String clusterStrategy;

    public RevokerProxyBeanFactory(Class<?> targetInterface,int consumeTimeout,String clusterStrategy){
        this.targetInterface = targetInterface;
        this.consumeTimeout = consumeTimeout;
        this.clusterStrategy = clusterStrategy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //服务接口名称
        String serviceKey = targetInterface.getName();
        //获取某个接口的服务提供者列表
        IRegisterCenter4Invoker registerCenter4Consumer = RegisterCenter.singleton();
        List<ProviderService> providerServices = registerCenter4Consumer.getServiceMetaDataMap4Consume().get(serviceKey);

        //根据客户端配置信息选择负载策略具体实现
        ClusterStrategy clusterStrategyService = ClusterEngine.queryClusterStrategy(clusterStrategy);
        //根据软负载策略，从服务提供者列表选取本次调用的服务提供者
        ProviderService providerService = clusterStrategyService.select(providerServices);

        //复制一份服务提供者信息
        ProviderService newProvider = providerService.copy();
        //设置本次调用服务的方法及接口
        newProvider.setServiceMethod(method);
        newProvider.setServiceItf(targetInterface);

        //声明调用 AresRequest 对象，AresRequest 表示发起一次调用所包含的信息
        final AresRequest request = new AresRequest();
        //设置本次调用的唯一标识
        request.setUniqueKey(UUID.randomUUID().toString() + "-" + Thread.currentThread().getId());
        //设置本次调用的服务提供者消息
        request.setProviderService(newProvider);
        //设置本次调用的超时时间
        request.setInvokeTimeout(consumeTimeout);
        //设置本次调用的方法名称
        request.setInvokedMethodName(method.getName());
        //设置本次调用的方法参数信息
        request.setArgs(args);

        try{
            //构建用来发起调用的线程池
            if (fixedThreadPool == null){
                synchronized (RevokerProxyBeanFactory.class){
                    if (null == fixedThreadPool){
                        fixedThreadPool = Executors.newFixedThreadPool(threadWorkerNumber);
                    }
                }
            }
            //根据服务提供者的 IP、port，构建 InetSocketAddress 对象，标识服务提供者地址
            String serverIp = request.getProviderService().getServerIp();
            int serverPort = request.getProviderService().getServerPort();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(serverIp,serverPort);

            //提交本次调用信息到线程池 fixedThreadPool，发起调用
            Future<AresResponse> responseFuture = fixedThreadPool.submit(RevokerServiceCallable.of(inetSocketAddress,request));
            //获取调用的返回结果
            AresResponse response = responseFuture.get(request.getInvokeTimeout(), TimeUnit.MILLISECONDS);
            if (response != null){
                return response.getResult();
            }
        }catch (Exception e){
            throw new RuntimeException();
        }
        return null;
    }

    public Object getProxy(){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class<?>[]{targetInterface},this);
    }

    private static volatile RevokerProxyBeanFactory singleton;

    public static RevokerProxyBeanFactory singleton(Class<?> targetInterface,int consumeTimeout,String clusterStrategy){
        if (null == singleton){
            synchronized (RevokerProxyBeanFactory.class){
                if (null == singleton){
                    singleton = new RevokerProxyBeanFactory(targetInterface, consumeTimeout, clusterStrategy);
                }
            }
        }
        return singleton;
    }
}
