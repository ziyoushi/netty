package com.cc.develop.netty.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author changchen
 * @create 2020-09-25 13:36
 */
public class GroupChatClient {

    //定义相关的属性
    //服务器ip
    private final String HOST = "127.0.0.1";
    //服务器端口
    private final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;

    //构造器 完成初始化
    public GroupChatClient(){

        try {

            selector = Selector.open();
            //连接服务器
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST,PORT));
            //设置非阻塞
            socketChannel.configureBlocking(false);
            //将channel注册到selector
            socketChannel.register(selector, SelectionKey.OP_READ);
            //得到userName
            userName = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(userName + " is ok...");

        }catch (Exception e){

        }

    }

    //向服务器发送消息
    public void sendInfo(String info){
        info = userName + "说：" + info;

        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    //读取从服务器端回复的消息
    public void readInfo(){
        try {
            int readChannels = selector.select();
            if (readChannels >0){
                //有可用的通道
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if (key.isReadable()){
                        //得到相关的通道
                        SocketChannel sc = (SocketChannel)key.channel();
                        //得到一个buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        //从通道读取数据到buffer
                        sc.read(buffer);
                        //把缓冲区读取到的数据转成字符串
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }

                }
            }else {
                //System.out.println("没有可读的通道");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //启动我们客户端
        GroupChatClient chatClient = new GroupChatClient();
        //启动一个线程 每隔3秒 读取从服务端发送的数据
        new Thread(){

            @Override
            public void run(){
                while (true){
                    chatClient.readInfo();
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

        }.start();

        //客户端发送数据给服务端
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }

    }

}
