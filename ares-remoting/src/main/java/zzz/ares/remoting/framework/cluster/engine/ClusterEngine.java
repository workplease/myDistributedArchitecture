package zzz.ares.remoting.framework.cluster.engine;

import com.google.common.collect.Maps;
import zzz.ares.remoting.framework.cluster.ClusterStrategy;
import zzz.ares.remoting.framework.cluster.impl.*;

import java.util.Map;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-14 13:15
 * @Version: 1.0
 *
 * 自定义软负载机制
 */
public class ClusterEngine {

    private static final Map<ClusterStrategyEnum, ClusterStrategy> clusterStrategyMap = Maps.newConcurrentMap();

    static {
        clusterStrategyMap.put(ClusterStrategyEnum.Random,new RandomClusterStrategyImpl());
        clusterStrategyMap.put(ClusterStrategyEnum.WeightRandom,new WeightRandomClusterStrategyImpl());
        clusterStrategyMap.put(ClusterStrategyEnum.Polling,new PollingClusterStrategyImpl());
        clusterStrategyMap.put(ClusterStrategyEnum.WeightPolling,new WeightPollingClusterStrategyImpl());
        clusterStrategyMap.put(ClusterStrategyEnum.Hash,new HashClusterStrategyImpl());
    }

    public static ClusterStrategy queryClusterStrategy(String clusterStrategy){
        ClusterStrategyEnum clusterStrategyEnum = ClusterStrategyEnum.queryByCode(clusterStrategy);
        if (clusterStrategyEnum == null){
            //默认选择随机算法
            return new RandomClusterStrategyImpl();
        }
        return clusterStrategyMap.get(clusterStrategyEnum);
    }
}
