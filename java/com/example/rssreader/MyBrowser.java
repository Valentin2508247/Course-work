package com.example.rssreader;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.net.URL;

public class MyBrowser extends AppCompatActivity {

    private WebView browser;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_browser);

        url = getIntent().getStringExtra("url");

        try
        {
            browser = findViewById(R.id.my_browser);
            browser.setWebViewClient(new MyWebViewClient());
            browser.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
            browser.getSettings().setAllowFileAccess(true);
            browser.getSettings().setAppCacheEnabled(true);
            //browser.getSettings().setJavaScriptEnabled(true);
            browser.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            if (!isNetworkAvailable())
            {
                browser.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            }
            //else
                //
            //browser.clearCache(true);
            browser.loadUrl(url);
        }
        catch (Exception ex)
        {

        }
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed()
    {
        if(browser.canGoBack())
        {
            browser.goBack();
        }
        else
        {
            super.onBackPressed();
        }
    }
}
