package com.cc.develop.netty.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author changchen
 * @create 2020-09-15 19:10
 */
public class NioFileChannel01 {
    public static void main(String[] args) throws Exception{

        String str = "hello,world!! hello 中国";
        //创建一个输出流->Channel
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\file01.txt");
        //通过fileOutputStream 获取对应文件FileChannel
        FileChannel fileChannel = fileOutputStream.getChannel();
        //创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //将str放入到byteBuffer中
        byteBuffer.put(str.getBytes());
        //对byteBuffer反转 读取数据
        byteBuffer.flip();
        //将byteBuffer 数据写入到fileChannel
        fileChannel.write(byteBuffer);
        //关闭流
        fileOutputStream.close();

    }
}
