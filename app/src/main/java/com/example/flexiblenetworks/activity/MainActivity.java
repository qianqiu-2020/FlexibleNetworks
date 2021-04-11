package com.example.flexiblenetworks.activity;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.flexiblenetworks.adapter.FriendAdapter;
import com.example.flexiblenetworks.adapter.MsgAdapter;
import com.example.flexiblenetworks.adapter.messageAdapter;
import com.example.flexiblenetworks.define.Friend;
import com.example.flexiblenetworks.define.Msg;
import com.example.flexiblenetworks.R;
import com.example.flexiblenetworks.define.messageItem;
import com.example.flexiblenetworks.network.TCP_Sender;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


/*此活动为主界面，集成了各种不同的功能模块，也包含了公告栏，此活动登录后一直存在，不被销毁*/
public class MainActivity extends BaseActivity {
    private Button information;
    private Button friendchat;
    private Button location;
    private Button update;
    private ImageView exit;
    private Button market;
    private TextView broad;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView imageView;
    private ImageView main_interface;
    private ImageView message_interface;
    private ListView message_list;
    private ImageView web;
    private ImageView setting;
    private messageAdapter adapter;//主界面消息的适配器
    private List<messageItem> MsgList = new ArrayList<>();

    public static class MyCallback implements ComponentCallbacks {
        @Override
        public void onConfigurationChanged(Configuration arg) {
        }

        @Override
        public void onLowMemory() {
//do release operation
            Log.d("销毁1", "main");

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        /*集中创建网络线程*/
        tcp_sender.setHandler(handler);
        udp_sender.setHandler(handler);
        udp_listener.setHandler(handler);
        udp_sender_tread = new Thread(udp_sender);//发送udp消息
        udp_listener_tread = new Thread(udp_listener);//接收udp消息
//
        udp_sender_tread.start();
        udp_listener_tread.start();

        information = (Button) findViewById(R.id.information);
        friendchat = (Button) findViewById(R.id.friendchat);
        location = (Button) findViewById(R.id.location);
        broad = (TextView) findViewById(R.id.text_view);
        update = (Button) findViewById(R.id.update);
        market = (Button) findViewById(R.id.market);
        exit = (ImageView) findViewById(R.id.exit);
        imageView = (ImageView) findViewById(R.id.imageview);

        main_interface = (ImageView) findViewById(R.id.main_interface);
        message_interface = (ImageView) findViewById(R.id.message_interface);
        web = (ImageView) findViewById(R.id.web);
        message_list = (ListView) findViewById(R.id.message_list);
        message_list.setVisibility(View.INVISIBLE);

        setSupportActionBar(findViewById(R.id.toolbar));
        setting = (ImageView) findViewById(R.id.setting);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navigationView.setCheckedItem(R.id.nav_call);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //可以在这里设置逻辑，这里只是用nav_call做一个示范
                drawerLayout.closeDrawers();
                return true;
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "设置", Toast.LENGTH_SHORT).show();
            }
        });
        market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MarketActivity.class);
                startActivity(intent);
            }
        });

        /*主动向服务器发送获取公告信息*/
        Msg msg = new Msg(mainserverIp,mainserverPort,Msg.TYPE_BROADCAST, user_id, mainserverId, "broadcast");
        Log.d("msg", "消息构造完成");
        tcp_sender.putMsg(msg);
        //tcp_sender_tread.interrupt();

//        netThread.setMsg(msg);
//        netThread.setHandler(handler);
//        new Thread(netThread).start();//网络子线程开始运行

        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InformationActivity.class);//打开网页活动
                startActivity(intent);
                //finish();主活动一直存在不销毁
            }
        });
        friendchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FriendChatActivity.class);//打开聊天模块
                startActivity(intent);
                //finish();主活动一直存在不销毁
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LBSActivity.class);//打开聊天模块
                startActivity(intent);
                //finish();主活动一直存在不销毁
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DownLoadActivity.class);//打开下载模块
                startActivity(intent);
                //finish();主活动一直存在不销毁
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//退出程序
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.cug.edu.cn/"));
                startActivity(intent);//打开官网
            }
        });

        main_interface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message_list.setVisibility(View.INVISIBLE);
                location.setVisibility(View.VISIBLE);

                information.setVisibility(View.VISIBLE);
                friendchat.setVisibility(View.VISIBLE);
                location.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                exit.setVisibility(View.VISIBLE);
                market.setVisibility(View.VISIBLE);
                broad.setVisibility(View.VISIBLE);
                web.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
                Log.d("ssss", "1111111");
            }
        });
        message_interface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("ssss", "111111222221");
                information.setVisibility(View.INVISIBLE);
                friendchat.setVisibility(View.INVISIBLE);
                location.setVisibility(View.INVISIBLE);
                update.setVisibility(View.INVISIBLE);
                exit.setVisibility(View.VISIBLE);
                market.setVisibility(View.INVISIBLE);
                broad.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                web.setVisibility(View.VISIBLE);
                message_list.setVisibility(View.VISIBLE);
                message_interface.setVisibility(View.VISIBLE);
                main_interface.setVisibility(View.VISIBLE);

            }
        });

        adapter = new messageAdapter(MainActivity.this, R.layout.message_item, MsgList);//实例化适配器

        message_list.setAdapter(adapter);//给listview设置适配器
        message_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                messageItem messageItem = MsgList.get(position);//获取对应项内容

                chat_aim = new Friend(messageItem.getSender_id(),String.valueOf(messageItem.getSender_id()),messageItem.getImageId(),messageItem.getSender_ip(),11000);

                Intent intent = new Intent(MainActivity.this, MsgActivity.class);//点击时跳转到聊天页面
                startActivity(intent);
            }
        });


        /*待写功能
        * 向服务端获取未读消息，使用tcp，一次连接多次传输
        * 每次获取10条（根据消息大小）
        *
        * */

    }

    public void popupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.popup_add:
                    Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.popup_delete:
                    Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.popup_more:
                    Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        });
        menuInflater.inflate(R.menu.web_menu, popupMenu.getMenu());
        popupMenu.show();
    }

    /*处理网络线程返回的信息，当前为处理公告栏内容*/
    @Override
    public void processMessage(Message msg) {
        Log.d("pro", "主活动处理消息");
        Log.d("ttt2",String.valueOf(tcp_sender_tread.isAlive()));
        String content = msg.getData().getString("content");
        Log.d("msgProssess_Main", "msg.what（msgtype） " + msg.what + "\nmsg携带的bundle（msgcontent）内容如下\n" + content);
        switch (msg.what) {
            case Msg.TYPE_BROADCAST: {
                broad.setText("[公告栏]\n" + content);
                break;
            }
            case Msg.TYPE_SENT:
                //注意图灵传递消息是通过msg.obj,而自己写的则是bundle带的数据！
                String sender_ip = findipbyid(msg.arg1);
                String time = msg.getData().getString("time");
                Log.d("mark显示测试", content);
                Log.d("mark显示测试", sender_ip);
                messageItem temp = new messageItem(msg.what,msg.arg1,sender_ip,R.drawable.image_5, content,time);

                MsgList.add(temp);
                adapter.notifyDataSetChanged();
                //notifyItemInserted(msgList.size()-1);//更新适配器，通知适配器消息列表有新的数据插入
                message_list.smoothScrollToPosition(MsgList.size() - 1);//显示最新的消息，定位到最后一行
                //message_list
                //showData(content);

                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        tcp_sender.setHandler(handler);
        udp_sender.setHandler(handler);
        udp_listener.setHandler(handler);
        Msg msg = new Msg(mainserverIp,mainserverPort,Msg.TYPE_BROADCAST, user_id, mainserverId, "broadcast");
        tcp_sender.putMsg(msg);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("销毁", "main");
        /*向服务器发送退出登录信息*/
        Msg msg = new Msg(mainserverIp,mainserverPort,Msg.TYPE_QUIT, user_id, mainserverId, "quit");
        Log.d("msg", "消息构造完成");
        udp_sender.putMsg(msg);
        //udp_sender_tread.interrupt();


//        Msg msg=new Msg(Msg.TYPE_QUIT,user_id,"quit");
//        Log.d("msgSend","发送退出登录消息");
//        netThread.setMsg(msg);
//        netThread.setHandler(handler);
//        new Thread(netThread).start();//子线程开始运行
    }
}