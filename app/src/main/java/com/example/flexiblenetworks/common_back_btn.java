package com.example.flexiblenetworks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class common_back_btn extends BaseActivity {

    @Override
    public void processMessage(Message msg) {
        Toast.makeText(this, "试试", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_back_btn);
        ImageView imageView=(ImageView)findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this.finish();
            }
        });

    }
}