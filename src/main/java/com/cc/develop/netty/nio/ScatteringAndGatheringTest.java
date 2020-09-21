package com.cc.develop.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author changchen
 * @create 2020-09-21 19:40
 * 说明：
 *  Scattering：将数据写入到buffer时 可以采用buffer数组 依次写入[分散]
 *  Gathering：从buffer读取数据时，可以采用buffer数组 依次读取
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception{

        // 使用ServerSocketChannel 和SocketChannel 网络
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到socket并启动
        serverSocketChannel.socket().bind(inetSocketAddress);
        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等待客户端连接(telnet)
        SocketChannel socketChannel = serverSocketChannel.accept();
        //假定从客户端接收8个子节
        int messageLength = 8;
        //循环读取
        while (true){
            int byteRead = 0;

            while (byteRead < messageLength){
                long read = socketChannel.read(byteBuffers);
                //累计读取的字节数
                byteRead += read;
                System.out.println("byteRead="+byteRead);
                //使用流打印 看看当前buffer的position和limit
                Arrays.asList(byteBuffers).stream().map(buffer ->
                        "position="+buffer.position()+"limit="+buffer.position()).forEach(System.out::println);
                //将所有的buffer进行flip反转
                Arrays.asList(byteBuffers).forEach(byteBuffer -> {byteBuffer.flip();});
                //将数据读出显示到客户端
                long byteWrite = 0;
                while (byteWrite < messageLength){
                    long write = socketChannel.write(byteBuffers);
                    byteWrite += write;
                }
                //将所有的buffer进行clear复位
                Arrays.asList(byteBuffers).forEach(byteBuffer -> {byteBuffer.clear();});

                System.out.println("byteRead="+byteRead+"byteWrite="+byteWrite+"messageLength="+messageLength);

            }

        }

    }
}
