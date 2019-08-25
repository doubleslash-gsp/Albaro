package com.example.jh.albaro.FindPwd;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jh.albaro.ServerData.HttpClient;
import com.example.jh.albaro.R;
import com.example.jh.albaro.ServerData.MemberInfo;
import com.example.jh.albaro.ServerData.StaticVariable;
import com.google.gson.Gson;

public class SettingPWActivity extends AppCompatActivity {

    private String email;
    private EditText pwd, repwd;
    private  Button bt_ok, bt_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_pw);

        pwd = (EditText) findViewById(R.id.et_passwd);
        repwd = (EditText) findViewById(R.id.et_repasswd);
        bt_ok = (Button) findViewById(R.id.bt_ok);
        bt_back = (Button) findViewById(R.id.bt_back);

        Intent i = getIntent();

        email = i.getExtras().getString("email");

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pwd.getText().toString().equals(repwd.getText().toString())){
                    ResetPwdTask resetpwd = new ResetPwdTask();
                    resetpwd.execute(email, pwd.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    class ResetPwdTask extends AsyncTask<String, Integer, String> {

        String server = "http://" + StaticVariable.server_ip + StaticVariable.server_web_port;

        @Override protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            // HTTP 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", server+ "/resetpwd.do");

            http.addOrReplace("email", params[0]);
            http.addOrReplace("password", params[1]);
            // HTTP 요청 전송
            HttpClient post = http.create();
            post.request();

            // 응답 상태코드 가져오기
            int statusCode = post.getHttpStatusCode();

            // 응답 본문 가져오기
            String body = post.getBody();

            return body;
        }

        /** * @param s : doInBackground에서 리턴한 body */
        @Override
        protected void onPostExecute(String s) {

            Gson gson = new Gson();
            try{
                MemberInfo result = gson.fromJson(s, MemberInfo.class);

                if(result.getResult().equals("Success")){
                    FinishPwd_Dialog customDialog = new FinishPwd_Dialog(SettingPWActivity.this);
                    customDialog.callFunction();
                }else{
                    Toast.makeText(getApplicationContext(), "네트워크나 서버 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }catch (NullPointerException e){
                e.printStackTrace();
            }


        }
    }


}
