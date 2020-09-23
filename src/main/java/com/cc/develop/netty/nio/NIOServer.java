package com.cc.develop.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author changchen
 * @create 2020-09-22 21:46
 */
public class NIOServer {
    public static void main(String[] args) throws Exception{

        //创建ServerSocketChannel -->ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个Selector对象
        Selector selector = Selector.open();
        //绑定一个端口6666在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //把 serverSocketChannel 注册到selector 关心 事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true){

            if (selector.select(1000) == 0){
                //没有事件发生
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }

            //如果返回的>0 就获取相应的 selectionKey集合
            //1、如果返回的>0 表示已经获取到关注的事件
            //2、selector.selectedKeys() 返回关注事件的集合
            //   通过selectedKeys 反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //使用迭代器遍历 selectionKeys
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()){
                //获取到SelectionKey
                SelectionKey key = keyIterator.next();
                //根据key 对应的通道发生的事件做相应处理
                if (key.isAcceptable()){
                    //如果是OP_ACCEPT 有新的客户端连接
                    //给该客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //将socketChannel 注册到selector 关注事件为 OP_READ 同时给该socketChannel关联一个buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if (key.isReadable()){
                    //发生OP_READ
                    //通过key 反向获取对应的channel
                    SocketChannel channel = (SocketChannel)key.channel();
                    //获取该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    //将channel的数据读到buffer
                    channel.read(buffer);
                    System.out.println("from 客户端"+ new String(buffer.array()));

                }
                //手动从集合中移除当前SelectionKey 防止重复操作
                keyIterator.remove();

            }

        }

    }
}
