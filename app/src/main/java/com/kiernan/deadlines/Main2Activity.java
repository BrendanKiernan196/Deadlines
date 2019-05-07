package com.kiernan.deadlines;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.webkit.WebView;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebView webView = (WebView)
                findViewById(R.id.webview);
        webView.loadUrl("https://www.calendar.com/blog/10-essential-tips-to-ensure-you-never-miss-a-deadline/");
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
