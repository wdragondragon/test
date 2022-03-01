package org.example.peer2peer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class UDPClientA {

    public static final String localHost = IPUtils.getIp();

    public static void main(String[] args) {
        try {
            String hostname = System.getProperty("server.ip", "127.0.0.1");
            String serverPort = System.getProperty("server.port", "2008");
            // 向server发起请求
            SocketAddress target = new InetSocketAddress(hostname, Integer.parseInt(serverPort));
            DatagramSocket client = new DatagramSocket();
            String message = "I am UPDClinetA " + localHost;
            byte[] sendbuf = message.getBytes();
            DatagramPacket pack = new DatagramPacket(sendbuf, sendbuf.length, target);
            client.send(pack);
            // 接收请求的回复,可能不是server回复的，有可能来自UPDClientB的请求内
            receive(client);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //接收请求内容
    private static void receive(DatagramSocket client) {
        try {
            for (; ; ) {
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                client.receive(packet);
                String receiveMessage = new String(packet.getData(), 0, packet.getLength());
                System.out.println(receiveMessage);
                int port = packet.getPort();
                InetAddress address = packet.getAddress();
                String reportMessage = "tks";
                //获取接收到请问内容后并取到地址与端口,然后用获取到地址与端口回复内容
                sendMessaage(reportMessage, port, address, client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //回复内容
    private static void sendMessaage(String reportMessage, int port, InetAddress address, DatagramSocket client) {
        try {
            byte[] sendBuf = reportMessage.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, address, port);
            client.send(sendPacket);
            System.out.println("消息发送成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
