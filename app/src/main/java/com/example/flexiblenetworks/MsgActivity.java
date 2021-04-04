package com.example.flexiblenetworks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
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
    private static final String WEB_SITE = "http://openapi.tuling123.com/openapi/api/v2";//接口地址
    private static final String KEY = "2bf269c26b324fc8bcf8d2b332313182";//apikey
    private SwipeRefreshLayout swipeRefreshLayout;
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
            JSONObject obj = new JSONObject(vlResult);
            JSONObject intent = obj.getJSONObject("intent");
            int code = intent.getInt("code");
            JSONArray results = obj.getJSONArray("results");
            Log.d("mark显示接收消息内容21", String.valueOf(results.length()));

            for (int i = 0; i < results.length(); i++) {
                String content = "";
                JSONObject result = results.getJSONObject(i);
                JSONObject values = result.getJSONObject("values");
                if (values.has("text"))
                    content = values.getString("text");
                if (values.has("url")) {
                    content = values.getString("url");
                    inputText.setText(content);
                }
            /*    if(results.length()==1)
                    content=values.getString("text");
                else */
                Log.d("mark显示接收消息内容2", content);
                switch (code) {
                    case 4003:
                        showData("主人，我今天累了，我要休息了，明天再来找我耍吧");
                        continue;
                    default:
                        showData(content);
                        continue;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showData("主人，你的网络不太好哦");
        }
    }

    private void showData(String content) {
        /*新增消息时的操作*/
        Msg msg = new Msg(Msg.TYPE_RECEIVERD, 0, content);
        msgList.add(msg);//添加消息到消息列表
        Log.d("mark显示接收消息内容3", content);
        adapter.notifyItemInserted(msgList.size() - 1);//更新适配器，通知适配器消息列表有新的数据插入
        msgRecyclerView.scrollToPosition(msgList.size() - 1);//显示最新的消息，定位到最后一行
    }

    private List<Msg> msgList = new ArrayList<>();//消息列表
    private EditText inputText;//输入消息内容
    private Button send;//发送按钮
    private RecyclerView msgRecyclerView;//此控件用于显示消息
    private MsgAdapter adapter;//RecyclerView的适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);//注意指向的布局
        if (chat_aim.getName().equals("图灵机器人"))
            initMsgs();//初始化消息列表，当前未使用网络线程，故使用本地数据
        else {
            tempinitMsgs();
        }


        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//布局管理器
        msgRecyclerView.setLayoutManager(layoutManager);//给RecyclerView设置布局管理器
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);//给RecyclerView设置适配器
        Log.d("mark", msgList.get(1).getContent());//从List中获取内容的方法

        setSupportActionBar(findViewById(R.id.toolbar));
        TextView name = findViewById(R.id.name);
        name.setText(chat_aim.getName());

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {



                    /*新增消息时的操作*/
                    Msg msg = new Msg(Msg.TYPE_SENT, user_id, content);
                    msgList.add(msg);//添加消息到消息列表
                    Log.d("mark", content);
                    adapter.notifyItemInserted(msgList.size() - 1);//更新适配器，通知适配器消息列表有新的数据插入
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);//显示最新的消息，定位到最后一行
                    inputText.setText("");

                    /*根据消息类型判定发送对象*/
                    if (chat_aim.getName().equals("图灵机器人"))//向图灵服务器发送数据
                        sendData(content);
                    else {//好友消息

                        /*私聊情况一：对方在线，通过服务器获取到对方直接发送消息(不经过服务器)*/
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String ip = chat_aim.getIp();
                                    int port = 11000;

                                    /*UDP发送消息*/
                                    // 创建发送端Socket, 绑定本机IP地址, 绑定任意一个未使用的端口号
                                    DatagramSocket socket = new DatagramSocket();
                                    // 创建发送端Packet, 指定数据, 长度, 地址, 端口号
                                    String temp = msg.getType() + "\r\n" + msg.getsender_id() + "\r\n" + msg.getContent();
                                    DatagramPacket packet = new DatagramPacket(temp.getBytes("UTF-8"), temp.getBytes().length, InetAddress.getByName(ip), port);
                                    // 使用Socket发送Packet
                                    socket.send(packet);
                                    // 关闭Socket
                                    socket.close();
                                    Log.d("mark", "发送UDP流为" + temp);
                                } catch (SocketException | UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    //netThread_udp.sendUdpData(msg, chat_aim.getIp(), 11000);
                    /*私聊情况二：对方不在线，发送给服务器暂存*/
                    /*私聊情况三：对方假在线（下线后在线状态没有及时更新），或者网络原因发送失败，未收到确认消息。
                    先尝试重新发送，否则发送给服务端*/
                }





                /*                    *//*向服务器发送信息，未实现*//*
                    netThread.setMsg(msg);
                    netThread.setHandler(handler);
                    new Thread(netThread).start();//子线程开始运行*/

            }
        });
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.btn_blue_normal);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }
    private void refresh(){
        //本地刷新测试
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initMsgs();
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
    private void sendData(String content) {//content已不为空
        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        String jsonStr = "{ \"reqType\":0,\"perception\": {\"inputText\": {\"text\": \"" + content + "\" },\"inputImage\": {\"url\": \"imageUrl\" },\"selfInfo\": {\"location\": {\"city\": \"北京\",\"province\": \"北京\",\"street\": \"信息路\" } } },\"userInfo\": {\"apiKey\": \"2bf269c26b324fc8bcf8d2b332313182\",\"userId\": \"" + user_id + "\" } }";

        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url(WEB_SITE)
                .post(body)
                .build();

        // Request request=new Request.Builder().url(WEB_SITE+"?key="+KEY+"&info="+content).build();
        Call call = okHttpClient.newCall(request);
        //开启异步线程访问网络
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res = response.body().string();
                Message msg = new Message();
                msg.what = Msg.TYPE_TULING_OK;
                msg.obj = res;
                Log.d("mark显示接收消息内容0", res);
                handler.sendMessage(msg);//注意此处是个独立的Handler，需要在MsgActiviy中再次写
            }
        });
    }


    /*收到消息在这里处理*/
    @Override
    public void processMessage(Message msg) {
        //Log.d("mark显示接收消息内容1", (String) msg.obj);
        switch (msg.what) {
            case Msg.TYPE_TULING_OK:
                if (msg.obj != null) {
                    String vlResult = (String) msg.obj;
                    parseData(vlResult);
                }
                break;
            case Msg.TYPE_SENT:
                //注意图灵传递消息是通过msg.obj,而自己写的则是bundle带的数据！
                    String content=msg.getData().getString("content");
                    Log.d("mark显示测试", content);
                    showData(content);
                break;


        }
    }

    /*初始化消息列表，后续可改写为处理未读消息*/
    private void initMsgs() {
        Msg msg1 = new Msg(Msg.TYPE_RECEIVERD, 0, "这里是图灵机器人，来找我聊天吧！");
        msgList.add(msg1);
        Msg msg2 = new Msg(Msg.TYPE_RECEIVERD, 0, "可以让我讲笑话，脑筋急转弯，玩成语接龙，新闻资讯，星座运势，歇后语，绕口令，顺口溜，天气查询，菜谱大全，快递查询，列车查询，日期查询，城市邮编等等....");
        msgList.add(msg2);
/*        Msg msg3=new Msg(Msg.TYPE_RECEIVERD,0,"This is Tom.Nice talking to you.");
        msgList.add(msg3);*/
    }

    private void tempinitMsgs() {
        Msg msg1 = new Msg(Msg.TYPE_RECEIVERD, 0, "暂时无法和好友聊天哦~");
        msgList.add(msg1);
        Msg msg2 = new Msg(Msg.TYPE_RECEIVERD, 0, "去试试和图灵机器人聊天吧！");
        msgList.add(msg2);
        Msg msg3 = new Msg(Msg.TYPE_SENT, 0, "好的-.-");
        msgList.add(msg3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}