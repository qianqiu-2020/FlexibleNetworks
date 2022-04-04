package com.example.flexiblenetworks.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flexiblenetworks.R;
import com.example.flexiblenetworks.adapter.InfoAdapter;
import com.example.flexiblenetworks.define.Friend;
import com.example.flexiblenetworks.define.InfoItem;
import com.example.flexiblenetworks.define.Msg;

import java.util.ArrayList;
import java.util.List;

/*此活动为公共信息平台，当前考虑通过网页实现*/
public class InformationActivity extends BaseActivity {

    private List<InfoItem> infoList = new ArrayList<>();
    private InfoAdapter adapter;
    RecyclerView recyclerView;
    EditText searchEdit;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        //向服务器请求数据

        recyclerView = (RecyclerView) findViewById(R.id.info_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new InfoAdapter(infoList);
        recyclerView.setAdapter(adapter);
        searchEdit=(EditText)findViewById(R.id.search_edit);
        searchButton =(Button)findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //将搜索关键词发往服务器
                String content=searchEdit.getText().toString();
                Msg msg=new Msg(mainserverIp,mainserverPort,Msg.TYPE_LOGIN,0,mainserverId,content);//构造自定义协议内容
                Log.d("msg","发送消息构造完成，内容为"+msg.getContent());
                tcp_sender.putMsg(msg);//将要发送内容设置好
            }
        });
        initInfoItems();
    }
    private void initInfoItems(){
        infoList.clear();
        InfoItem temp=new InfoItem("标题","回复");
        infoList.add(temp);
    }
    @Override
    public void processMessage(Message msg) {
        Log.d("pro","资料库列表活动处理消息");
        String content=msg.getData().getString("content");//网络线程传过来的内容
        Log.d("msgProssess_Chat","msg.what（msgtype） "+msg.what+"\nmsg携带的bundle（msgcontent）内容如下\n"+content);
        switch (msg.what){
            case Msg.TYPE_ONLINELIST:{
                Log.d("msgProssess_Chat","收到申请的在线列表"+content);
                initInfoItems();
                String[] templist=content.split("\n");
                for(int i=0;i<templist.length;i++)
                {
                    String[]tempInfoItem=templist[i].split("\\|");//注意转义字符！
                    InfoItem temp=new InfoItem(tempInfoItem[0],tempInfoItem[1]);
                    infoList.add(temp);
                }
                adapter.notifyDataSetChanged();
                //notifyItemInserted(msgList.size()-1);//更新适配器，通知适配器消息列表有新的数据插入
//                listView.smoothScrollToPosition(infoList.size()-1);//显示最新的消息，定位到最后一行
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //返回网页的上一页，防止直接退出
//        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
//            webView.goBack();// 返回前一个页面
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }
}