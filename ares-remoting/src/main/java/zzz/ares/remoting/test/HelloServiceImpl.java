package zzz.ares.remoting.test;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-05 23:17
 * @Version: 1.0
 */
public class HelloServiceImpl implements HelloService{
    @Override
    public String sayHello(String somebody) {
        return "hello," + somebody + "!";
    }
}
