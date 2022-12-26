package org.example.container;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class ChannelSocketTest {

    public void start() throws IOException {

        // 1.新建NIO通道
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false); // 设置为非阻塞状态
        ServerSocket socket = ssc.socket();
        System.out.println("启动web服务");
        socket.bind(new InetSocketAddress(8888));
        while (true) {
            SocketChannel channel = ssc.accept();
            if(Objects.nonNull(channel)){
                Thread thread = new Thread(new HttpServerThread(channel));
                thread.start();
            }

        }
    }


    // 内部类
    private class HttpServerThread implements Runnable {
        SocketChannel channel;

        HttpServerThread(SocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void run() {
            if (channel != null) {

                try {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    InetSocketAddress remoteAddress = (InetSocketAddress) channel.getRemoteAddress();
//                    System.out.println(remoteAddress.getAddress());
//                    System.out.println(remoteAddress.getPort());
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    while (byteBuffer.hasRemaining()) {
                        char c = (char) byteBuffer.get();
                        System.out.print(c);
                    }
                    // 此处打印执行的线程名称，永远为 main 线程
                    System.out.println(Thread.currentThread().getName() + "开始向web返回消息。。。");
                    ByteBuffer byteBuffer2 = ByteBuffer.allocate(1024);
                    // 给客户端一个响应，即向输出流写入信息
                    String reply = "HTTP/1.1 "; // 必须添加的响应头
                    reply += "Content-type:text/html "; // 必须添加的响应头
                    reply += "服务器返回的消息";
                    byteBuffer2.put(new String(reply).getBytes());
                    byteBuffer2.flip();
                    channel.write(byteBuffer2);
                    channel.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        new ChannelSocketTest().start();
    }
}
