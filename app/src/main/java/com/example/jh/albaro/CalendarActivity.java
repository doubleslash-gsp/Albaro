package com.example.jh.albaro;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity {

    //어드민 여부, 상호명, 직명넘버
    //    private int num;
    //    private String store_id;
    //    private String store_name;
    //    private String Is_admin;

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        textView = (TextView)findViewById(R.id.tv_test);
        Intent intent  = getIntent();

        textView.setText(intent.getStringExtra("store_id") +  ", " + intent.getStringExtra("store_name") +
                            ", "  + intent.getStringExtra("Is_admin"));
        textView.setTextColor(Color.parseColor(intent.getStringExtra("color")));
    }
}
