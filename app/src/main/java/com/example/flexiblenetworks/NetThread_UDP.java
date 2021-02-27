package com.example.flexiblenetworks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public class NetThread_UDP implements Runnable {
    private boolean onWork = true;    //线程工作标识

    static Msg msg;
    String mainServerIp = "119.45.115.128";
    int port = 8520;
    static Handler handler;
    String aimip;
    int aimport;//啦啦啦啦

    public void setMsg(Msg msg1) {
        msg = msg1;
    }

    public static void setHandler(Handler handler1) {
        handler = handler1;
    }

    public void setip(Msg msg1) {
        msg = msg1;
    }

    /*发送UDP数据报，线程运行时也可调用NetThread_UDP中的其他方法*/
    //udp传输：
    /**步骤：---
     * 1、建立udp socket 接收和发送点
     * 2、提供数据，打包
     * 3、使用send发送
     * 4、关闭资源
     * */
    public void sendUdpData(Msg msg,String ip,int port) {
       try{
        /*UDP发送消息*/
        // 创建发送端Socket, 绑定本机IP地址, 绑定任意一个未使用的端口号
        DatagramSocket socket = new DatagramSocket();
        // 创建发送端Packet, 指定数据, 长度, 地址, 端口号
        String temp = msg.getType() + "\r\n" + msg.getsender_id() + "\r\n" + msg.getContent();
        DatagramPacket packet = new DatagramPacket(temp.getBytes("UTF-8"), temp.getBytes().length, InetAddress.getByName(ip), port);
        // 使用Socket发送Packet
        socket.send(packet);
        // 关闭Socket
        socket.close();
        Log.d("mark", "发送UDP流为" + temp);
       } catch (SocketException | UnsupportedEncodingException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    /*线程运行时调用的方法，一直监听一个UDP端口，当前似乎无法收到服务端发来的数据*/
    //udp传输： 接收
    /**步骤：---
     * 1、建立udp socket，设置接收端口
     * 2、预先创建数据存放的位置，封装
     * 3、使用receive阻塞式接收
     * 4、关闭资源
     * */
    @Override
    public void run() {
        try {
            // 创建接收端Socket, 绑定本机IP地址, 绑定指定端口
            DatagramSocket socket = new DatagramSocket(port);
            Log.d("mark", socket.getLocalSocketAddress()+"监听UDP端口"+port+"ing...");
            // 创建接收端Packet, 用来接收数据
            //sendUdpData( socket.getLocalPort(),);

            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            while (onWork) {
                Log.d("mark", socket.getInetAddress()+"监听UDP端口"+port+"ing...");
                /*UDP接收*/
                // 用Socket接收Packet, 未收到数据时会阻塞
                socket.receive(packet);
                Log.d("mark", socket.getInetAddress()+"监听UDP端口"+port+"ing...");
                // 从Packet中获取数据
                byte[] temp = packet.getData();
                int len = packet.getLength();
                String result = new String(temp, 0, len, "UTF-8");

                /*消息发往主线程*/
                Msg reply = new Msg(result);
                Log.d("mark", "已收到UDP消息" + result);
                Message message = new Message();
                message.what = Integer.valueOf(reply.getType());
                Bundle data = new Bundle();
                data.putString("content", reply.getContent());
                message.setData(data);
                handler.sendMessage(message);
            }
            // 关闭Socket
            socket.close();
        } catch (SocketException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
