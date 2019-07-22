package com.example.jh.albaro;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jh.albaro.FindPwd.FindPWActivity;
import com.example.jh.albaro.Register.RegisterActivity;
import com.example.jh.albaro.ServerData.HttpClient;
import com.example.jh.albaro.ServerData.MemberInfo;
import com.example.jh.albaro.ServerData.StaticVariable;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email, password;
    private Button login, find_id, find_pw, register, autologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.et_email);
        password = (EditText) findViewById(R.id.et_passwd);
        login = (Button) findViewById(R.id.bt_login);
        find_id = (Button) findViewById(R.id.bt_find_id);
        find_pw = (Button) findViewById(R.id.bt_find_pw);
        register = (Button) findViewById(R.id.bt_register);
        autologin = (Button) findViewById(R.id.ib_autologin);

        login.setOnClickListener(LoginActivity.this);
        find_id.setOnClickListener(LoginActivity.this);
        find_pw.setOnClickListener(LoginActivity.this);
        register.setOnClickListener(LoginActivity.this);
        autologin.setOnClickListener(LoginActivity.this);
    }

    /* 클릭 이벤트 모임 */
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt_login:
                String mail = email.getText().toString();
                String pwd = password.getText().toString();



                if(mail.equals("") || pwd.equals("")){
                    Toast.makeText(getApplicationContext(), "이메일 혹은 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                }else{
                    LoginTask task = new LoginTask();
                    task.execute(mail, pwd);
                }
                break;
            case R.id.bt_find_id:
                ChangePage(FindIDActivity.class);
                break;
            case R.id.bt_find_pw:
                ChangePage(FindPWActivity.class);
                break;
            case R.id.bt_register:
                ChangePage(RegisterActivity.class);
                break;
            case R.id.ib_autologin:
                break;

        }
    }


    class LoginTask extends AsyncTask<String, Integer, String> {

        String server = "http://" + StaticVariable.server_ip + StaticVariable.server_web_port;

        @Override protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            // HTTP 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", server+ "/login.do");

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
                    Intent intent = new Intent(LoginActivity.this, StoreListActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "아이디 혹은 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                }

            }catch (NullPointerException e){
                e.printStackTrace();
            }


        }
    }


    /* 화면전환 함수 */
    protected void ChangePage(Class page){
        Intent intent = new Intent(LoginActivity.this, page);
        startActivity(intent);
    }



}
