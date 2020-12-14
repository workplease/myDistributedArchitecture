package zzz.ares.remoting.framework.model;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-11 19:28
 * @Version: 1.0
 */
public class RevokerResponseHolder {

    //服务返回结果 Map
    private static final Map<String,AresResponseWrapper> responseMap = Maps.newConcurrentMap();
    //清除过期的返回结果
    private static final ExecutorService removeExpireKeyExecutor = Executors.newSingleThreadExecutor();

    static {
        //删除超时未获取到结果的 Key，防止内存泄漏
        removeExpireKeyExecutor.execute(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try{
                        for (Map.Entry<String,AresResponseWrapper> entry : responseMap.entrySet()){
                            boolean isExpire = entry.getValue().isExpire();
                            if (isExpire){
                                responseMap.remove(entry.getKey());
                            }
                            Thread.sleep(10);
                        }
                    }catch (Throwable e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 初始化返回结果容器，requestUniqueKey 唯一识别本次调用
     * @param requestUniqueKey
     */
    public static void initResponseData(String requestUniqueKey){
        responseMap.put(requestUniqueKey,AresResponseWrapper.of());
    }

    /**
     * 将 Netty 调用异步返回结果放入阻塞队列
     * @param response
     */
    public static void putResultValue(AresResponse response){
        long currentTime = System.currentTimeMillis();
        AresResponseWrapper responseWrapper = responseMap.get(response.getUniqueKey());
        responseWrapper.setResponseTime(currentTime);
        responseWrapper.getResponseQueue().add(response);
        responseMap.put(response.getUniqueKey(),responseWrapper);
    }

    /**
     * 从阻塞队列中获取 Netty 异步返回的结果值
     * @param requestUniqueKey
     * @param timeout
     * @return
     */
    public static AresResponse getValue(String requestUniqueKey,long timeout){
        AresResponseWrapper responseWrapper = responseMap.get(requestUniqueKey);
        try{
            return responseWrapper.getResponseQueue().poll(timeout, TimeUnit.MILLISECONDS);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }finally {
            responseMap.remove(requestUniqueKey);
        }
    }
}
