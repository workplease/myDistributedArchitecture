package zzz.ares.remoting.framework.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import zzz.ares.remoting.framework.serialization.common.SerializeType;
import zzz.ares.remoting.framework.serialization.engine.SerializerEngine;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-11 15:16
 * @Version: 1.0
 */
public class NettyEncoderHandler extends MessageToByteEncoder {

    //序列化类型
    private SerializeType serializeType;

    public NettyEncoderHandler(SerializeType serializeType){
        this.serializeType = serializeType;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object in, ByteBuf out) throws Exception {
        //将对象序列化为字节数组
        byte[] data = SerializerEngine.serialize(in,serializeType.getSerializeType());
        //将字节数组（消息体）的长度作为小西天写入，解决半包，粘包问题
        out.writeInt(data.length);
        //写入序列哈后得到的字节数组
        out.writeBytes(data);
    }
}
