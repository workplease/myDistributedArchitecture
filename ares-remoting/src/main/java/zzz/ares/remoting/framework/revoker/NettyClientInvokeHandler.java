package zzz.ares.remoting.framework.revoker;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import zzz.ares.remoting.framework.model.AresResponse;
import zzz.ares.remoting.framework.model.RevokerResponseHolder;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-11 19:41
 * @Version: 1.0
 *
 * 获取 Netty 异步调用返回的结果
 */
public class NettyClientInvokeHandler extends SimpleChannelInboundHandler<AresResponse> {

    public NettyClientInvokeHandler(){}

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AresResponse response) throws Exception {
        //将 Netty 异步返回的结果存入阻塞队列，以便调用端同步获取
        RevokerResponseHolder.putResultValue(response);
    }
}
