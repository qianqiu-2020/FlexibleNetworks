package com.example.flexiblenetworks.define;

import java.text.SimpleDateFormat;
import java.util.Date;

/*该类为自定义协议内容，用于与服务器或其他客户端交换数据，当前主要包含三部分，消息类型，发送者id，消息内容*/
public class Msg extends Object {
    /*客户端点对点通信的消息类型*/
    public static final int TYPE_RECEIVERD = 0;
    public static final int TYPE_SENT = 1;
    /*与主服务器通信的消息类型*/
    public static final int TYPE_SERVER_STATUS = 1000;//查找服务器，试探服务器状态
    public static final int TYPE_LOGIN = 1100; //登录
    public static final int TYPE_REGISTER = 1200;//注册
    public static final int TYPE_FIND = 1300;//找回密码
    public static final int TYPE_VERIFY = 1400;//验证服务

    public static final int TYPE_ADDFRIEND = 2100;//添加好友
    public static final int TYPE_DELFRIEND = 2110;//删除好友
    public static final int TYPE_EGISTERLIST = 2120;//获取注册列表
    public static final int TYPE_ONLINELIST = 2130;//获取在线列表
    public static final int TYPE_TULING_OK = 2140;//接收到的图灵机器人消息
    public static final int TYPE_GROUPLIST = 2200;//获取群聊列表
    public static final int TYPE_INGROUPLIST = 2210;//获取群成员列表
    public static final int TYPE_CREGROUP = 2220;//创建群聊
    public static final int TYPE_JOINGROUP = 2230;//加入群聊
    public static final int TYPE_LEAGROUP = 2240;//离开群聊
    public static final int TYPR_DELGROUP = 2250;//删除群聊
    public static final int TYPE_QUITGROUP = 2260;//退出群聊
    public static final int TYPE_REPOSITORY = 2300;//获取知识库列表
    public static final int TYPE_REPLUMPLIST = 2310;//获取知识库各模块内容列表
    public static final int TYPE_CREREPOSITORY = 2320;//创建知识库
    public static final int TYPE_DELREPOSITORY = 2330;//删除知识库
    public static final int TYPE_BROADCAST = 2400;//广播
    public static final int TYPE_LOCATION = 2500;//位置请求

    public static final int TYPE_QUIT = 3100;//退出登录
    public static final int TYPE_LOGOUT = 3200;//注销账号

    String ip;//目的ip
    int port;//目的端口

    private int type;//消息类型
    private long sender_id;//发送者id，0为服务端，或者默认id
    private long receiver_id;//接收者id
    String time;//消息构造时间
    private String content;//消息内容

    /*构造函数之构造消息*/
    public Msg(String ip, int port, int type, long sender_id, long receiver_id) {
        this.ip = ip;
        this.port = port;
        this.type = type;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.content = "";
    }

    public Msg(String ip, int port, int type, long sender_id, long receiver_id, String content) {
        this.ip = ip;
        this.port = port;
        this.type = type;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.content = content;
    }

    public Msg(String ip, int port, int type, long sender_id, long receiver_id, String time, String content) {
        this.ip = ip;
        this.port = port;
        this.type = type;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.time = time;
        this.content = content;
    }

    /*构造函数之解析消息，将网络线程返回的消息（String类型）重新构造成Msg类型*/
    public Msg(String temp) {
        String[] list = temp.split("\r\n");
        type = Integer.parseInt(list[0]);
        sender_id = Integer.parseInt(list[1]);
        receiver_id = Integer.parseInt(list[2]);
        time = list[3];
        content = list[4];
    }

    public String getContent() {
        return content;
    }

    public long getsender_id() {
        return sender_id;
    }

    public int getType() {
        return type;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public long getReceiver_id() {
        return receiver_id;
    }

    public String getTime() {
        return time;
    }
}
