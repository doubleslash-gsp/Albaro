package com.example.jh.albaro.FindPwd;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindPWActivity extends AppCompatActivity {

    private EditText name, email, phone1, phone2, phone3;
    private Button next, bt_back;
    private TextView findId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        name = (EditText) findViewById(R.id.et_name);
        email = (EditText) findViewById(R.id.et_mail);
        phone1 = (EditText) findViewById(R.id.et_phone1);
        phone2 = (EditText) findViewById(R.id.et_phone2);
        phone3 = (EditText) findViewById(R.id.et_phone3);
        next = (Button) findViewById(R.id.bt_next);
        bt_back = (Button) findViewById(R.id.bt_back);
        findId = (TextView) findViewById(R.id.tv_find_id);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //미입력한 정보 있는지 확인
                if(name.getText().toString().equals("")|email.getText().toString().equals("")|
                        phone1.getText().toString().equals("")|phone2.getText().toString().equals("")|phone3.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();

                }else{ //모두 입력했으면 다음 페이지로 이동

                    if(isEmailValid(email.getText().toString())){ //이메일 형식 검사
                        String phone = phone1.getText().toString() + "-" + phone2.getText().toString() + "-" + phone3.getText().toString();

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("name", name.getText().toString());
                        params.put("phone", phone);
                        params.put("email", email.getText().toString());

                        FindPwdTask task = new FindPwdTask();
                        task.execute(params);
                    }

                }


            }
        });


        findId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindPWActivity.this, AuthEmailActivity.class);
                startActivity(intent);
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

    class FindPwdTask extends AsyncTask<Map<String, String>, Void, String> {

        String server = "http://" + StaticVariable.server_ip + StaticVariable.server_web_port;
        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder
                    ("POST", server+"/findpwd.do"); //포트번호,서블릿주소

            // Parameter 를 전송한다.
            http.addAllParameters(maps[0]);

            //Http 요청 전송
            HttpClient post = http.create();
            post.request();

            // 응답 상태코드 가져오기
            int statusCode = post.getHttpStatusCode();

            // 응답 본문 가져오기
            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String s) { //서블릿으로부터 값을 받을 함수

            Gson gson = new Gson();
            try{
                MemberInfo result = gson.fromJson(s, MemberInfo.class);

                if(result.getResult().equals("Success")){
                    Intent intent = new Intent(FindPWActivity.this, AuthEmailActivity.class);
                    intent.putExtra("email", email.getText().toString());
                    startActivity(intent);
                    finish();
                } else{
                    Toast.makeText(getApplicationContext(), "사용자 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }catch (NullPointerException e){
                Toast.makeText(getApplicationContext(), "네트워크 혹은 서버 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }catch (Exception e ){
                e.printStackTrace();
            }


        }
    }

}
