package com.cc.develop.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author changchen
 * @create 2020-09-23 19:20
 */
public class NIOClient {
    public static void main(String[] args) throws Exception{

        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞模式
        socketChannel.configureBlocking(false);
        //提供服务器端的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        //连接服务端
        if (!socketChannel.connect(inetSocketAddress)){
            //如果客户端没有连上服务端 不用一直阻塞在这里 也可以做其他工作
            while (!socketChannel.finishConnect()){
                System.out.println("因为连接需要时间,客户端不会阻塞，可以做其他工作");
            }
        }
        //连接成功 发送数据
        String str = "hello,world netty";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        //发送数据 将buffer中的数据写入到channel
        socketChannel.write(buffer);
        System.in.read();

    }
}
