package zzz.service;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-05 20:31
 * @Version: 1.0
 *
 * 远程服务接口实现：Service Impl
 */
public class HelloServiceImpl implements HelloService{
    public String sayHello(String content) {
        return "hello," + content;
    }
}
