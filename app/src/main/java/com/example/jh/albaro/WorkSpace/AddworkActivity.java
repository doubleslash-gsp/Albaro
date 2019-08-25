package com.example.jh.albaro.WorkSpace;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jh.albaro.R;
import com.example.jh.albaro.ServerData.HttpClient;
import com.example.jh.albaro.ServerData.StaticVariable;

import org.json.JSONException;
import org.json.JSONObject;

public class AddworkActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton ib_question;
    private EditText et_storename, et_storeid;
    private TextView tv_result;
    private Button bt_ok, bt_idcheck, bt_back;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addwork);

        et_storename = (EditText) findViewById(R.id.et_storename);
        et_storeid = (EditText) findViewById(R.id.et_storeid);
        tv_result = (TextView) findViewById(R.id.tv_result);

        ib_question = (ImageButton) findViewById(R.id.ib_question);
        bt_ok = (Button) findViewById(R.id.bt_ok);
        bt_idcheck = (Button) findViewById(R.id.bt_idcheck);
        bt_back = (Button) findViewById(R.id.bt_back);

        ib_question.setOnClickListener(this);
        bt_ok.setOnClickListener(this);
        bt_idcheck.setOnClickListener(this);
        bt_back.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ib_question:
                Workspace_Dialog dialog = new Workspace_Dialog(AddworkActivity.this, "question", null);
                dialog.callFunction();
                break;
            case R.id.bt_idcheck:
                if(et_storeid.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "직장 고유 아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    storeTask checkid = new storeTask();
                    checkid.execute("checkStoreId", et_storeid.getText().toString());
                }
                break;
            case R.id.bt_ok:
                if(et_storename.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "직장명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    if(flag){
                        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                        String email = auto.getString("email",null);

                        storeTask saveStore = new storeTask();
                        saveStore.execute("saveStore", et_storeid.getText().toString(), et_storename.getText().toString(), email);
                    } else{
                        Toast.makeText(getApplicationContext(), "중복체크를 먼저 진행해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }


                break;
            case R.id.bt_back:
                onBackPressed();
                break;
        }

    }


    class storeTask extends AsyncTask<String, Integer, String> {

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

            if (params[0].equals("checkStoreId")){
                //직장 고유 아이디 체크하기
                http.addOrReplace("store_id", params[1]);
            }else if(params[0].equals("saveStore")){
                //직장 추가
                http.addOrReplace("store_id", params[1]);
                http.addOrReplace("store_name", params[2]);
                http.addOrReplace("email", params[3]);
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
                JSONObject result = null;
                try{
                    result = new JSONObject(s);
                    if(State.equals("saveStore")){
                        if(result.getString("result").equals("Success")){
                            Workspace_Dialog dialog = new Workspace_Dialog(AddworkActivity.this, "finish", et_storename.getText().toString());
                            dialog.callFunction();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "서버 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();

                    }else if(State.equals("checkStoreId")){
                        if(result.getString("result").equals("Success")){
                            flag=true;
                            tv_result.setText("사용 가능한 아이디입니다.");
                            tv_result.setVisibility(View.VISIBLE);
                            tv_result.setTextColor(getColor(R.color.base_color));
                        }
                        else{
                            flag=false;
                            tv_result.setText("사용 불가능한 아이디입니다.");
                            tv_result.setVisibility(View.VISIBLE);
                            tv_result.setTextColor(getColor(R.color.wrong_auth));
                        }
                    }

                }catch (NullPointerException e){
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }

}
