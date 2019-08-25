package com.example.jh.albaro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.jh.albaro.FindId.FindIDActivity;
import com.example.jh.albaro.FindPwd.FindPWActivity;
import com.example.jh.albaro.Register.RegisterActivity;
import com.example.jh.albaro.ServerData.HttpClient;
import com.example.jh.albaro.ServerData.MemberInfo;
import com.example.jh.albaro.ServerData.StaticVariable;
import com.example.jh.albaro.WorkSpace.WorkspaceActivity;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email, password;
    private Button login, find_id, find_pw, register, bt_autologin;
    private LinearLayout layout_autologin;
    private String autologin_id, autologin_pwd, autologin_flag;
    private SharedPreferences auto;

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

        bt_autologin = (Button) findViewById(R.id.bt_autologin);
        layout_autologin = (LinearLayout) findViewById(R.id.ll_autologin);

        SharedPreferences init_auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        autologin_id = init_auto.getString("email",null);
        autologin_pwd = init_auto.getString("pwd",null);
        autologin_flag = init_auto.getString("flag","N");

        checkAutoLogin(autologin_flag);

        login.setOnClickListener(LoginActivity.this);
        find_id.setOnClickListener(LoginActivity.this);
        find_pw.setOnClickListener(LoginActivity.this);
        register.setOnClickListener(LoginActivity.this);
        bt_autologin.setOnClickListener(LoginActivity.this);
    }

    public void checkAutoLogin(String flag){
        if(autologin_flag.equals("Y")){
            bt_autologin.setBackgroundResource(R.drawable.delete_white);
            if(autologin_id !=null && autologin_pwd != null && autologin_flag.equals("Y")) { //저장된 값이 있으면 자동로그인
                email.setText(autologin_id);
                password.setText(autologin_pwd);
                LoginTask task = new LoginTask();
                task.execute(autologin_id, autologin_pwd);
            }
        }else{
            bt_autologin.setBackgroundResource(R.drawable.button);
        }

    }


    /* 클릭 이벤트 모임 */
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt_login:
                String mail = email.getText().toString();
                String pwd = password.getText().toString();

                if(mail.equals("") || pwd.equals("") || !isEmailValid(mail)){
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
            case R.id.bt_autologin:
                //TODO: 값 삭제, 이미지 변경
                SharedPreferences.Editor editor = auto.edit();
                if (autologin_flag.equals("N")){ //자동로그인 x -> 자동로그인
                    bt_autologin.setBackgroundResource(R.drawable.delete_white);
                    autologin_flag = "Y";
                } else if(autologin_flag.equals("Y")) {//자동로그인 -> 자동로그인 x
                    bt_autologin.setBackgroundResource(R.drawable.button);
                    autologin_flag = "M";
                }
                break;

        }
    }

    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
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


                    SharedPreferences.Editor autoLogin = auto.edit();
                    if(autologin_flag.equals("Y")){
                        //이메일, 비밀번호 저장
                        autoLogin.putString("email", email.getText().toString());
                        autoLogin.putString("pwd", password.getText().toString());
                        autoLogin.putString("year", result.getYear());
                        autoLogin.putString("month", result.getMonth());
                        autoLogin.putString("day", result.getDay());
                        autoLogin.putString("sex", result.getEmail());
                        autoLogin.putString("phone", result.getEmail());
                        autoLogin.putString("color", result.getColor());
                        autoLogin.putString("flag", "Y");
                        autoLogin.commit();
                    }else{
                        autoLogin.remove("email");
                        autoLogin.remove("pwd");
                        autoLogin.putString("flag", "N");
                        autoLogin.commit();
                    }


                    Intent intent = new Intent(LoginActivity.this, WorkspaceActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(), "아이디 혹은 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor autoLogin = auto.edit();
                    autoLogin.putString("flag", "N");

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
