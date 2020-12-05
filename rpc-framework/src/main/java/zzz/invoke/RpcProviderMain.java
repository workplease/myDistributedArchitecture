package zzz.invoke;

import zzz.framework.ProviderReflect;
import zzz.service.HelloService;
import zzz.service.HelloServiceImpl;

import java.security.Provider;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-05 20:54
 * @Version: 1.0
 */
public class RpcProviderMain {

    public static void main(String[] args) throws Exception {
        HelloService service = new HelloServiceImpl();
        ProviderReflect.provider(service,8083);
    }
}
