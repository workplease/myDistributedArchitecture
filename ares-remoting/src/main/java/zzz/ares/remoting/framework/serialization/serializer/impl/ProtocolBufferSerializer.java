package zzz.ares.remoting.framework.serialization.serializer.impl;

import com.google.protobuf.GeneratedMessageV3;
import zzz.ares.remoting.framework.serialization.serializer.ISerializer;
import org.apache.commons.lang3.reflect.MethodUtils;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-11 15:08
 * @Version: 1.0
 *
 * protobuf 实现序列化/反序列化
 */
public class ProtocolBufferSerializer implements ISerializer {

    public <T> byte[] serialize(T obj) {
        try {
            if (!(obj instanceof GeneratedMessageV3)){
                throw new UnsupportedOperationException("not supported obj type");
            }
            return (byte[]) MethodUtils.invokeMethod(obj, "toByteArray");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T deserialize(byte[] data, Class<T> cls) {
        try {
            if (!GeneratedMessageV3.class.isAssignableFrom(cls)){
                throw new UnsupportedOperationException("not supported obj type");
            }
            Object o = MethodUtils.invokeStaticMethod(cls, "getDefaultInstance");
            return (T) MethodUtils.invokeMethod(o, "parseFrom", new Object[]{data});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
