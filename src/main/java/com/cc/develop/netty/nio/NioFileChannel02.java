package com.cc.develop.netty.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author changchen
 * @create 2020-09-19 12:29
 */
public class NioFileChannel02 {
    public static void main(String[] args) throws Exception{

        //创建文件的输入流
        File file = new File("d:\\file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        //通过fileInputStream获取对应的channel
        FileChannel fileChannel = fileInputStream.getChannel();

        //创建缓冲区 使用字节缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        //将通道的数据读入到buffer
        fileChannel.read(byteBuffer);
        //将byteBuffer里的字节转成字符串
        System.out.println(new String(byteBuffer.array()));

        //关闭流
        fileInputStream.close();

    }
}
