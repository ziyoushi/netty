package com.cc.develop.netty.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * com.cc.develop.netty.netty.simple
 *
 * @author changchen
 * @email java@mail.com
 * @date 2020-11-07 08:19:16
 */
/**
 * 说明：
 * 1、我们自定义一个Handler 需要继承netty规定好的某个HandlerAdapter
 * 2、这时我们自定义的一个Handler,才能称为一个Handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //读取数据的事件 可以读取客户端发送的消息

    /**
     * 说明
     * ChannelHandlerContext 上下文对象 含有管道pipeline 和通道 channel 地址
     * Object 客户端发送的数据 默认object
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("server ctx=="+ctx);
        // 将msg转成byteBuf
        /**
         * 说明：
         * ByteBuf 是netty提供的 和ByteBuffer 是NIO提供的
         *
         */
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端发送消息是："+byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址："+ctx.channel().remoteAddress());

    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        //writeAndFlush = write + flush
        //将数据写入到缓存 并刷新
        //对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端",CharsetUtil.UTF_8));
    }

    /**
     * 处理异常 一般是需要关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        ctx.close();
    }
}
