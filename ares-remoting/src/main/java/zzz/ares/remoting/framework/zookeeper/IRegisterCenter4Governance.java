package zzz.ares.remoting.framework.zookeeper;

import zzz.ares.remoting.framework.model.InvokerService;
import zzz.ares.remoting.framework.model.ProviderService;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-11 9:36
 * @Version: 1.0
 *
 * 服务治理接口
 */
public interface IRegisterCenter4Governance {

    /**
     * 获取服务提供者列表与服务消费者列表
     * @param serviceName
     * @param appKey
     * @return
     */
    public Pair<List<ProviderService>, List<InvokerService>> queryProvidersAndInvokers(String serviceName, String appKey);
}
