package com.cjj.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cjj.com.customview.AnFQNumEditText;

/**
 * 程序主页
 */
public class MainActivity extends AppCompatActivity {

    private AnFQNumEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (AnFQNumEditText) findViewById(R.id.editTextView);
        editText.setEtHint("请输入内容。。。")
                .setLength(100)
                .setType(AnFQNumEditText.SINGULAR)
                .setLineColor("#3F51B5")//设置横线颜色
                .show();

    }


}
