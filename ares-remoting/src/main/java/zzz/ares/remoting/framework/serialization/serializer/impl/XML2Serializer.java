package zzz.ares.remoting.framework.serialization.serializer.impl;

import zzz.ares.remoting.framework.serialization.serializer.ISerializer;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-24 19:29
 * @Version: 1.0
 *
 * Java 自带方式实现 XML 序列化/反序列化
 */
public class XML2Serializer implements ISerializer {
    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLEncoder xe = new XMLEncoder(out,"utf-8",true,0);
        xe.writeObject(obj);
        xe.close();
        return out.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        XMLDecoder xd = new XMLDecoder(new ByteArrayInputStream(data));
        Object obj = xd.readObject();
        xd.close();
        return (T) obj;
    }
}
