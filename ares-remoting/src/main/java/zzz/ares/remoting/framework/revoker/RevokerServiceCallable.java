package zzz.ares.remoting.framework.revoker;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zzz.ares.remoting.framework.model.AresRequest;
import zzz.ares.remoting.framework.model.AresResponse;
import zzz.ares.remoting.framework.model.RevokerResponseHolder;

import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-11 19:45
 * @Version: 1.0
 *
 * Netty 请求发起调用线程
 */
public class RevokerServiceCallable implements Callable<AresResponse> {

    private static final Logger logger = LoggerFactory.getLogger(RevokerServiceCallable.class);

    private Channel channel;
    private InetSocketAddress inetSocketAddress;
    private AresRequest request;

    public static RevokerServiceCallable of(InetSocketAddress inetSocketAddress,AresRequest request){
        return new RevokerServiceCallable(inetSocketAddress,request);
    }

    public RevokerServiceCallable(InetSocketAddress inetSocketAddress,AresRequest request){
        this.inetSocketAddress = inetSocketAddress;
        this.request = request;
    }

    @Override
    public AresResponse call() throws Exception {
        //初始化返回结果容器，将本次调用的唯一标识符作为 Key 存入返回结果的 Map
        RevokerResponseHolder.initResponseData(request.getUniqueKey());
        //根据本地调用服务提供者地址获取相应的 Netty 通道 channel 队列
        ArrayBlockingQueue<Channel> blockingQueue =
                NettyChannelPoolFactory.channelPoolFactoryInstance().acquire(inetSocketAddress);
        try{
            if (channel == null){
                //从队列中获取本次调用的 Netty 通道 channel
                channel = blockingQueue.poll(request.getInvokeTimeout(), TimeUnit.MILLISECONDS);
            }

            //若获取的 channel 通道已经不可用，则重新获取一个
            while (!channel.isOpen() || !channel.isActive() || !channel.isWritable()){
                logger.warn("---------------------retry get new Channel---------------------");
                channel = blockingQueue.poll(request.getInvokeTimeout(),TimeUnit.MILLISECONDS);
                if (channel == null){
                    //若队列中没有可用的 Channel，则重新注册一个 Channel
                    channel = NettyChannelPoolFactory.channelPoolFactoryInstance().registerChannel(inetSocketAddress);
                }
            }

            //将本次调用的信息写入 Netty 通道，发起异步调用
            ChannelFuture channelFuture = channel.writeAndFlush(request);
            channelFuture.syncUninterruptibly();

            //从返回结果容器中获取返回结果，同时设置等待超时时间未 invokeTimeout
            long invokeTimeout = request.getInvokeTimeout();
            return RevokerResponseHolder.getValue(request.getUniqueKey(),invokeTimeout);
        }catch (Exception e){
            logger.error("service invoke error.",e);
        }finally {
            //本次调用完毕后，将 Netty 的通道 channel 重新释放到队列中，以便下次调用复用
            NettyChannelPoolFactory.channelPoolFactoryInstance().release(blockingQueue,channel,inetSocketAddress);
        }
        return null;
    }
}
