package zzz.invoke;

import zzz.framework.ConsumerProxy;
import zzz.service.HelloService;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-05 20:55
 * @Version: 1.0
 */
public class RpcConsumerMain {

    public static void main(String[] args) throws Exception {
        HelloService service = ConsumerProxy.consume(HelloService.class,"127.0.0.1",8083);
        for (int i = 0;i < 1000;i++){
            String hello = service.sayHello("zzz_"+i);
            System.out.println(hello);
            Thread.sleep(1000);
        }
    }
}
