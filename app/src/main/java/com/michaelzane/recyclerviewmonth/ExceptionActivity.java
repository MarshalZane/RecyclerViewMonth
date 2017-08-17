package com.michaelzane.recyclerviewmonth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * 类描述：有异常的界面
 */
public class ExceptionActivity extends AppCompatActivity {

    private String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception);
        //异常
        System.out.println(s.equals("any string"));
    }
}
