package zzz.ares.remoting.framework.zookeeper;

import zzz.ares.remoting.framework.model.ProviderService;

import java.util.List;
import java.util.Map;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-06 10:58
 * @Version: 1.0
 *
 * 服务端注册中心接口
 */
public interface IRegisterCenter4Provider {

    /**
     * 服务端将服务提供者信息注册到zk对应的节点下
     *
     * @param serviceMetaData
     */
    public void registerProvider(final List<ProviderService> serviceMetaData);


    /**
     * 服务端获取服务提供者信息
     * <p/>
     * 注:返回对象,Key:服务提供者接口  value:服务提供者服务方法列表
     * @return
     */
    public Map<String, List<ProviderService>> getProviderServiceMap();
}
