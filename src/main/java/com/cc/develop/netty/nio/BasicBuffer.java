package com.cc.develop.netty.nio;

import java.nio.IntBuffer;

/**
 * @author changchen
 * @create 2020-09-12 12:34
 */
public class BasicBuffer {
    public static void main(String[] args) {

        // 举例说明buffer的使用(简单说明)
        //创建buffer 设置大小为5 即可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);
        //添加数据到buffer
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i*2);
        }
        // buffer 读写切换
        intBuffer.flip();

        //读取buffer中的数据
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }

    }
}
