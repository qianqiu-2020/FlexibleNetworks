package com.example.flexiblenetworks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
/*
此活动继承自AppCompatActivity
所有活动再继承自此抽象活动
在此活动中写一些所有活动都需要用到的功能，如网络线程
*/
public abstract class BaseActivity extends AppCompatActivity {
    protected static long user_id;//生存期为应用存在全程，记录客户端登录的账号id，在登录时从服务端获取
    protected static int mark=1;//客户端位置信息向服务端发送次数，在LBSactivity（定位活动）中被使用
    protected static List<String> online_list=new ArrayList<>();//客户端维护的在线列表，从主服务器获取
    /*每个活动创建时，加入到ActivityCollector中*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }
    /*每个活动销毁时，从ActivityCollector中删除*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    public abstract void processMessage(Message msg);//在handle中调用此方法，处理handle收到的数据，每个活动都有且需实现的方法
    protected static NetThread netThread=new NetThread();
    protected static NetThread_UDP netThread_udp=new NetThread_UDP();
    /*handle用于沟通子线程与主线程，在子线程中调用handler.sendMessage(message)后，会调用此处的handleMessage方法*/
    protected Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                default:
                    processMessage(msg);//处理消息的方式由子类定义，如何确定哪个活动处理？
            }
        }
    };

}
