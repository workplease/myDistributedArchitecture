package zzz.ares.remoting.framework.cluster.impl;

import com.google.common.collect.Lists;
import zzz.ares.remoting.framework.cluster.ClusterStrategy;
import zzz.ares.remoting.framework.model.ProviderService;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-14 12:48
 * @Version: 1.0
 *
 * 软负载加权轮询算法实现
 */
public class WeightPollingClusterStrategyImpl implements ClusterStrategy {

    //计数器
    private int index = 0;
    private Lock lock = new ReentrantLock();

    @Override
    public ProviderService select(List<ProviderService> providerServices) {

        ProviderService service = null;
        try{
            lock.tryLock(10, TimeUnit.MILLISECONDS);
            //存放加权后的服务提供者列表
            List<ProviderService> providerServiceList = Lists.newArrayList();
            for (ProviderService provider : providerServiceList){
                int weight = provider.getWeight();
                for (int i = 0;i < weight;i++){
                    providerServiceList.add(provider.copy());
                }
            }
            //若计数大于服务提供者个数，将计数器归0
            if (index >= providerServiceList.size()){
                index = 0;
            }
            service = providerServiceList.get(index);
            index++;
            return service;
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        //兜底，保证程序健壮性，若未取到服务，则直接取第1个
        return providerServices.get(0);

    }
}
