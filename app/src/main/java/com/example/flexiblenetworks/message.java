package com.example.flexiblenetworks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class message extends AppCompatActivity {
    private List<messageItem> messageList=new ArrayList<>();
    messageAdapter messageAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        init();
        messageAdapter=new messageAdapter(message.this,R.layout.message_item ,messageList);//实例化适配器
        listView=(ListView)findViewById(R.id.message_list);//获取listview实例
        listView.setAdapter(messageAdapter);//给listview设置适配器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                messageItem message=messageList.get(position);//获取对应项内容
                Toast.makeText(message.this,message.getName(),Toast.LENGTH_SHORT).show();
            }
        });//每一项被点击时执行的操作
    }
    private void init(){
        for(int i=0;i<2;i++){
            messageItem one=new messageItem("备胎一",R.drawable.image_1,"你大爷的");
            messageList.add(one);
            messageItem two=new messageItem("备胎二",R.drawable.image_2,"我爱你");
            messageList.add(two);
            messageItem three=new messageItem("备胎三",R.drawable.image_3,"我爱你");
            messageList.add(three);
            messageItem four=new messageItem("备胎四",R.drawable.image_4,"我爱你");
            messageList.add(four);
            messageItem five=new messageItem("备胎五",R.drawable.image_5,"我爱你");
            messageList.add(five);

        }
    }
}