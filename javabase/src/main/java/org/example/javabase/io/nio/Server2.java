package org.example.javabase.io.nio;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.12 22:59
 * @Description:
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server2 implements Runnable {

    private int flag = 1;
    //1 多路复用器（管理所有的通道）
    private Selector selector;
    //2 建立读缓冲区
    private ByteBuffer readBuf = ByteBuffer.allocate(1024);
    //3 建立写缓冲区
    private ByteBuffer writeBuf = ByteBuffer.allocate(1024);

    public Server2(int port) {
        try {
            //1 打开多路复用器
            this.selector = Selector.open();
            //2 打开服务器通道
            ServerSocketChannel server = ServerSocketChannel.open();
            //3 设置服务器通道为非阻塞模式
            server.configureBlocking(false);
            //4 绑定地址
            server.bind(new InetSocketAddress(port));
            //5 把服务器通道注册到多路复用器上，并且监听阻塞事件
            server.register(this.selector, SelectionKey.OP_ACCEPT);

            System.out.println("Server start, port :" + port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                //1 必须要让多路复用器开始监听
                this.selector.select();
                //2 返回多路复用器已经选择的结果集
                Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
                int keyNum = 0;
                //3 进行遍历
                while (keys.hasNext()) {
                    keyNum++;
                    //4 获取一个选择的元素
                    SelectionKey key = keys.next();
                    //5 直接从容器中移除就可以了
                    keys.remove();
                    //6 如果是有效的
                    if (key.isValid()) {
                        //7 如果为阻塞状态
                        if (key.isAcceptable()) {
                            this.accept(key);
                        }
                        //写数据
                        if (key.isWritable()) {
                            this.write(key);
                        }
                        //如果为可读状态
                        if (key.isReadable()) {
                            this.read(key);
                        }
                    }
                }
                System.out.println(keyNum);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void write(SelectionKey key) throws IOException {
        //server.register(this.selector, SelectionKey.OP_WRITE);
        //服务端发送数据给客户端
        this.writeBuf.clear();
        SocketChannel client = (SocketChannel) key.channel();
        String sendText = "你好 Client:" + flag++;
        writeBuf.put(sendText.getBytes());
        writeBuf.flip();
        client.write(writeBuf);
        System.out.println("服务端发送数据给客户端：" + sendText);
//        client.register(this.selector, SelectionKey.OP_READ);
        key.interestOps(SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException {

        //1 清空缓冲区旧的数据
        this.readBuf.clear();
        //2 获取之前注册的socket通道对象
        SocketChannel client = (SocketChannel) key.channel();
        //3 读取数据,对readBuf而言是写模式
        int count = client.read(this.readBuf);
        //4 如果没有数据,大于0 有数据，等于0没有数据，等于-1对方已关闭
        if (count == -1) {
            key.channel().close();
            key.cancel();
            return;
        }
        //5 有数据则进行读取 读取之前需要进行复位方法(把position 和limit进行复位)
        //对readBuf来说是切换到读模式
        this.readBuf.flip();
        String body =new String(readBuf.array(),0,count);
        System.out.println("Server : " + body);

        // 9..可以写回给客户端数据
//        client.register(this.selector, SelectionKey.OP_READ);
        key.interestOps(SelectionKey.OP_READ);

    }

    private void accept(SelectionKey key) {
        try {
            //1 获取服务通道
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            //2 执行阻塞方法
            SocketChannel client = server.accept();
            //3 设置阻塞模式
            client.configureBlocking(false);
            //4 注册到多路复用器上，并设置读取标识
            client.register(this.selector, SelectionKey.OP_WRITE);
//            key.interestOps(SelectionKey.OP_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new Thread(new Server2(8765)).start();
    }
}