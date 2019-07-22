package com.example.jh.albaro.Register;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;

public class RegisterNextActivity extends AppCompatActivity {

    private TextView email;
    private EditText passwd, repasswd;
    private Button next;
    private Intent intent;
    private String getname, getemail, getsex, getphone, getyear, getmonth, getday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_next);

        email = (TextView) findViewById(R.id.tv_email);
        passwd = (EditText) findViewById(R.id.et_passwd);
        repasswd = (EditText) findViewById(R.id.et_repasswd);
        next = (Button) findViewById(R.id.bt_next);

        intent = getIntent();
        getname = intent.getExtras().getString("name");
        getemail = intent.getExtras().getString("email");
        getsex = intent.getExtras().getString("sex");
        getphone = intent.getExtras().getString("phone");
        getyear = intent.getExtras().getString("year");
        getmonth = intent.getExtras().getString("month");
        getday = intent.getExtras().getString("day");

        email.setText(getemail);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(passwd.getText().toString().equals("") | repasswd.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{

                    if(passwd.getText().toString().equals(repasswd.getText().toString())){
                        insertTask task = new insertTask();
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("name", getname);
                        params.put("email", getemail);
                        params.put("sex", getsex);
                        params.put("phone", getphone);
                        params.put("year", getyear);
                        params.put("month", getmonth);
                        params.put("day", getday);
                        params.put("password", passwd.getText().toString());


                        Log.i("error_check", "\nname: "+getname + "\nemail: " + getemail);

                        task.execute(params);

                    }else {
                        Toast.makeText(getApplicationContext(), "동일한 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }


    public class insertTask extends AsyncTask<Map<String, String>, Void, String> {

        String server = "http://" + StaticVariable.server_ip + StaticVariable.server_web_port;
        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder
                    ("POST", server+"/join.do"); //포트번호,서블릿주소

            // Parameter 를 전송한다.
            http.addAllParameters(maps[0]);
            Log.i("error_check", "params: " + maps[0]);

            //Http 요청 전송
            HttpClient post = http.create();
            post.request();
            Log.i("httpresult", "post: "+post);

            // 응답 상태코드 가져오기
            int statusCode = post.getHttpStatusCode();

            Log.i("httpresult", "code: "+statusCode);

            // 응답 본문 가져오기
            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String s) { //서블릿으로부터 값을 받을 함수

            Gson gson = new Gson();
            MemberInfo result = gson.fromJson(s, MemberInfo.class);

            if(result.getResult().equals("Success")){
                Intent intent = new Intent(getApplicationContext(), RegisterFinishActivity
                        .class);
                intent.putExtra("email", email.getText().toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }

        }
    }

}
