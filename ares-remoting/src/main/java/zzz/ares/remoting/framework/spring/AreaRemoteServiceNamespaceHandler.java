package zzz.ares.remoting.framework.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-06 1:31
 * @Version: 1.0
 *
 * 为远程服务发布自定义标签处理类
 */
public class AreaRemoteServiceNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("service",new ProviderFactoryBeanDefinitionParser());
    }
}
