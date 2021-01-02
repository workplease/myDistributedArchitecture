package zzz.ares.remoting.framework.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import zzz.ares.remoting.framework.serialization.common.SerializeType;
import zzz.ares.remoting.framework.serialization.engine.SerializerEngine;

import java.util.List;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-11 15:08
 * @Version: 1.0
 *
 * 解码器
 */
public class NettyDecoderHandler extends ByteToMessageDecoder {

    //解码对象 class
    private Class<?> genericClass;
    //解码对象编码所使用的序列化类型
    private SerializeType serializeType;

    public NettyDecoderHandler(Class<?> genericClass, SerializeType serializeType){
        this.genericClass = genericClass;
        this.serializeType = serializeType;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //获取消息头所识别的消息体字节数组长度
        if (byteBuf.readableBytes() < 4){
            return;
        }
        byteBuf.markReaderIndex();
        int dataLength = byteBuf.readInt();
        if (dataLength < 0){
            channelHandlerContext.close();
        }
        //若当前可以获取到的字节数小于实际长度，则直接返回，直到当前可以获取到的字节数等于实际长度
        if (byteBuf.readableBytes() < dataLength){
            byteBuf.resetReaderIndex();
            return;
        }
        //读取完整的消息体字节数组
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);

        //将字节数组反序列化为 Java 对象
        Object obj = SerializerEngine.deserialize(data,genericClass,serializeType.getSerializeType());
        list.add(obj);
    }
}
