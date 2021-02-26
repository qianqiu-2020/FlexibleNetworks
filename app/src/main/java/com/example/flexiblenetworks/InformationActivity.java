package com.example.flexiblenetworks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
/*此活动为公共信息平台，当前考虑通过网页实现*/
public class InformationActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        //向服务器请求数据

        //暂时设置为网页
        webView=(WebView)findViewById((R.id.web_view));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://xingmeng.work/");
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //返回网页的上一页，防止直接退出
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}