package zzz.ares.remoting.framework.serialization.serializer.impl;

import zzz.ares.remoting.framework.serialization.serializer.ISerializer;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-11 15:08
 * @Version: 1.0
 *
 * XStream 实现 XML 序列化/反序列化
 */
public class XmlSerializer implements ISerializer {

    //初始化 XStream 对象
    private static final XStream xStream = new XStream(new DomDriver());

    public <T> byte[] serialize(T obj) {
        return xStream.toXML(obj).getBytes();
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        String xml = new String(data);
        return (T) xStream.fromXML(xml);
    }
}
