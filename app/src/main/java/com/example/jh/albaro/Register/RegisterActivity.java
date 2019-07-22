package com.example.jh.albaro.Register;

import android.annotation.SuppressLint;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name, email, year, month, day, phone1, phone2, phone3;
    private Button man, female, next;
    private String sex = "man";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.et_name);
        email = (EditText) findViewById(R.id.et_email);
        year = (EditText) findViewById(R.id.et_year);
        month = (EditText) findViewById(R.id.et_month);
        day = (EditText) findViewById(R.id.et_day);
        phone1 = (EditText) findViewById(R.id.et_phone1);
        phone2 = (EditText) findViewById(R.id.et_phone2);
        phone3 = (EditText) findViewById(R.id.et_phone3);
        man = (Button) findViewById(R.id.bt_man);
        female = (Button) findViewById(R.id.bt_female);
        next = (Button) findViewById(R.id.bt_next);

        man.setOnClickListener(RegisterActivity.this);
        female.setOnClickListener(RegisterActivity.this);
        next.setOnClickListener(RegisterActivity.this);



    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_man:
                CheckSex(sex); //남->여
                break;
            case R.id.bt_female:
                CheckSex(sex); //여->남
                break;
            case R.id.bt_next:
                //미입력한 정보 있는지 확인
                if(name.getText().toString().equals("")|email.getText().toString().equals("")|
                        year.getText().toString().equals("")|month.getText().toString().equals("")|day.getText().toString().equals("")|
                        phone1.getText().toString().equals("")|phone2.getText().toString().equals("")|phone3.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();

                }else { //모두 입력했으면 이메일 확인 후 다음 페이지로 이동
                    CheckEmailTask task = new CheckEmailTask();
                    task.execute(email.getText().toString());
                }
                break;
        }

    } // end click event


    class CheckEmailTask extends AsyncTask<String, Integer, String> {

        String server = "http://" + StaticVariable.server_ip + StaticVariable.server_web_port;

        @Override protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            // HTTP 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", server+ "/checkemail.do");

            http.addOrReplace("email", params[0]);

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

                if(result.getResult().equals("has_not_email")){

                    String phone = phone1.getText().toString() + "-" + phone2.getText().toString() + "-" + phone3.getText().toString();

                    Intent intent = new Intent(RegisterActivity.this, RegisterNextActivity.class);

                    intent.putExtra("name",name.getText().toString()); /*송신*/
                    intent.putExtra("email",email.getText().toString());
                    intent.putExtra("year", year.getText().toString());
                    intent.putExtra("month", month.getText().toString());
                    intent.putExtra("day", day.getText().toString());
                    intent.putExtra("sex",sex);
                    intent.putExtra("phone",phone);

                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(), "이미 사용중인 이메일입니다.", Toast.LENGTH_SHORT).show();
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }


        }
    }

    protected void CheckSex(String sex){

        if(sex.equals("man")){
            man.setBackgroundColor(getColor(R.color.white));
            man.setTextColor(getColor(R.color.base_color));

            female.setBackgroundColor(getColor(R.color.base_color));
            female.setTextColor(getColor(R.color.white));

            sex = "female";

        }else if(sex.equals("female")){
            man.setBackgroundColor(getColor(R.color.white));
            man.setTextColor(getColor(R.color.base_color));

            female.setBackgroundColor(getColor(R.color.base_color));
            female.setTextColor(getColor(R.color.white));

            sex = "man";
        }
    } // end CheckSex()


}
