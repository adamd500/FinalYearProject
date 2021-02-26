package com.example.fyp.ProfessionalFeatures;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.fyp.R;

public class StripeDetails extends AppCompatActivity {
    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_details);

        Intent intent=getIntent();
        String url = intent.getStringExtra("url");
         webview = findViewById(R.id.webView);
        webview.setWebChromeClient(new WebChromeClient());
        webview.getSettings().setJavaScriptEnabled(true);

        webview.loadUrl(url);
    }
}