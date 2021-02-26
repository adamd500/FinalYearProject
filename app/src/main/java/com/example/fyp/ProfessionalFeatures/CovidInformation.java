package com.example.fyp.ProfessionalFeatures;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.fyp.R;

public class CovidInformation extends AppCompatActivity {

    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_information);

        webview = findViewById(R.id.webView);
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl("https://www.citizensinformation.ie/en/employment/employment_rights_during_covid19_restrictions.html");
    }
}