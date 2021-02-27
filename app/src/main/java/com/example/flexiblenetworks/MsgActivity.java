package com.example.flexiblenetworks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*此活动为聊天界面*/
public class MsgActivity extends BaseActivity {
    /*接入图灵机器人*/
    private static final String WEB_SITE="http://openapi.tuling123.com/openapi/api/v2";//接口地址
    private static final String KEY="2bf269c26b324fc8bcf8d2b332313182";//apikey
/*    class MHandler extends Handler{
        @Override
        public void dispatchMessage(Message msg){
            super.dispatchMessage(msg);
            Log.d("mark显示接收消息内容1",(String)msg.obj);
            switch (msg.what){
                case Msg.TYPE_TULING_OK:
                    if(msg.obj!=null){
                        String vlResult=(String)msg.obj;
                        parseData(vlResult);
                    }
                    break;
            }
        }
    }
    private MHandler mHandler;*/
    private void parseData(String vlResult) {
        try {
            JSONObject obj=new JSONObject(vlResult);
            JSONObject intent=obj.getJSONObject("intent");
            int code=intent.getInt("code");
            JSONArray results=obj.getJSONArray("results");
            for(int i=0;i<results.length();i++)
            {
                JSONObject result=results.getJSONObject(i);
                JSONObject values=result.getJSONObject("values");
                String content=values.getString("text");

                switch (code){
                    case 4003:
                        showData("主人，我今天累了，我要休息了，明天再来找我耍吧");
                        break;
                    default:
                        showData(content);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showData("主人，你的网络不太好哦");
        }
    }
    private void showData(String content){
        /*新增消息时的操作*/
        Msg msg=new Msg(Msg.TYPE_RECEIVERD,0,content);
        msgList.add(msg);//添加消息到消息列表
        Log.d("mark显示接收消息内容",content);
        adapter.notifyItemInserted(msgList.size()-1);//更新适配器，通知适配器消息列表有新的数据插入
        msgRecyclerView.scrollToPosition(msgList.size()-1);//显示最新的消息，定位到最后一行
    }
    private List<Msg> msgList=new ArrayList<>();//消息列表
    private EditText inputText;//输入消息内容
    private Button send;//发送按钮
    private RecyclerView msgRecyclerView;//此控件用于显示消息
    private MsgAdapter adapter;//RecyclerView的适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);//注意指向的布局

        initMsgs();//初始化消息列表，当前未使用网络线程，故使用本地数据

        inputText=(EditText)findViewById(R.id.input_text);
        send=(Button)findViewById(R.id.send);
        msgRecyclerView=(RecyclerView)findViewById(R.id.msg_recycler_view);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);//布局管理器
        msgRecyclerView.setLayoutManager(layoutManager);//给RecyclerView设置布局管理器
        adapter=new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);//给RecyclerView设置适配器
        Log.d("mark",msgList.get(1).getContent());//从List中获取内容的方法

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=inputText.getText().toString();
                if(!"".equals(content)){
                    /*新增消息时的操作*/
                    Msg msg=new Msg(Msg.TYPE_SENT,0,content);
                    msgList.add(msg);//添加消息到消息列表
                    Log.d("mark",content);
                    adapter.notifyItemInserted(msgList.size()-1);//更新适配器，通知适配器消息列表有新的数据插入
                    msgRecyclerView.scrollToPosition(msgList.size()-1);//显示最新的消息，定位到最后一行
                    inputText.setText("");
                    /*向图灵服务器发送数据*/
                    sendData(content);
/*                    *//*向服务器发送信息，未实现*//*
                    netThread.setMsg(msg);
                    netThread.setHandler(handler);
                    new Thread(netThread).start();//子线程开始运行*/

                }
            }
        });
    }

    private void sendData(String content) {//content已不为空
        OkHttpClient okHttpClient=new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        String jsonStr = "{ \"reqType\":0,\"perception\": {\"inputText\": {\"text\": \""+content+"\" },\"inputImage\": {\"url\": \"imageUrl\" },\"selfInfo\": {\"location\": {\"city\": \"北京\",\"province\": \"北京\",\"street\": \"信息路\" } } },\"userInfo\": {\"apiKey\": \"2bf269c26b324fc8bcf8d2b332313182\",\"userId\": \""+user_id+"\" } }";

        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url(WEB_SITE)
                .post(body)
                .build();

       // Request request=new Request.Builder().url(WEB_SITE+"?key="+KEY+"&info="+content).build();
        Call call=okHttpClient.newCall(request);
        //开启异步线程访问网络
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res=response.body().string();
                Message msg=new Message();
                msg.what=Msg.TYPE_TULING_OK;
                msg.obj=res;
                Log.d("mark显示接收消息内容0",res);
                handler.sendMessage(msg);//注意此处是个独立的Handler，需要在MsgActiviy中再次写
            }
        });
    }



    /*收到消息在这里处理*/
    @Override
    public void processMessage(Message msg) {
        Log.d("mark显示接收消息内容1",(String)msg.obj);
        switch (msg.what){
            case Msg.TYPE_TULING_OK:
                if(msg.obj!=null){
                    String vlResult=(String)msg.obj;
                    parseData(vlResult);
                }
                break;
        }
    }
    /*初始化消息列表，后续可改写为处理未读消息*/
    private void initMsgs() {
        Msg msg1=new Msg(Msg.TYPE_RECEIVERD,0,"Hello guy.");
        msgList.add(msg1);
        Msg msg2=new Msg(Msg.TYPE_SENT,0,"Hello.Who is that?");
        msgList.add(msg2);
        Msg msg3=new Msg(Msg.TYPE_RECEIVERD,0,"This is Tom.Nice talking to you.");
        msgList.add(msg3);
    }
}