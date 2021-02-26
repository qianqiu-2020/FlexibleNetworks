package com.example.flexiblenetworks;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/*此活动为主界面，集成了各种不同的功能模块，也包含了公告栏，此活动登录后一直存在，不被销毁*/
public class MainActivity extends BaseActivity {
    private Button information;
    private Button friendchat;
    private Button location;
    private TextView broad;
    private String broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        information=(Button)findViewById(R.id.information);
        friendchat=(Button)findViewById(R.id.friendchat);
        location=(Button)findViewById(R.id.location);
        broad=(TextView)findViewById(R.id.text_view);
        /*主动向服务器发送获取公告信息*/
        Msg msg=new Msg(Msg.TYPE_SEND_BROADCAST,user_id,"broadcast");
        Log.d("msg","消息构造完成");
        netThread.setMsg(msg);
        netThread.setHandler(handler);
        new Thread(netThread).start();//网络子线程开始运行

        information.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,InformationActivity.class);//打开网页活动
                startActivity(intent);
                //finish();主活动一直存在不销毁
            }
        });
        friendchat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, FriendChatActivity.class);//打开聊天模块
                startActivity(intent);
                //finish();主活动一直存在不销毁
            }
        });
        location.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LBSActivity.class);//打开定位模块
                startActivity(intent);
                //finish();主活动一直存在不销毁
            }
        });
    }
    /*处理网络线程返回的信息，当前为处理公告栏内容*/
    @Override
    public void processMessage(Message msg) {
        String content=msg.getData().getString("content");
        Log.d("msgProssess_Main","msg.what（msgtype） "+msg.what+"\nmsg携带的bundle（msgcontent）内容如下\n"+content);
        switch (msg.what){
            case Msg.TYPE_RECEIVE_BROADCAST :{
                broad.setText("[公告栏]\n"+content);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*向服务器发送退出登录信息*/
        Msg msg=new Msg(Msg.TYPE_QUIT,user_id,"quit");
        Log.d("msgSend","发送退出登录消息");
        netThread.setMsg(msg);
        netThread.setHandler(handler);
        new Thread(netThread).start();//子线程开始运行
    }
}