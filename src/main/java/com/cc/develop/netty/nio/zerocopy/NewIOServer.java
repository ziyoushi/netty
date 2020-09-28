package com.cc.develop.netty.nio.zerocopy;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author changchen
 * @create 2020-09-28 19:11
 */
public class NewIOServer {
    public static void main(String[] args) throws Exception{

        InetSocketAddress address = new InetSocketAddress(7001);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        ServerSocket socket = serverSocketChannel.socket();

        socket.bind(address);
        //创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        while (true){
            SocketChannel socketChannel = serverSocketChannel.accept();

            int readCount = 0;
            if (-1 != readCount){

                try {
                    readCount = socketChannel.read(byteBuffer);

                }catch (Exception ex){
                    ex.printStackTrace();
                    break;
                }
                //buffer倒带position=0 mark作废
                byteBuffer.rewind();

            }

        }

    }
}
