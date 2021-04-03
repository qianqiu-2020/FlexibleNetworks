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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/*网络线程类。Socket默认是使用了TCP三次握手建立连接*/
//Socket 是对 TCP/IP 协议的封装，Socket 只是个接口不是协议，通过 Socket 我们才能使用 TCP/IP 协议
public class NetThread implements Runnable{
    static Msg msg;
    String mainServerIp="192.168.12.1";//主服务器ip
    int port=12000;//主服务器端口号
    static Handler handler;
    public void setMsg(Msg msg1){
        msg=msg1;
    }
    public static void setHandler(Handler handler1){
        handler=handler1;
    }

    /*线程运行时调用的方法*/
    @Override
    public void run() {
        try
        {
            Log.d("mark","进入TCP网络子线程");
            /*TCP登录验证*/
            Socket client;//客户端，与服务端套接字不同
            client = new Socket();
            //用connect，可以设置超时时间，提醒用户
            client.connect(new InetSocketAddress(mainServerIp, port),3000);
            Log.d("mark",client.getRemoteSocketAddress().toString());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            String temp=msg.getType()+"\r\n"+msg.getsender_id()+"\r\n"+msg.getContent();//将自定义协议封装为String类型用于传输，接收方可以再解析并重新构造出Msg类型
            out.writeUTF(temp);//发送信息
            Log.d("msgSend","发送流内容为\n"+temp);
            //此处会阻塞？直到收到数据

            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            String result=in.readUTF();//接收验证结果


            Msg reply=new Msg(result);//从String类型解析出Msg类型
            Log.d("msgReceive","返回流,来自"+client.getInetAddress()+"端口"+client.getPort()+"，内容如下\n"+result);
            client.close();//断开TCP连接

            /*将信息通过handle从当前子线程发送给主线程*/
            Message message=new Message();
            message.what=Integer.valueOf(reply.getType());
            Bundle data=new Bundle();//携带较多数据时的方法
            data.putString("content",reply.getContent());
            message.setData(data);
            handler.sendMessage(message);//发往主线程，会调用本类设置的Handle的handleMessage方法，注意，handle不是baseactivity中的handle，而是重新它的拷贝
        }catch(SocketTimeoutException e)
        {
            /*将信息通过handle从当前子线程发送给主线程*/
            Message message=new Message();
            message.what=1000;
            handler.sendMessage(message);//发往主线程，会调用本类设置的Handle的handleMessage方法，注意，handle不是baseactivity中的handle，而是重新它的拷贝
            //Connection reset
            /*一端退出，但退出时并未关闭该连接，另一端如果在从连接中读数据则抛出该异常（Connection reset）。简单的说就是在连接断开后的读和写操作引起的。*/
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
