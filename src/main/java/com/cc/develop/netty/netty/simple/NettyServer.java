package com.cc.develop.netty.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * com.cc.develop.netty.netty.simple
 *
 * @author changchen
 * @email java@mail.com
 * @date 2020-11-07 07:49:33
 */
public class NettyServer {
    public static void main(String[] args) throws Exception{

        //创建BossGroup 和WorkGroup
        //说明
        //1创建两个线程组 bossGroup 和workGroup
        //2 bossGroup只是处理连接请求 真正处理业务的是workGroup
        //3两个都是无限循环
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        //创建服务器端启动对象 配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();
        //可以链式编程 设置两个线程组
        bootstrap.group(bossGroup,workGroup)
                //使用NioSctpServerChannel作为服务器通道实现
                .channel(NioServerSocketChannel.class)
                //设置线程队列等待连接个数
                .option(ChannelOption.SO_BACKLOG,128)
                //设置保持活动连接状态
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                //给我们的workGroup的EventLoop对应的管道设置处理器
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    //创建一个通道测试对象(匿名对象)
                    //给pipeline设置处理器
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(null);
                    }
                });

        System.out.println("===服务器 is ready ===");

        //绑定一个端口并且同步 生成一个ChannelFuture对象
        ChannelFuture channelFuture = bootstrap.bind(6668).sync();

        //对关闭通道进行监听
        channelFuture.channel().closeFuture().sync();


    }
}
