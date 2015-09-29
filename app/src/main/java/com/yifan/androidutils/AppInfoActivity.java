package com.yifan.androidutils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AppInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new
                AppInfoFragment()).commit();
        setContentView(R.layout.app_list_fragment);
    }

}
