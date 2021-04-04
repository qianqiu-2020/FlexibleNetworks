package com.example.flexiblenetworks;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


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


    public static class MyCallback implements ComponentCallbacks {
        @Override
        public void onConfigurationChanged(Configuration arg) {
        }

        @Override
        public void onLowMemory() {
//do release operation
            Log.d("销毁1","main");

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        MyCallback callBacks =new MyCallback();
        this.registerComponentCallbacks( callBacks );
        this.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }
            @Override
            public void onActivityStarted(Activity activity) {
                //sActivity = activity; //可以获取到栈顶对象
            }
            @Override
            public void onActivityResumed(Activity activity) {
            }
            @Override
            public void onActivityPaused(Activity activity) {
            }
            @Override
            public void onActivityStopped(Activity activity) {
            }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d("销毁","main");
            }
        });
        setContentView(R.layout.activity_main);
        netThread_udp.setHandler(handler);//
        listenTread=new Thread(netThread_udp);
        listenTread.start();//打开侦听UDP套接字

        information=(Button)findViewById(R.id.information);
        friendchat=(Button)findViewById(R.id.friendchat);
        location=(Button) findViewById(R.id.location);
        broad=(TextView)findViewById(R.id.text_view);
        update=(Button)findViewById(R.id.update);
        market=(Button)findViewById(R.id.market);
        exit=(ImageView)findViewById(R.id.exit);
        imageView=(ImageView)findViewById(R.id.imageview);

        main_interface=(ImageView) findViewById(R.id.main_interface);
        message_interface=(ImageView) findViewById(R.id.message_interface);
        web=(ImageView)findViewById(R.id.web);
        message_list=(ListView)findViewById(R.id.message_list);
        message_list.setVisibility(View.INVISIBLE);

        setSupportActionBar(findViewById(R.id.toolbar));
        setting=(ImageView)findViewById(R.id.setting) ;
        navigationView=(NavigationView)findViewById(R.id.nav_view);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
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
                Intent intent=new Intent(MainActivity.this,MarketActivity.class);
                startActivity(intent);
            }
        });

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
                Intent intent=new Intent(MainActivity.this, LBSActivity.class);//打开聊天模块
                startActivity(intent);
                //finish();主活动一直存在不销毁
            }
        });
        update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,DownLoadActivity.class);//打开下载模块
                startActivity(intent);
                //finish();主活动一直存在不销毁
            }
        });
        exit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();//退出程序
            }
        });
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
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
                Log.d("ssss","1111111");
            }
        });
        message_interface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("ssss","111111222221");
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

    }
    public void popupMenu(View v){
        PopupMenu popupMenu=new PopupMenu(MainActivity.this,v);
        MenuInflater menuInflater=popupMenu.getMenuInflater();
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
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
        menuInflater.inflate(R.menu.web_menu,popupMenu.getMenu());
        popupMenu.show();
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("销毁","main");
        /*向服务器发送退出登录信息*/
        Msg msg=new Msg(Msg.TYPE_QUIT,user_id,"quit");
        Log.d("msgSend","发送退出登录消息");
        netThread.setMsg(msg);
        netThread.setHandler(handler);
        new Thread(netThread).start();//子线程开始运行
    }
}