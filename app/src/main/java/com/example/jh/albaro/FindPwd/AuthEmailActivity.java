package com.example.jh.albaro.FindPwd;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jh.albaro.ServerData.HttpClient;
import com.example.jh.albaro.R;
import com.example.jh.albaro.ServerData.MemberInfo;
import com.example.jh.albaro.ServerData.StaticVariable;
import com.google.gson.Gson;

public class AuthEmailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView email, tv_authresult;
    private EditText auth;
    private Button bt_ok, bt_next, bt_send, bt_back;
    private boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_email);

        email = (TextView) findViewById(R.id.tv_email);
        tv_authresult = (TextView) findViewById(R.id.tv_result);
        auth = (EditText) findViewById(R.id.et_authkey);
        bt_ok = (Button) findViewById(R.id.bt_ok);
        bt_next = (Button) findViewById(R.id.bt_next);
        bt_send = (Button) findViewById(R.id.bt_send);
        bt_back = (Button) findViewById(R.id.bt_back);

        Intent i = getIntent();
        email.setText(i.getExtras().getString("email"));
        bt_ok.setOnClickListener(AuthEmailActivity.this);
        bt_next.setOnClickListener(AuthEmailActivity.this);
        bt_send.setOnClickListener(AuthEmailActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_send:
                SendTask sendTask = new SendTask();
                sendTask.execute("sendemail", email.getText().toString());

                break;
            case R.id.bt_ok:
                if(auth.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "인증번호를 입력해주세요..", Toast.LENGTH_SHORT).show();
                }else{
                    SendTask authChckTask = new SendTask();
                    authChckTask.execute("checkauthkey", email.getText().toString());
                }
                break;
            case R.id.bt_next:
                if(flag){
                    Intent intent = new Intent(AuthEmailActivity.this, SettingPWActivity.class);
                    intent.putExtra("email", email.getText().toString());
                    startActivity(intent);
                    finish();
                } else{
                    Toast.makeText(getApplicationContext(), "인증을 먼저 해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_back:
                onBackPressed();
                break;
        }
    }// end click event

    class SendTask extends AsyncTask<String, Integer, String> {

        String server = "http://" + StaticVariable.server_ip + StaticVariable.server_web_port;
        String State;

        @Override protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            // HTTP 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", server+ "/" + params[0] + ".do");
            State = params[0];

            if(params[0].equals("")){
                //메일로 인증번호만 전송
                http.addOrReplace("email", params[1]);
            }else{
                //인증번호 체크
                http.addOrReplace("email", params[1]);
                http.addOrReplace("authkey", auth.getText().toString());
            }


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

            if(s==null){
                Toast.makeText(getApplicationContext(), "네트워크나 서버 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }else{
                Gson gson = new Gson();
                try{
                    MemberInfo result = gson.fromJson(s, MemberInfo.class);

                    if(State.equals("sendemail")){
                        if(result.getResult().equals("Success"))
                            Toast.makeText(getApplicationContext(), "인증번호가 전송되었습니다.", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "이메일을 확인해주세요.", Toast.LENGTH_SHORT).show();

                    }else if(State.equals("checkauthkey")){
                        if(result.getResult().equals("Success")){
                            flag=true;
                            tv_authresult.setText("인증 확인되었습니다.");
                            tv_authresult.setVisibility(View.VISIBLE);
                            tv_authresult.setTextColor(getColor(R.color.base_color));
                        }
                        else{
                            flag=false;
                            tv_authresult.setText("잘못된 번호입니다.");
                            tv_authresult.setVisibility(View.VISIBLE);
                            tv_authresult.setTextColor(getColor(R.color.wrong_auth));
                        }
                    }

                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }




        }
    }

}
