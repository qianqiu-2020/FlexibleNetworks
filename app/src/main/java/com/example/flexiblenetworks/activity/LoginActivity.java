package com.example.flexiblenetworks.activity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flexiblenetworks.define.ActivityCollector;
import com.example.flexiblenetworks.define.Msg;
import com.example.flexiblenetworks.R;
import com.example.flexiblenetworks.network.NetWorkUtil;

/*此活动为登录与注册，保存密码功能采用preference实现*/
public class LoginActivity extends BaseActivity {

    private EditText accountEdit;
    private EditText passwordEdit;
    private Button registerButton;
    private Button loginButton;
    private Button forgetButton;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox remeberPassword;
    private CheckBox autoLogin;
    private TextView status;
    private TextView selfstatus;
    @Override
    protected void onDestroy() {
        super.onDestroy();
          Log.d("销毁","login");
    }

    @Override
    protected void onStop() {
        super.onStop();
        //退出前安全关闭线程
        //tcp_sender.onWork=false;
        //tcp_sender_tread.interrupt();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
/*        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null) actionBar.hide();*/
        accountEdit=(EditText)findViewById(R.id.account);
        passwordEdit=(EditText)findViewById(R.id.password);
        registerButton=(Button)findViewById(R.id.register);
        loginButton=(Button)findViewById(R.id.login);
        forgetButton=(Button)findViewById(R.id.forget);
        pref= getSharedPreferences("login", Context.MODE_PRIVATE);
        remeberPassword=(CheckBox)findViewById(R.id.remember_pass);
        autoLogin=(CheckBox)findViewById(R.id.auto_login);
        status=(TextView) findViewById(R.id.status);
        selfstatus=(TextView) findViewById(R.id.selfstatus);
        boolean isRemember=pref.getBoolean("remember_password",false);
        boolean isAutoLogin=pref.getBoolean("auto_login",false);

        if(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

        if(isRemember){
            String account=pref.getString("account","");
            String password=pref.getString("password","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            remeberPassword.setChecked(true);
            Log.d("local","检测到记住密码已勾选");
        }
/*        if(isAutoLogin){
            String account=pref.getString("account","");
            String password=pref.getString("password","");
            accountEdit.setText(account);
            remeberPassword.setText(password);
            //向服务器验证并跳转至首页
        }*/


        tcp_sender.setHandler(handler);
        //tcp_listener.setHandler(handler);
        tcp_sender_tread=new Thread(tcp_sender);//发送tcp消息
        //tcp_listener_tread=new Thread(tcp_listener);//接收tcp消息
        tcp_sender_tread.start();



        /*查询服务器状态*/
        Msg msg=new Msg(mainserverIp,mainserverPort,Msg.TYPE_VERIFY,100,mainserverId,"isOK?");//构造自定义协议内容
        tcp_sender.putMsg(msg);
        //tcp_sender_tread.interrupt();


        StringBuilder currentPosition=new StringBuilder();
        currentPosition.append("【您的网络信息】").append("\n");
        currentPosition.append("网络 ").append(NetWorkUtil.isNetworkAvailable(this)).append("\n");
        currentPosition.append("GPS ").append(NetWorkUtil.isGpsEnabled(this)).append("\n");
        currentPosition.append("WIFI ").append(NetWorkUtil.isWifi(this)).append("\n");
        currentPosition.append("移动网络 ").append(NetWorkUtil.isMobileNetwork(this)).append("\n");
        currentPosition.append("网络类型 ").append(NetWorkUtil.getNetworkType(this)).append("\n");
        currentPosition.append("提供商 ").append(NetWorkUtil.getProvider(this)).append("\n");
        selfstatus.setText(currentPosition);


        msg=new Msg(mainserverIp,mainserverPort,Msg.TYPE_VERIFY,100,mainserverId,"isOK?");//构造自定义协议内容
        tcp_sender.putMsg(msg);
        //tcp_sender_tread.interrupt();


        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityCollector.activities.get(ActivityCollector.activities.size()-1), RegisterActivity.class);
                startActivity(intent);
                /**String account=accountEdit.getText().toString();
                String password=passwordEdit.getText().toString();
            if(account.isEmpty()||password.isEmpty())
            {
                Toast.makeText(ActivityCollector.activities.get(ActivityCollector.activities.size()-1),"账号或密码为空！",Toast.LENGTH_SHORT).show();
                return;
            }

                editor=pref.edit();
                if(remeberPassword.isChecked()){
                    editor.putBoolean("remember_password",true);
                    editor.putString("account",account);
                    editor.putString("password",password);
                    Log.d("local","将要存储的账号"+account+"密码"+password);
                }else {
                    editor.clear();
                }
                editor.apply();
                Log.d("local","apply，已存入preference");
               if(autoLogin.isChecked()){
                    editor.putBoolean("auto_login",true);
                }else {
                    editor.clear();
                }
                editor.apply();

                /*向服务器发送注册信息
                Msg msg=new Msg(Msg.TYPE_LOGIN_REGISTER,0,account+"@@"+password);
                Log.d("msg","消息构造完成");
                netThread.setMsg(msg);
                netThread.setHandler(handler);
                new Thread(netThread).start();//子线程开始运行*/
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ActivityCollector.activities.get(ActivityCollector.activities.size()-1),MainActivity.class);
                startActivity(intent);


                String account=accountEdit.getText().toString();
                String password=passwordEdit.getText().toString();

                if(account.isEmpty()||password.isEmpty())
                {
                    Toast.makeText(ActivityCollector.activities.get(ActivityCollector.activities.size()-1),"账号或密码为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                /*存入preference的方法*/
                editor=pref.edit();
                if(remeberPassword.isChecked()){
                    editor.putBoolean("remember_password",true);
                    editor.putString("account",account);
                    editor.putString("password",password);
                    Log.d("local","将要存储的账号"+account+"密码"+password);
                }else {
                    editor.clear();
                }
                editor.apply();
                Log.d("local","apply，已存入preference");

/*                if(autoLogin.isChecked()){
                    editor.putBoolean("auto_login",true);
                }else {
                    editor.clear();
                }
                editor.apply();*/

                /*向服务器发送验证信息*/
                Msg msg=new Msg(mainserverIp,mainserverPort,Msg.TYPE_LOGIN,0,mainserverId,account+"@@"+password);//构造自定义协议内容
                Log.d("msg","发送消息构造完成，内容为"+msg.getContent());
                tcp_sender.putMsg(msg);//将要发送内容设置好
                //tcp_sender_tread.interrupt();

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        tcp_sender.setHandler(handler);
    }

    /*处理网络线程返回的数据*/
    @Override
    public void processMessage(Message msg) {
        Log.d("pro","登录活动处理消息");
        if(msg.what==1000)//连接超时异常，验证服务器状态的失败处理
        {
            Toast.makeText(ActivityCollector.activities.get(ActivityCollector.activities.size()-1),"无法连接服务器！",Toast.LENGTH_LONG).show();
            status.setText("【服务器配置】\n主服务器IP:"+mainserverIp+"\n状态:不可连接！");
            return;
        }
        if(msg.what==1001)//连接超时异常，验证服务器状态的失败处理
        {
            Toast.makeText(ActivityCollector.activities.get(ActivityCollector.activities.size()-1),"服务器未响应！",Toast.LENGTH_LONG).show();
            status.setText("【服务器配置】\n主服务器IP:"+mainserverIp+"\n状态:不可连接！");
            return;
        }


        String content=msg.getData().getString("content");
        int mark;
        long id=0;//临时存储接受到的id
        Log.d("msgProcess_login","msg.what（msgType） "+msg.what+"\nmsg携带的bundle（msgContent）内容如下\n"+content);

        /*处理不同情况的返回数据*/
        if(content.contains("@@"))//登录成功时返回数据中含有标记值和id
        {
            String [] list=content.split("@@");
            mark=Integer.valueOf(list[0]);
            id=Integer.valueOf(list[1]);
            Log.d("msg","已分配id"+String.valueOf(id));
        }
        else mark=Integer.valueOf(content);//其他情况只含有标记值

        /*处理不同的标记值*/
        switch (mark){
            case 1111:
                Intent intent=new Intent(ActivityCollector.activities.get(ActivityCollector.activities.size()-1),MainActivity.class);
                startActivity(intent);
                user_id=id;
                Log.d("msg_当前用户id",String.valueOf(user_id));
                //finish();
                break;
            case 1112:
                Toast.makeText(ActivityCollector.activities.get(ActivityCollector.activities.size()-1),"账号不存在/服务器数据库连接断开",Toast.LENGTH_LONG).show();
                break;
            case 1113:
                Toast.makeText(ActivityCollector.activities.get(ActivityCollector.activities.size()-1),"密码错误",Toast.LENGTH_SHORT).show();
                break;
            case 111111://验证服务器状态的成功处理
                status.setText("【服务器配置】\n主服务器IP:"+mainserverIp+"\n状态:可连接");
                break;
            default:
                Toast.makeText(ActivityCollector.activities.get(ActivityCollector.activities.size()-1),"未知错误",Toast.LENGTH_SHORT).show();
        }
    }
}