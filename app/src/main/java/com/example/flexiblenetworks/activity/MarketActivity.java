package com.example.flexiblenetworks.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.flexiblenetworks.widget.AnimationUtil;
import com.example.flexiblenetworks.widget.FloatingDraftButton;
import com.example.flexiblenetworks.adapter.MarketAdapter;
import com.example.flexiblenetworks.R;
import com.example.flexiblenetworks.define.Titles;
import com.example.flexiblenetworks.define.content;
import com.example.flexiblenetworks.adapter.contentAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MarketActivity extends BaseActivity {
    private List<Titles> titlesList=new ArrayList<>();
    private List<content> contentList=new ArrayList<>();
    contentAdapter madapter;
    ListView listView;
    ImageView imageView;

    FloatingDraftButton floatingDraftButton;
    com.google.android.material.floatingactionbutton.FloatingActionButton liveness;
    com.google.android.material.floatingactionbutton.FloatingActionButton floatingActionButton2;
    com.google.android.material.floatingactionbutton.FloatingActionButton floatingActionButton3;
    com.google.android.material.floatingactionbutton.FloatingActionButton floatingActionButton4;
    com.google.android.material.floatingactionbutton.FloatingActionButton floatingActionButton5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        imageView=(ImageView)findViewById(R.id.back);
        init();//初始化标题数据
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.titles);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        MarketAdapter adapter=new MarketAdapter(titlesList);
        recyclerView.setAdapter(adapter);

        initcontent();
        madapter=new contentAdapter(MarketActivity.this,R.layout.content_item ,contentList);//实例化适配器
        listView=(ListView)findViewById(R.id.content);//获取listview实例
        listView.setAdapter(madapter);//给listview设置适配器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                content mcontent=contentList.get(position);//获取对应项内容
                Toast.makeText(MarketActivity.this,mcontent.getName(),Toast.LENGTH_SHORT).show();
            }
        });//每一项被点击时执行的操作

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this.finish();
            }
        });


        ButterKnife.bind(this);


        floatingDraftButton=(FloatingDraftButton)findViewById(R.id.floatingActionButton);
        liveness=(FloatingActionButton)findViewById(R.id.floatingActionButton_liveness) ;
        floatingActionButton2=(FloatingActionButton)findViewById(R.id.floatingActionButton_2);
        floatingActionButton3=(FloatingActionButton)findViewById(R.id.floatingActionButton_3);
        floatingActionButton4=(FloatingActionButton)findViewById(R.id.floatingActionButton_4);
        floatingActionButton5=(FloatingActionButton)findViewById(R.id.floatingActionButton_5);
        floatingDraftButton.registerButton(liveness);
        floatingDraftButton.registerButton(floatingActionButton2);
        floatingDraftButton.registerButton(floatingActionButton3);
        floatingDraftButton.registerButton(floatingActionButton4);
        floatingDraftButton.registerButton(floatingActionButton5);
        floatingDraftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MarketActivity.this,"hhahahhah",Toast.LENGTH_SHORT).show();
                AnimationUtil.slideButtons(MarketActivity.this,floatingDraftButton);
            }
        });
        liveness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.slideButtons(MarketActivity.this,floatingDraftButton);
                Toast.makeText(getApplicationContext(),"liveness",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void processMessage(Message msg) {

    }

    private void init(){
        for(int i=0;i<2;i++){
            Titles one=new Titles("首页");
            titlesList.add(one);
            Titles two=new Titles("手机");
            titlesList.add(two);
            Titles three=new Titles("电脑");
            titlesList.add(three);
            Titles four=new Titles("首饰");
            titlesList.add(four);
            Titles five=new Titles("书籍");
            titlesList.add(five);

        }
    }
    private void initcontent(){
        for(int i=0;i<2;i++){
            content one=new content("首页",R.drawable.image_1);
            contentList.add(one);
            content two=new content("首页",R.drawable.image_2);
            contentList.add(two);
            content three=new content("首页",R.drawable.image_3);
            contentList.add(three);
            content four=new content("首页",R.drawable.image_4);
            contentList.add(four);
            content five=new content("首页",R.drawable.image_5);
            contentList.add(five);


        }
    }
}