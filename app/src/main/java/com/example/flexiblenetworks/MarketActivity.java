package com.example.flexiblenetworks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarketActivity extends AppCompatActivity {
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
}