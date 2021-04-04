package com.example.flexiblenetworks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class diBianLan extends AppCompatActivity {
    private Button main_interface;
    private Button message_interface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_di_bian_lan);
        main_interface=(Button)findViewById(R.id.main_interface);
        message_interface=(Button)findViewById(R.id.message_interface);
        main_interface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(diBianLan.this,MainActivity.class);
                startActivity(intent);
            }
        });
        message_interface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_message);
            }
        });
    }
}