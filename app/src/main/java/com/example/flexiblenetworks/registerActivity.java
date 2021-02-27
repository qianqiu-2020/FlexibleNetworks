package com.example.flexiblenetworks;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class registerActivity extends BaseActivity implements View.OnClickListener{
    private EditText name;
    private EditText passwordEdit;
    private EditText surePassword;
    private Button sure;
    private Button cancel;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;

    public registerActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_register);
/*        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null) actionBar.hide();*/
        Init();
    }

    private void Init() {
        this.name = (EditText)this.findViewById(R.id.username);
        this.passwordEdit = (EditText)this.findViewById(R.id.password);
        this.surePassword = (EditText)this.findViewById(R.id.sure_password);
        this.cancel = (Button)this.findViewById(R.id.cancelRegister);
        this.sure = (Button)this.findViewById(R.id.addRegister);
        this.sure.setOnClickListener(registerActivity.this);
        this.cancel.setOnClickListener(registerActivity.this);
        this.pref= getSharedPreferences("login", Context.MODE_PRIVATE);
    }

    public void onClick(View v) {
        String account=name.getText().toString().trim();
        String password=passwordEdit.getText().toString();
        String password2=surePassword.getText().toString();
        if(account.isEmpty()||password.isEmpty())
        {
            Toast.makeText(ActivityCollector.activities.get(ActivityCollector.activities.size()-1),"账号或密码为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!password.equals(password2)){
            Toast.makeText(ActivityCollector.activities.get(ActivityCollector.activities.size()-1),"两次密码输入不一样！",Toast.LENGTH_SHORT).show();
            return;
        }
        editor=pref.edit();

        editor.clear();
        editor.apply();
        Log.d("local","apply，已存入preference");
/*                if(autoLogin.isChecked()){
                    editor.putBoolean("auto_login",true);
                }else {
                    editor.clear();
                }
                editor.apply();*/

        /*向服务器发送注册信息*/
        Msg msg=new Msg(Msg.TYPE_LOGIN_REGISTER,0,account+"@@"+password);
        Log.d("msg","消息构造完成");
        netThread.setMsg(msg);
        netThread.setHandler(handler);
        new Thread(netThread).start();//子线程开始运行
    }

    @Override
    public void processMessage(Message msg) {
        String content=msg.getData().getString("content");
        int mark;
        long id=0;
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
                finish();
                break;
            case 1114:
                Toast.makeText(ActivityCollector.activities.get(ActivityCollector.activities.size()-1),"注册成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case 1115:
                Toast.makeText(ActivityCollector.activities.get(ActivityCollector.activities.size()-1),"此账号已被注册",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(ActivityCollector.activities.get(ActivityCollector.activities.size()-1),"未知错误",Toast.LENGTH_SHORT).show();
        }
    }
}
