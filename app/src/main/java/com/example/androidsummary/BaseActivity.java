package com.example.androidsummary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);
    }

    public void toClass(Class<?> cls){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
    }
}
