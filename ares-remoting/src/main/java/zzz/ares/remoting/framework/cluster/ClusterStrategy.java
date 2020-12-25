package zzz.ares.remoting.framework.cluster;

import zzz.ares.remoting.framework.model.ProviderService;

import java.util.List;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-14 11:21
 * @Version: 1.0
 *
 * 负载均衡策略算法接口
 */
public interface ClusterStrategy {

    public ProviderService select(List<ProviderService> providerServices);
}
