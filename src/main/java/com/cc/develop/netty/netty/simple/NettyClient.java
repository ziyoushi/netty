package com.cc.develop.netty.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * com.cc.develop.netty.netty.simple
 *
 * @author changchen
 * @email java@mail.com
 * @date 2020-11-08 14:37:52
 */
public class NettyClient {
    public static void main(String[] args) throws Exception{

        //客户端需要一个事件循环组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
        //创建客户端启动对象
        //ps 客户端使用的不是ServerBootstrap而是Bootstrap
        Bootstrap bootstrap = new Bootstrap();

        //设置相关参数
        bootstrap.group(group)
                //设置客户端通道实现类(反射)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //加入自己的处理器
                        ch.pipeline().addLast(null);
                    }
                });

        System.out.println("客户端 is ok");

        //启动客户端 去连接服务器端
        // ChannelFuture涉及到netty的异步模型
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();
        //给关闭通道进行监听
        channelFuture.channel().close().sync();

        }finally {
            group.shutdownGracefully();
        }

    }
}
