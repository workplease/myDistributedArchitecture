package zzz.ares.remoting.framework.cluster.impl;

import zzz.ares.remoting.framework.cluster.ClusterStrategy;
import zzz.ares.remoting.framework.model.ProviderService;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-14 11:30
 * @Version: 1.0
 *
 * 软负载轮询算法
 */
public class PollingClusterStrategyImpl implements ClusterStrategy {

    //计数器
    private int index = 0;
    private Lock lock = new ReentrantLock();

    @Override
    public ProviderService select(List<ProviderService> providerServices) {
        ProviderService service = null;
        try{
            lock.tryLock(10, TimeUnit.MILLISECONDS);
            //若计数大于服务提供者个数，将计数器归0
            if (index >= providerServices.size()){
                index = 0;
            }
            service = providerServices.get(index);
            index ++;
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        //兜底，保证程序健壮性，若未取到服务，则直接取第1个
        if (service == null){
            service = providerServices.get(0);
        }
        return service;
    }
}
