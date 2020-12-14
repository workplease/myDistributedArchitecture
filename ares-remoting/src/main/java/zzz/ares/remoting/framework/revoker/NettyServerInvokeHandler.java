package zzz.ares.remoting.framework.revoker;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zzz.ares.remoting.framework.model.AresRequest;
import zzz.ares.remoting.framework.model.AresResponse;
import zzz.ares.remoting.framework.model.ProviderService;
import zzz.ares.remoting.framework.zookeeper.IRegisterCenter4Provider;
import zzz.ares.remoting.framework.zookeeper.RegisterCenter;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-11 15:47
 * @Version: 1.0
 *
 * 处理服务端的逻辑
 */
@ChannelHandler.Sharable
public class NettyServerInvokeHandler extends SimpleChannelInboundHandler<AresRequest> {

    private static final Logger Logger = LoggerFactory.getLogger(NettyServerInvokeHandler.class);

    //服务端限流
    private static final Map<String, Semaphore> serviceKeySemaphoreMap = Maps.newConcurrentMap();

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        //发生异常，关闭链路
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AresRequest request) throws Exception {
        if (channelHandlerContext.channel().isWritable()){
            //从服务调用对象里获取服务端提供者信息
            ProviderService metaDataModel = request.getProviderService();
            long consumeTimeOut = request.getInvokeTimeout();
            final String methodName = request.getInvokedMethodName();

            //根据方法名称定位到具体某一个服务提供者
            String serviceKey = metaDataModel.getServiceItf().getName();
            //获取限流工具类
            int workerThread = metaDataModel.getWorkerThreads();
            Semaphore semaphore = serviceKeySemaphoreMap.get(serviceKey);
            //初始化流控基础设施 semaphore
            if (semaphore == null){
                synchronized (serviceKeySemaphoreMap){
                    semaphore = serviceKeySemaphoreMap.get(serviceKey);
                    serviceKeySemaphoreMap.put(serviceKey,semaphore);
                }
            }

            //获取注册中心服务
            IRegisterCenter4Provider registerCenter4Provider = RegisterCenter.singleton();
            List<ProviderService> localProviderCaches = registerCenter4Provider.getProviderServiceMap().get(serviceKey);
            ProviderService localProviderCache = Collections2.filter(localProviderCaches, new Predicate<ProviderService>() {
                @Override
                public boolean apply(ProviderService input) {
                    return StringUtils.equals(input.getServiceMethod().getName(),methodName);
                }
            }).iterator().next();
            Object serviceObject = localProviderCache.getServiceObject();

            //利用反射发起服务调用
            Method method = localProviderCache.getServiceMethod();
            Object result = null;
            boolean acquire = false;
            try{
                //利用 semaphore 实现限流
                acquire = semaphore.tryAcquire(consumeTimeOut, TimeUnit.MILLISECONDS);
                if (acquire){
                    //利用反射发起服务调用
                    result = method.invoke(serviceObject,request.getArgs());
                }
            }catch (Exception e){
                result = e;
            }finally {
                if (acquire){
                    semaphore.release();
                }
            }
            //根据服务调用结果组装调用返回对象
            AresResponse response = new AresResponse();
            response.setInvokeTimeout(consumeTimeOut);
            response.setUniqueKey(request.getUniqueKey());
            response.setResult(result);
            //将服务调用返回对象回写到消费端
            channelHandlerContext.writeAndFlush(response);
        }else {
            Logger.error("----------------channel closed!-----------------");
        }
    }
}
