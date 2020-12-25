package zzz.ares.remoting.framework.cluster.impl;

import org.apache.commons.lang3.RandomUtils;
import zzz.ares.remoting.framework.cluster.ClusterStrategy;
import zzz.ares.remoting.framework.model.ProviderService;

import java.util.List;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-14 11:23
 * @Version: 1.0
 *
 * 软负载随机算法的实现
 */
public class RandomClusterStrategyImpl implements ClusterStrategy {
    @Override
    public ProviderService select(List<ProviderService> providerServices) {
        int MAX_LEN = providerServices.size();
        int index = RandomUtils.nextInt(0,MAX_LEN - 1);
        return providerServices.get(index);
    }
}
