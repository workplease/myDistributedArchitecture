package zzz.ares.remoting.framework.provider;

import org.apache.avro.ipc.NettyServer;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-05 22:21
 * @Version: 1.0
 *
 * 实现远程服务的发布
 */
public class ProviderFactoryBean implements FactoryBean, InitializingBean {

    //服务接口
    private Class<?> serviceItf;
    //服务实现
    private Object serviceObject;
    //服务端口
    private String serverPort;
    //服务超过时间
    private long timeout;
    //服务代理对象
    private Object serviceProxyObject;
    //服务提供者唯一标识
    private String appKey;
    //服务分组组名
    private String groupName = "default";
    //服务提供者权重，默认为1，范围为[1-100]
    private int weight = 1;
    //服务端线程数，默认10个线程
    private int workerThreads = 10;

    @Override
    public Object getObject() throws Exception {
        return serviceProxyObject;
    }

    @Override
    public Class<?> getObjectType() {
        return serviceItf;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //启动Netty服务端

    }
}
