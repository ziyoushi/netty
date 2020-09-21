package com.cc.develop.netty.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author changchen
 * @create 2020-09-19 13:46
 * 说明：
 *      1、MappedByteBuffer 可以让文件直接在内存(堆外内存)修改 操作系统中不需要拷贝一次
 *      操作系统级别的修改 性能比较高
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception{

        RandomAccessFile rw = new RandomAccessFile("1.txt", "rw");
        //获取对应的通道
        FileChannel channel = rw.getChannel();

        /**
         * 参数1、FileChannel.MapMode.READ_WRITE 使用的读写模式
         * 参数2、0 可以直接修改的起始位置
         * 参数3 5 是映射到内存的大小(不是索引位置) 即将1.txt的多少个子节映射到内存
         * 实际类型是DirectByteBuffer
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0,(byte) 'H');
        mappedByteBuffer.put(3,(byte) '9');
        //IndexOutOfBoundsException 说明只能是内存大小 而不是索引位置
        //mappedByteBuffer.put(5,(byte) 'Y');

        rw.close();
        System.out.println("修改成功");

    }
}
