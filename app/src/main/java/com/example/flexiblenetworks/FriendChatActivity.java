package com.example.flexiblenetworks;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

import java.util.ArrayList;
import java.util.List;
/*此活动用来显示好友列表*/
public class FriendChatActivity extends BaseActivity {

  //  private String[] data={"image1","image2","image3","image4","image5","image6","image7","image8","image9","image10","image11","image12","image13"};
    private List<Friend> FriendList=new ArrayList<>();
    /*活动创建时先加载一次在线列表*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendchat);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null) actionBar.hide();

        new Thread(netThread_udp).start();//从服务端获取在线列表，暂未实现
        initFriends();//故先直接使用本地已有资源初始化

        FriendAdapter adapter=new FriendAdapter(FriendChatActivity.this,R.layout.friend_item ,FriendList);//实例化适配器
        ListView listView=(ListView)findViewById(R.id.list_view);//获取listview实例
        listView.setAdapter(adapter);//给listview设置适配器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend Friend=FriendList.get(position);//获取对应项内容
                Toast.makeText(FriendChatActivity.this,Friend.getName(),Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(FriendChatActivity.this,MsgActivity.class);//点击时跳转到聊天页面
                startActivity(intent);
            }
        });//每一项被点击时执行的操作
    }
    /*此活动需要处理的网络消息为好友列表，当网络线程获取到数据时，会调用此方法*/
    @Override
    public void processMessage(Message msg) {
        String content=msg.getData().getString("content");//网络线程传过来的内容
        Log.d("msgProssess_Chat","msg.what（msgtype） "+msg.what+"\nmsg携带的bundle（msgcontent）内容如下\n"+content);
        switch (msg.what){
            case Msg.TYPE_ONLINE_LIST :{
                Log.d("msgProssess_Chat","收到在线列表"+content);
                Friend temp=new Friend(content,R.drawable.image_1);
                FriendList.add(temp);
            }
        }
    }

    private void initFriends(){
        for(int i=0;i<2;i++){

            Friend image1=new Friend("图灵机器人",R.drawable.image_1);
            FriendList.add(image1);
            Friend image2=new Friend("image2",R.drawable.image_2);
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
            FriendList.add(image10);
        }
    }
}