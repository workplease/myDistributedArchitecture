package zzz.ares.remoting.framework.serialization.common;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.text.translate.UnicodeUnescaper;


/**
 * @Author: milkteazzz
 * @Data: 2020-12-06 11:22
 * @Version: 1.0
 *
 * 序列化类型枚举
 */
public enum SerializeType {
    DefaultJavaSerializer("DefaultJavaSerializer"),
    HessianSerializer("HessianSerializer"),
    JSONSerializer("JSONSerializer"),
    ProtoStuffSerializer("ProtoStuffSerializer"),
    XmlSerializer("XmlSerializer"),
    MarshallingSerializer("MarshallingSerializer"),
    AvroSerializer("AvroSerializer"),
    ProtocolBufferSerializer("ProtocolBufferSerializer"),
    ThriftSerializer("ThriftSerializer");

    private String SerializeType;

    private SerializeType(String serializeType){
        this.SerializeType = serializeType;
    }

    public static SerializeType queryByType(String serializeType){
        if (StringUtils.isBlank(serializeType)){
            return null;
        }
        for (zzz.ares.remoting.framework.serialization.common.SerializeType serialize :
                zzz.ares.remoting.framework.serialization.common.SerializeType.values()){
            if (StringUtils.equals(serializeType,serialize.getSerializeType())){
                return serialize;
            }
        }
        return null;
    }

    public String getSerializeType(){
        return SerializeType;
    }
}
