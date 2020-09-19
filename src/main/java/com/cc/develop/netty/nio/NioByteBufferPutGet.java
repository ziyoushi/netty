package com.cc.develop.netty.nio;

import java.nio.ByteBuffer;

/**
 * @author changchen
 * @create 2020-09-19 13:21
 */
public class NioByteBufferPutGet {
    public static void main(String[] args) {

        //创建一个Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        for (int i = 0;i<64;i++){
            byteBuffer.put((byte) i);
        }

        byteBuffer.flip();
        //按照类型化方式放入数据

        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();

        while (readOnlyBuffer.hasRemaining()){
            System.out.println(readOnlyBuffer.get());
        }

        readOnlyBuffer.put((byte) 88);

    }
}
