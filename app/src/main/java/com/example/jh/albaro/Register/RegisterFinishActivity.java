package com.example.jh.albaro.Register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jh.albaro.LoginActivity;
import com.example.jh.albaro.R;

public class RegisterFinishActivity extends AppCompatActivity {

    private TextView email;
    private Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_finish);

        email = (TextView) findViewById(R.id.tv_email);
        finish = (Button) findViewById(R.id.bt_finish);

        Intent i = getIntent();

        email.setText(i.getExtras().getString("email"));

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterFinishActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
