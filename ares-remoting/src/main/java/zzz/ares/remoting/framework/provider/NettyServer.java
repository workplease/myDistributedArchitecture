package zzz.ares.remoting.framework.provider;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import zzz.ares.remoting.framework.helper.PropertyConfigHelper;
import zzz.ares.remoting.framework.model.AresRequest;
import zzz.ares.remoting.framework.revoker.NettyServerInvokeHandler;
import zzz.ares.remoting.framework.serialization.NettyDecoderHandler;
import zzz.ares.remoting.framework.serialization.NettyEncoderHandler;
import zzz.ares.remoting.framework.serialization.common.SerializeType;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-05 22:35
 * @Version: 1.0
 */
public class NettyServer {

    private static NettyServer nettyServer = new NettyServer();

    //服务端 boss 线程组
    private EventLoopGroup bossGroup;
    //服务端 worker 线程组
    private EventLoopGroup workerGroup;
    //序列化类型配置信息
    private SerializeType serializeType = PropertyConfigHelper.getSerializeType();

    /**
     * 启动 Netty 服务
     * @param port
     */
    public void start(final int port){
        synchronized (NettyServer.class){
            if (bossGroup != null || workerGroup != null){
                return;
            }

            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //注册解码器 NettyDecoderHandler
                            socketChannel.pipeline().addLast(new NettyDecoderHandler(AresRequest.class,serializeType));
                            //注册编码器 NettyEncoderHandler
                            socketChannel.pipeline().addLast(new NettyEncoderHandler(serializeType));
                            //注册服务端业务逻辑处理器 NettyServerInvokeHandler
                            socketChannel.pipeline().addLast(new NettyServerInvokeHandler());
                        }
                    });
            try{
                serverBootstrap.bind(port).sync().channel();
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }

    private NettyServer(){}

    public static NettyServer singleton(){
        return nettyServer;
    }
}
