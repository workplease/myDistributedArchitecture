package zzz.ares.remoting.framework.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-06 1:11
 * @Version: 1.0
 */
public class AresRemoteServiceNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("reference",new RevokerFactoryBeanDefinitionParer());
    }
}
