package com.cc.develop.netty.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author changchen
 * @create 2020-09-25 10:25
 */
public class GroupChatServer {

    // 定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    //构造器
    //初始化工作
    public GroupChatServer(){
        try {
            //得到选择器
            selector = Selector.open();
            //初始化ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            //将listenChannel 注册到selector上 如果有新的连接就注册上
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //监听
    public void listen(){

        try {
            //循环处理
            while (true){
                int count = selector.select();

                if (count >0){
                    //有事件要处理 下面需要遍历处理

                    //要遍历得到SelectionKey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        //取出selectionKey
                        SelectionKey key = iterator.next();
                        //判断key到底是什么事件 进行不同处理
                        //如果监听到accept事件
                        if (key.isAcceptable()){
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            //将socketChannel注册到selector
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            //提示
                            System.out.println(socketChannel.getRemoteAddress()+"上线了。。。。");
                        }
                        //读取事件 通道发送read事件 即通道可读的状态
                        if (key.isReadable()){
                            //从通道读数据到buffer 处理读(方法抽取)
                            this.readData(key);

                        }

                        //当前key移除 防止重复操作
                        iterator.remove();
                    }

                }else {
                    System.out.println("等待。。。。");
                }
            }

        }catch (Exception e){

        }finally {

        }

    }

    //读取客户端消息
    private void readData(SelectionKey key){
        //通过SelectionKey反向获取channel再读取数据

        //定义一个SocketChannel
        SocketChannel socketChannel = null;
        try {
            //得到socketChannel
            socketChannel = (SocketChannel)key.channel();
            //创建buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            int count = socketChannel.read(byteBuffer);
            //根据count的值做处理
            if (count >0){
                //把缓冲区的数据转成字符串
                String msg = new String(byteBuffer.array());
                System.out.println("from 客户端"+msg);
                //向其他客户端 转发消息(排除自己) 专门抽取方法来实现
                this.sendInfo2OtherClients(msg,socketChannel);
            }

        }catch (IOException e){
            try {
                System.out.println(socketChannel.getRemoteAddress()+"下线了。。。");
                //离线之后要取消key
                key.cancel();
                //关闭通道
                socketChannel.close();
            }catch (IOException e1){
                e1.printStackTrace();
            }

        }

    }

    //转发消息给其他客户(通道)
    private void sendInfo2OtherClients(String msg,SocketChannel self) throws IOException{
        System.out.println("服务器转发消息中。。。");
        //遍历所有注册到selector上的SocketChannel 并排除self
        for (SelectionKey key : selector.keys()) {
            //通过key 取出对应的SocketChannel
            Channel targetChannel = key.channel();
            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self){
                //转型
                SocketChannel dest = (SocketChannel)targetChannel;
                //将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer的数据写入到通道
                dest.write(buffer);
            }

        }

    }

    public static void main(String[] args) {

        //创建一个服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();

    }

}
