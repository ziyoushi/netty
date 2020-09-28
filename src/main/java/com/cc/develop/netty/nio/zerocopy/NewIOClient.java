package com.cc.develop.netty.nio.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author changchen
 * @create 2020-09-28 19:12
 */
public class NewIOClient {
    public static void main(String[] args) throws Exception{

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1",7001));
        //需要复制的文件
        String fileName = "";
        //得到一个文件Channel
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
        //准备发送
        long startTime = System.currentTimeMillis();
        //在Linux下一个transferTo方法就可以完成传输
        //在Windows下一次调用transferTo只能发送8M，就需要分段传输文件
        //而且要注意传输时的位置
        //transferTo底层使用到零拷贝
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("发送的总的字节数"+transferCount+"耗时："+(System.currentTimeMillis()-startTime));
        //关闭
        fileChannel.close();

    }
}
