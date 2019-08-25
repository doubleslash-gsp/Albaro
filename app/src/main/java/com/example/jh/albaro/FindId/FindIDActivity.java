package com.example.jh.albaro.FindId;

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

import com.example.jh.albaro.FindPwd.FindPWActivity;
import com.example.jh.albaro.R;
import com.example.jh.albaro.ServerData.HttpClient;
import com.example.jh.albaro.ServerData.MemberInfo;
import com.example.jh.albaro.ServerData.StaticVariable;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class FindIDActivity extends AppCompatActivity {

    private EditText name, year, month, day, phone1, phone2, phone3;
    private Button ok, bt_back;
    private TextView findPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        name = (EditText) findViewById(R.id.et_name);
        year = (EditText) findViewById(R.id.et_year);
        month = (EditText) findViewById(R.id.et_month);
        day = (EditText) findViewById(R.id.et_day);
        phone1 = (EditText) findViewById(R.id.et_phone1);
        phone2 = (EditText) findViewById(R.id.et_phone2);
        phone3 = (EditText) findViewById(R.id.et_phone3);
        ok = (Button) findViewById(R.id.bt_ok);
        bt_back = (Button) findViewById(R.id.bt_back);
        findPwd = (TextView) findViewById(R.id.tv_findPwd);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //미입력한 정보 있는지 확인
                if(name.getText().toString().equals("")|
                        year.getText().toString().equals("")|month.getText().toString().equals("")|day.getText().toString().equals("")|
                        phone1.getText().toString().equals("")|phone2.getText().toString().equals("")|phone3.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();

                }else{ //모두 입력했으면 다음 페이지로 이동
                    String phone = phone1.getText().toString() + "-" + phone2.getText().toString() + "-" + phone3.getText().toString();

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("name", name.getText().toString());
                    params.put("phone", phone);
                    params.put("year", year.getText().toString());
                    params.put("month", month.getText().toString());
                    params.put("day", day.getText().toString());

                    FindIdTask task = new FindIdTask();
                    task.execute(params);
                }


            }
        });

        findPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindIDActivity.this, FindPWActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    class FindIdTask extends AsyncTask<Map<String, String>, Void, String> {

        String server = "http://" + StaticVariable.server_ip + StaticVariable.server_web_port;
        @Override
        protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder
                    ("POST", server+"/findid.do"); //포트번호,서블릿주소

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

                    EmailCheck_Dialog customDialog = new EmailCheck_Dialog(FindIDActivity.this, result.getEmail());
                    customDialog.callFunction();


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
