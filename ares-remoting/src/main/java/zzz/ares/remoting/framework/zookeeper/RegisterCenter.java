package zzz.ares.remoting.framework.zookeeper;

import com.google.common.collect.Maps;
import org.omg.CORBA.PRIVATE_MEMBER;
import zzz.ares.remoting.framework.helper.PropertyConfigHelper;
import zzz.ares.remoting.framework.model.InvokerService;
import zzz.ares.remoting.framework.model.ProviderService;

import java.util.List;
import java.util.Map;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-06 11:10
 * @Version: 1.0
 *
 * 注册中心实现
 */
public class RegisterCenter implements IRegisterCenter4Invoker,IRegisterCenter4Provider{

    private static RegisterCenter registerCenter = new RegisterCenter();

    //服务提供者列表，key：服务提供者接口，value：服务提供者方法列表
    private static final Map<String,List<ProviderService>> providerServiceMap = Maps.newConcurrentMap();

    //服务端zk服务元信息，选择服务（第一次直接从zk拉取，后续由zk的监听机制主动更新）
    private static final Map<String,List<ProviderService>> serviceMetaDataMap4Consume = Maps.newConcurrentMap();

    //从配置文件中获取ZK的服务地址列表
    private static String ZK_SERVICE = PropertyConfigHelper.getZkService();
    //从配置文件中获取ZK会话超时时间配置
    private static int ZK_SESSION_TIME_OUT = PropertyConfigHelper.getZkSessionTimeout();
    //从配置文件中获取ZK连接超时时间配置
    private static int ZK_CONNECTION_TIME_OUT = PropertyConfigHelper.getZkConnectionTimeout();

    //组装ZK根路径/APPKEY路径
    private static String ROOT_PATH = "/config_register";



    @Override
    public void initProviderMap() {

    }

    @Override
    public Map<String, List<ProviderService>> getServiceMetaDataMao4Consume() {
        return null;
    }

    @Override
    public void registerInvoker(InvokerService invoker) {

    }

    @Override
    public void registerProvider(List<ProviderService> serviceMetaData) {

    }

    @Override
    public Map<String, List<ProviderService>> getProviderServiceMap() {
        return null;
    }
}
