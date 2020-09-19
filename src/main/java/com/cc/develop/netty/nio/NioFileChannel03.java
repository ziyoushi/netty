package com.cc.develop.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author changchen
 * @create 2020-09-19 12:43
 */
public class NioFileChannel03 {
    public static void main(String[] args) throws Exception{

        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel fileChannel01 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        //循环读取
        while (true){
            //这里需要复位 清空byteBuffer
            byteBuffer.clear();
            int read = fileChannel01.read(byteBuffer);
            if (read == -1){
                //说明已经读完
                break;
            }
            //将byteBuffer中的数据写入到fileChannel02 -->2.txt
            byteBuffer.flip();//注意反转
            fileChannel02.write(byteBuffer);

        }
        //关闭流
        fileInputStream.close();
        fileOutputStream.close();

    }
}
