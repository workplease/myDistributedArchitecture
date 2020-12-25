package zzz.ares.remoting.framework.serialization.serializer.impl;

import zzz.ares.remoting.framework.serialization.serializer.ISerializer;
import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-11 15:08
 * @Version: 1.0
 *
 * Thrift 实现序列化/反序列化
 */
public class ThriftSerializer implements ISerializer {

    public <T> byte[] serialize(T obj) {
        try {
            if (!(obj instanceof TBase)){
                throw new UnsupportedOperationException("not supported obj type");
            }
            TSerializer serializer = new TSerializer(new TBinaryProtocol.Factory());
            return serializer.serialize((TBase) obj);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T deserialize(byte[] data, Class<T> clazz) {
        try {
            if (!TBase.class.isAssignableFrom(clazz)){
                throw new UnsupportedOperationException("not supported obj type");
            }
            TBase o = (TBase) clazz.newInstance();
            TDeserializer tDeserializer = new TDeserializer();
            tDeserializer.deserialize(o, data);
            return (T) o;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
