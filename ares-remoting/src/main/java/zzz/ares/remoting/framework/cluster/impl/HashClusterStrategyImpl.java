package zzz.ares.remoting.framework.cluster.impl;

import zzz.ares.remoting.framework.cluster.ClusterStrategy;
import zzz.ares.remoting.framework.helper.IPHelper;
import zzz.ares.remoting.framework.model.ProviderService;

import java.util.List;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-14 13:04
 * @Version: 1.0
 *
 * 软负载源地址 hash 算法
 */
public class HashClusterStrategyImpl implements ClusterStrategy {

    @Override
    public ProviderService select(List<ProviderService> providerServices) {
        //获取调用方 IP
        String localIP = IPHelper.localIp();
        //获取源地址对应的hashcode
        int hashCode = localIP.hashCode();
        //获取服务列表大小
        int size = providerServices.size();

        return providerServices.get(hashCode % size);
    }
}
