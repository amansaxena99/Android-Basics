package com.example.hackernews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ArticleActivity extends AppCompatActivity {

    WebView articleWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        articleWebView = findViewById(R.id.articleWebView);
        articleWebView.getSettings().setJavaScriptEnabled(true);
        articleWebView.setWebViewClient(new WebViewClient());

        Intent intent = getIntent();
        articleWebView.loadUrl(intent.getStringExtra("content"));
//        articleWebView.loadData(intent.getStringExtra("content"), "text/html", "UTH-8");
    }
}
