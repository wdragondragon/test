package org.example.io.nio;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.12 23:00
 * @Description:
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Client2 implements Runnable {

    private int flag = 1;
    //1 多路复用器（管理所有的通道）
    private Selector selector;
    //2 建立读缓冲区
    private ByteBuffer readBuf = ByteBuffer.allocate(1024);
    //3 建立写缓冲区
    private ByteBuffer writeBuf = ByteBuffer.allocate(1024);

    //创建连接的地址：ip+端口
    InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8765);

    SocketChannel client;
    public Client2() {
        try {
            //1 打开多路复用器
            this.selector = Selector.open();
            //2 打开服务器通道
            client = SocketChannel.open();
            //3 设置服务器通道为非阻塞模式
            client.configureBlocking(false);
            //5 把服务器通道注册到多路复用器上，并且监听阻塞事件
            client.register(this.selector, SelectionKey.OP_CONNECT);
            client.connect(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                //socket被关闭则退出
                if(!client.isOpen())
                    break;
                //1 必须要让多路复用器开始监听
                this.selector.select();
                //2 返回多路复用器已经选择的结果集
                Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
                //3 进行遍历
                while (keys.hasNext()) {
                    //4 获取一个选择的元素
                    SelectionKey key = keys.next();
                    //5 直接从容器中移除就可以了
                    keys.remove();
                    //6 如果是有效的
                    if (!key.isValid())
                        continue;
                    //7 如果为阻塞状态
                    if (key.isConnectable()) {
                        System.out.println("client connect");
                        this.connect(key);
                    }
                    //8 如果为可读状态
                    if (key.isReadable()) {
                        this.read(key);
                    }
                    //9 写数据
                    if (key.isWritable()) {
                        this.write(key);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void write(SelectionKey key) throws IOException {
        //客户端发送数据给服务端
        this.writeBuf.clear();
        SocketChannel client = (SocketChannel) key.channel();
        String sendText = "你好 Server:" + flag++;
        writeBuf.put(sendText.getBytes());
        writeBuf.flip();
        client.write(writeBuf);
        System.out.println("客户端发送数据给服务端：" + sendText);
        if(flag==100){
            System.out.println("取消写事件");
//            client.register(selector, SelectionKey.OP_READ);
            key.interestOps(key.interestOps()&~SelectionKey.OP_WRITE);
            //合法关闭
            key.cancel();
            client.close();
        }
    }

    private void read(SelectionKey key) throws IOException {
        //1 清空缓冲区旧的数据
        this.readBuf.clear();

        SocketChannel client = (SocketChannel) key.channel();

        try{
            int count = client.read(readBuf);
            if (count > 0) {
                String receiveTest = new String(readBuf.array(), 0, count);
                System.out.println("客户端接收到服务端的数据:" + receiveTest);
//                client.register(selector, SelectionKey.OP_WRITE);
                key.interestOps(SelectionKey.OP_WRITE);
            }
        }catch (IOException e){
            System.out.println("服务器异常关闭");
            //合法关闭
            key.cancel();
            client.close();
        }
    }

    private void connect(SelectionKey key) {
        try {
            //1 获取服务通道
            SocketChannel client = (SocketChannel) key.channel();
            if (client.isConnectionPending()) {
                while (!client.finishConnect()) {
                    System.out.println("连接中");
                }
                System.out.println("客户端完成连接操作！");
                writeBuf.clear();
                writeBuf.put("Hello,Server".getBytes());
                writeBuf.flip();
                client.write(writeBuf);
            }
            //4 注册到多路复用器上，并设置读取标识
//            client.register(selector, SelectionKey.OP_READ);
            key.interestOps(SelectionKey.OP_READ);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(1<<0);
        System.out.println(1<<1);
        System.out.println(1<<2);
        System.out.println(1<<3);
        new Thread(new Client2()).start();
    }
}
