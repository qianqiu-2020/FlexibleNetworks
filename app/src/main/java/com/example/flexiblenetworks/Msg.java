package com.example.flexiblenetworks;
/*该类为自定义协议内容，用于与服务器或其他客户端交换数据，当前主要包含三部分，消息类型，发送者id，消息内容*/
public class Msg extends Object {
    /*客户端点对点通信的消息类型*/
    public static final int TYPE_RECEIVERD=0;
    public static final int TYPE_SENT=1;
    /*与主服务器通信的消息类型*/
    public static final int TYPE_LOGIN=2;
    public static final int TYPE_KEEP_LOGIN=3;
    public static final int TYPE_QUIT=4;
    public static final int TYPE_SEND_BROADCAST=5;
    public static final int TYPE_RECEIVE_BROADCAST=6;
    public static final int TYPE_LOGIN_REGISTER=7;
    public static final int TYPE_ONLINE_LIST=8;
    public static final int TYPE_LBS=9;
    public static final int TYPE_GET_ONLINELIST=10;
    public static final int TYPE_TULING_OK=100;

    private int type;//消息类型
    private long sender_id;//发送者id，0为服务端，或者默认id
    private String content;//消息内容
    /*构造函数之构造消息*/
    public Msg(int type,long sender_id,String content){
        this.content=content;
        this.type=type;
        this.sender_id=sender_id;
    }
    /*构造函数之解析消息，将网络线程返回的消息（String类型）重新构造成Msg类型*/
    public Msg(String temp){
        String[] list=temp.split("\r\n");
        type=Integer.parseInt(list[0]);
        sender_id=Integer.parseInt(list[1]);
        content=list[2];
    }

    public String getContent(){
        return content;
    }
    public long getsender_id(){
        return sender_id;
    }
    public int getType(){
        return type;
    }

}
