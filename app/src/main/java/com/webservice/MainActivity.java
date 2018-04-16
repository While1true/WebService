package com.webservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Webservice webservice = new Webservice.Builder().setApplication(getApplication())
                .setGson(new Gson())
                .setUrl("111")
                .setShowlog(true)
                .setNamespace("dxxxc")
                .setTimeout(10000)
                .setNamespace("112").Builder();
    }
}
