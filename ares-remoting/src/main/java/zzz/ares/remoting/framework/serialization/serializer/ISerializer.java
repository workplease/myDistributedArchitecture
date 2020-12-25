package zzz.ares.remoting.framework.serialization.serializer;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-11 15:08
 * @Version: 1.0
 *
 * 序列化/反序列化通用接口
 */
public interface ISerializer {

    /**
     * 序列化
     *
     * @param obj
     * @param <T>
     * @return
     */
    public <T> byte[] serialize(T obj);


    /**
     * 反序列化
     *
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T deserialize(byte[] data, Class<T> clazz);
}
