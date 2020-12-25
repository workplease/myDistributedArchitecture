package zzz.ares.remoting.framework.spring;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;
import zzz.ares.remoting.framework.provider.RevokerFactoryBean;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-06 1:14
 * @Version: 1.0
 *
 * 解析远程服务引入的自定义标签类
 */
public class RevokerFactoryBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    //logger
    private static final Logger logger = LoggerFactory.getLogger(RevokerFactoryBeanDefinitionParser.class);

    protected Class getBeanClass(Element element){
        return RevokerFactoryBean.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        try{
            String timeOut = element.getAttribute("timeout");
            String targetInterface = element.getAttribute("interface");
            String clusterStrategy = element.getAttribute("clusterStrategy");
            String remoteAppKey = element.getAttribute("remoteAppKey");
            String groupName = element.getAttribute("groupName");

            bean.addPropertyValue("timeout",Integer.parseInt(timeOut));
            bean.addPropertyValue("targetInterface",Class.forName(targetInterface));
            bean.addPropertyValue("remoteAppKey",remoteAppKey);

            if (StringUtils.isNotBlank(clusterStrategy)){
                bean.addPropertyValue("clusterStrategy",clusterStrategy);
            }
            if (StringUtils.isNotBlank(groupName)){
                bean.addPropertyValue("groupName",groupName);
            }
        }catch (Exception e){
            logger.error("RevokerFactorBeanDefinitionParser error.",e);
            throw new RuntimeException(e);
        }
    }
}
