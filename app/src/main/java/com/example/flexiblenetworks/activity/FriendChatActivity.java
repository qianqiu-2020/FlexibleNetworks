package com.example.flexiblenetworks.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.flexiblenetworks.define.Friend;
import com.example.flexiblenetworks.adapter.FriendAdapter;
import com.example.flexiblenetworks.define.Msg;
import com.example.flexiblenetworks.R;

import java.util.ArrayList;
import java.util.List;
/*此活动用来显示好友列表*/
public class FriendChatActivity extends BaseActivity {

  //  private String[] data={"image1","image2","image3","image4","image5","image6","image7","image8","image9","image10","image11","image12","image13"};
    //转为全局变量private List<Friend> FriendList=new ArrayList<>();
    FriendAdapter adapter;
    ListView listView;
    ImageView imageView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.update:
                Toast.makeText(this,"刷新好友列表",Toast.LENGTH_SHORT).show();
                /*向服务器发送申请列表*/
                Msg msg=new Msg(mainserverIp,mainserverPort,Msg.TYPE_ONLINELIST,user_id,mainserverId,"get user online list");//构造自定义协议内容
                Log.d("msg","发送消息构造完成，内容为"+msg.getContent());
                tcp_sender.putMsg(msg);//将要发送内容设置好
                tcp_sender_tread.interrupt();//网络子线程开始运行

                break;
            case R.id.add:
                Toast.makeText(this,"添加",Toast.LENGTH_SHORT).show();
                break;
            case R.id.scan:
                Toast.makeText(this,"扫描",Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    /*活动创建时先加载一次在线列表*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ttt3",String.valueOf(tcp_sender_tread.isAlive()));
        super.onCreate(savedInstanceState);
        Log.d("ttt4",String.valueOf(tcp_sender_tread.isAlive()));
        setContentView(R.layout.activity_friendchat);
        imageView=(ImageView)findViewById(R.id.back);
        setSupportActionBar(findViewById(R.id.toolbar));//如果不这样，菜单则不会显示，但注意此时如果toolbar中没有现在的新布局，则会显示androidmanifrst中的应用名/活动中的label
/*        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null) actionBar.hide();*/

        tcp_sender.setHandler(handler);
        udp_sender.setHandler(handler);
        udp_listener.setHandler(handler);
        //根据不同活动中设置，可以使网络线程发送到不同活动中。
        /*向服务器发送申请，获取在线列表*/
        Msg msg=new Msg(mainserverIp,mainserverPort,Msg.TYPE_ONLINELIST,user_id,mainserverId,"get user online list");//构造自定义协议内容
        Log.d("msg","发送消息构造完成，内容为"+msg.getContent());
        tcp_sender.putMsg(msg);//将要发送内容设置好
        //tcp_sender_tread.interrupt();//网络子线程开始运行


        adapter=new FriendAdapter(FriendChatActivity.this,R.layout.friend_item ,FriendList);//实例化适配器
        listView=(ListView)findViewById(R.id.list_view);//获取listview实例
        listView.setAdapter(adapter);//给listview设置适配器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend Friend=FriendList.get(position);//获取对应项内容
                Toast.makeText(FriendChatActivity.this,Friend.getName(),Toast.LENGTH_SHORT).show();
                chat_aim=Friend;

                Intent intent=new Intent(FriendChatActivity.this,MsgActivity.class);//点击时跳转到聊天页面
                startActivity(intent);
            }
        });//每一项被点击时执行的操作


    }
    /*此活动需要处理的网络消息为好友列表，当网络线程获取到数据时，会调用此方法*/
    @Override
    public void processMessage(Message msg) {
        Log.d("pro","好友列表活动处理消息");
        String content=msg.getData().getString("content");//网络线程传过来的内容
        Log.d("msgProssess_Chat","msg.what（msgtype） "+msg.what+"\nmsg携带的bundle（msgcontent）内容如下\n"+content);
        switch (msg.what){
//            case Msg.TYPE_ONLINE_LIST :{
//                Log.d("msgProssess_Chat","收到在线列表"+content);
//                Friend temp=new Friend(content,R.drawable.image_1);
//                FriendList.add(temp);
//            }
            case Msg.TYPE_ONLINELIST:{
                Log.d("msgProssess_Chat","收到申请的在线列表"+content);
                initFriends();
                List<Friend> tempfriendlist=new ArrayList<>();
                String[] templist=content.split("\n");
                for(int i=0;i<templist.length;i++)
                {
                    String[]tempfriend=templist[i].split("\\|");//注意转义字符！
                    Friend temp=new Friend(Integer.parseInt(tempfriend[0]),tempfriend[1],R.drawable.image_6,tempfriend[2],Integer.parseInt(tempfriend[3]));
                    FriendList.add(temp);
                }
                adapter.notifyDataSetChanged();
                        //notifyItemInserted(msgList.size()-1);//更新适配器，通知适配器消息列表有新的数据插入
                listView.smoothScrollToPosition(FriendList.size()-1);//显示最新的消息，定位到最后一行
            }
        }
    }

    private void initFriends(){
//        for(int i=0;i<2;i++){
            FriendList.clear();
            Friend image1=new Friend("图灵机器人",R.drawable.image_1);
            FriendList.add(image1);
/*            Friend image2=new Friend("image2",R.drawable.image_2);
            FriendList.add(image2);
            Friend image3=new Friend("image3",R.drawable.image_3);
            FriendList.add(image3);
            Friend image4=new Friend("image4",R.drawable.image_4);
            FriendList.add(image4);
            Friend image5=new Friend("image5",R.drawable.image_5);
            FriendList.add(image5);
            Friend image6=new Friend("image6",R.drawable.image_6);
            FriendList.add(image6);
            Friend image7=new Friend("image7",R.drawable.image_7);
            FriendList.add(image7);
            Friend image8=new Friend("image8",R.drawable.image_8);
            FriendList.add(image8);
            Friend image9=new Friend("image9",R.drawable.image_9);
            FriendList.add(image9);
            Friend image10=new Friend("image10",R.drawable.image_10);
            FriendList.add(image10);*/
//        }
    }
}