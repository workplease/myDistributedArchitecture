package zzz.ares.remoting.framework.serialization.engine;

import com.google.common.collect.Maps;
import zzz.ares.remoting.framework.serialization.common.SerializeType;
import zzz.ares.remoting.framework.serialization.serializer.ISerializer;
import zzz.ares.remoting.framework.serialization.serializer.impl.*;

import java.util.Map;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-06 11:21
 * @Version: 1.0
 *
 * 通用的序列化工具引擎
 */
public class SerializerEngine {

    public static final Map<SerializeType, ISerializer> serializerMap = Maps.newConcurrentMap();

    //注册序列化工具类型 serializerMap
    static {
        serializerMap.put(SerializeType.DefaultJavaSerializer,new DefaultJavaSerializer());
        serializerMap.put(SerializeType.HessianSerializer,new HessianSerializer());
        serializerMap.put(SerializeType.JSONSerializer,new JSONSerializer());
        serializerMap.put(SerializeType.XmlSerializer,new XmlSerializer());
        serializerMap.put(SerializeType.ProtoStuffSerializer,new ProtoStuffSerializer());
        serializerMap.put(SerializeType.MarshallingSerializer,new MarshallingSerializer());

        //以下三个类型不能使用普通的Java Bean，需要根据各自 TDL 编译生成的类
        serializerMap.put(SerializeType.AvroSerializer,new AvroSerializer());
        serializerMap.put(SerializeType.ThriftSerializer,new ThriftSerializer());
        serializerMap.put(SerializeType.ProtocolBufferSerializer,new ProtocolBufferSerializer());
    }

    /**
     * 序列化
     * @param obj
     * @param serializeType
     * @param <T>
     * @return
     */
    public static <T> byte[] serialize(T obj,String serializeType){
        SerializeType serialize = SerializeType.queryByType(serializeType);
        if (serialize == null){
            throw new RuntimeException("serializer is null");
        }
        ISerializer serializer = serializerMap.get(serialize);
        if (serializer == null){
            throw new RuntimeException("serialize error");
        }
        try{
            return serializer.serialize(obj);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 反序列化
     * @param data
     * @param clazz
     * @param serializeType
     * @param <T>
     * @return
     */
    public static <T> T deserialize(byte[] data,Class<T> clazz,String serializeType){
        SerializeType serialize = SerializeType.queryByType(serializeType);
        if (serialize == null){
            throw new RuntimeException("serialize is null");
        }
        ISerializer serializer = serializerMap.get(serialize);
        if (serializer == null){
            throw new RuntimeException("serialize error");
        }
        try{
            return serializer.deserialize(data, clazz);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
