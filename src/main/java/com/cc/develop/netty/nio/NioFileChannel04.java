package com.cc.develop.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * @author changchen
 * @create 2020-09-19 13:10
 */
public class NioFileChannel04 {
    public static void main(String[] args) throws Exception{
        //创建文件流
        FileInputStream fileInputStream = new FileInputStream("d:\\f2.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\f4.jpg");

        //获取各自流流对应的channel
        FileChannel sourceCh = fileInputStream.getChannel();
        FileChannel destCh = fileOutputStream.getChannel();

        //使用transferForm完成拷贝
        destCh.transferFrom(sourceCh,0,sourceCh.size());
        //关闭相应的通道和流
        sourceCh.close();
        destCh.close();

        fileInputStream.close();
        fileOutputStream.close();

    }
}
