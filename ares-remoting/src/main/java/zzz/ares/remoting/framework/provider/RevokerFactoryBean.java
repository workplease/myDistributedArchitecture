package zzz.ares.remoting.framework.provider;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-05 23:22
 * @Version: 1.0
 *
 * 实现远程服务的引入
 */
public class RevokerFactoryBean implements FactoryBean, InitializingBean {

    //服务接口
    private Class<?> targetInterface;
    //超时时间
    private int timeout;
    //服务 bean
    private Object serviceObject;
    //负载均衡策略
    private String clusterStrategy;
    //服务提供者唯一标识
    private String remoteAppKey;
    //服务分组组名
    private String groupName = "default";

    @Override
    public Object getObject() throws Exception {
        return serviceObject;
    }

    @Override
    public Class<?> getObjectType() {
        return targetInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //获取服务注册中心

    }
}
