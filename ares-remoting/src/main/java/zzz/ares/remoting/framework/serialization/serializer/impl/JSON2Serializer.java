package zzz.ares.remoting.framework.serialization.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import zzz.ares.remoting.framework.serialization.serializer.ISerializer;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-24 19:42
 * @Version: 1.0
 *
 * 使用 fastjson 实现 JSON 序列化/反序列化
 */
public class JSON2Serializer implements ISerializer {

    @Override
    public <T> byte[] serialize(T obj) {
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        return JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat).getBytes();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(new String(data),clazz);
    }
}
