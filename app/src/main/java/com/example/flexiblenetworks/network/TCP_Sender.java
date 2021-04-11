package com.example.flexiblenetworks.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.flexiblenetworks.define.Msg;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

/*网络线程类。Socket默认是使用了TCP三次握手建立连接*/
//Socket 是对 TCP/IP 协议的封装，Socket 只是个接口不是协议，通过 Socket 我们才能使用 TCP/IP 协议
public class TCP_Sender implements Runnable {
    public boolean onWork = true;    //线程工作标识
    static Handler handler;
    //BlockingQueue<Msg> queue = new BlockingQueue<Msg>();//发送队列
    Queue<Msg> queue = new LinkedList<Msg>();//发送队列
    //注意，需要先添加消息到队列中再打断线程睡眠状态，不要打断睡眠后再添加消息到队列，可能会引发queue
    //不安全，queue需要互斥访问！
    public void putMsg(Msg msg) {
        queue.offer(msg);
    }

    public static void setHandler(Handler handler1) {
        handler = handler1;
    }

    public void sendTcpData(Msg msg) {
        try {
            Log.d("tcp", "输出流\n" + "目的IP" + msg.getIp() + " 端口" + String.valueOf(msg.getPort()) + "\n发送id " + String.valueOf(msg.getsender_id()) +  "\n接收id " + String.valueOf(msg.getReceiver_id()) + "\n发送时间 " + msg.getTime()+"\n消息类型 " + String.valueOf(msg.getType()) + "\ncontent " + msg.getContent());
            /*TCP建立连接*/
            Socket client;//客户端，与服务端套接字不同
            client = new Socket();
            client.connect(new InetSocketAddress(msg.getIp(), msg.getPort()), 3000);//用connect，可以设置超时时间，提醒用户
            /*TCP发送消息*/
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            String temp = msg.getType() + "\r\n" + msg.getsender_id() +"\r\n" +msg.getReceiver_id()+"\r\n" +msg.getTime()+ "\r\n" + msg.getContent();//将自定义协议封装为String类型用于传输，接收方可以再解析并重新构造出Msg类型
            out.writeUTF(temp);//发送信息
            /*TCP接收消息*/
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            String result = in.readUTF();//此处会阻塞，直到收到数据，接收验证结果
            Msg reply = new Msg(result);//从String类型解析出Msg类型
            Log.d("tcp", "返回流\n来自" + client.getRemoteSocketAddress() + "\n发送者id " + String.valueOf(reply.getsender_id()) +"\n接收id " + String.valueOf(reply.getReceiver_id()) + "\n发送时间 " + reply.getTime()+ "\n消息类型 " + String.valueOf(reply.getType()) + "\ncontent " + reply.getContent());
            client.close();//断开TCP连接
            /*将信息通过handler从当前子线程发送给主线程*/
            Message message = new Message();
            message.what = Integer.valueOf(reply.getType());//what字段带有消息类型
            message.arg1 = (int) reply.getsender_id();//arg1字段带有发送者id
            Bundle data = new Bundle();//携带较多数据时的方法，带有消息正文
            data.putString("content", reply.getContent());
            message.setData(data);
            handler.sendMessage(message);//发往主线程，会调用本类设置的Handle的handleMessage方法，注意，handle不是baseactivity中的handle，而是重新它的拷贝
        } catch (ConnectException e)//连接超时，可能是服务器未开启
        {
            /*将信息通过handle从当前子线程发送给主线程*/
            Message message = new Message();
            message.what = 1000;
            handler.sendMessage(message);//发往主线程，会调用本类设置的Handle的handleMessage方法，注意，handle不是baseactivity中的handle，而是重新它的拷贝

            //Connection reset异常
            /*一端退出，但退出时并未关闭该连接，另一端如果在从连接中读数据则抛出该异常（Connection reset）。简单的说就是在连接断开后的读和写操作引起的。*/
            e.printStackTrace();
        } catch (SocketTimeoutException e)//连接超时，可能是服务器未及时回应
        {
            /*将信息通过handle从当前子线程发送给主线程*/
            Message message = new Message();
            message.what = 1001;
            handler.sendMessage(message);//发往主线程，会调用本类设置的Handle的handleMessage方法，注意，handle不是baseactivity中的handle，而是重新它的拷贝

            //Connection reset异常
            /*一端退出，但退出时并未关闭该连接，另一端如果在从连接中读数据则抛出该异常（Connection reset）。简单的说就是在连接断开后的读和写操作引起的。*/
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*线程运行时调用的方法*/
    @Override
    public void run() {
        while (onWork) {
          //  try {
            while (!queue.isEmpty()) {
                sendTcpData(queue.poll());
            }
            Log.d("test",String.valueOf(queue.isEmpty()));
//                //Thread.sleep(10);//为空时休眠，等待唤醒,节省资源
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                continue;
//            }
        }
    }
}
