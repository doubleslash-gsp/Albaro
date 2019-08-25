package com.example.jh.albaro.WorkSpace;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.jh.albaro.CalendarActivity;
import com.example.jh.albaro.R;
import com.example.jh.albaro.ServerData.HttpClient;
import com.example.jh.albaro.ServerData.StaticVariable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WorkspaceActivity extends AppCompatActivity implements View.OnClickListener {

    private GridView gridView;
    private Button bt_add, bt_mypage;
    private WorkGridAdapter adapter;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace);

        bt_add = (Button) findViewById(R.id.bt_add);
        bt_mypage = (Button) findViewById(R.id.bt_mypage);
        gridView = (GridView) findViewById(R.id.gridview);

        bt_add.setOnClickListener(this);
        bt_mypage.setOnClickListener(this);

        adapter = new WorkGridAdapter() ;
        gridView.setAdapter(adapter);

        getWorkSpaceList_fucntion();//직장 리스트 가져오기

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectedRow = position;
                adapter.notifyDataSetChanged();
                SharedPreferences sp = getSharedPreferences("auto", Activity.MODE_PRIVATE);

                Intent intent = new Intent(WorkspaceActivity.this, CalendarActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                WorkGridListItem user = (WorkGridListItem)adapter.getItem(position);
                intent.putExtra("store_id", user.getStore_id());
                intent.putExtra("store_name", user.getStore_name());
                intent.putExtra("Is_admin", user.getIs_admin());
                intent.putExtra("color", sp.getString("color", null));
                startActivity(intent);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.showDeleteButton();
                adapter.notifyDataSetChanged();
                return true;
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_add:
                Intent i = new Intent(WorkspaceActivity.this, AddworkActivity.class);
                startActivity(i);
                break;
            case R.id.bt_mypage:
                Toast.makeText(getApplicationContext(), "진희가 만든 마이페이지 넣어주세요.", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    public void getWorkSpaceList_fucntion(){

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        String email = auto.getString("email",null);

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);

        getWorkSpaceList workSpaceList = new getWorkSpaceList();
        workSpaceList.execute(params);

    }

    class getWorkSpaceList extends AsyncTask<Map<String, String>, Integer, String> {

        String server = "http://" + StaticVariable.server_ip + StaticVariable.server_web_port;

        @Override protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // HTTP 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", server+ "/storelist.do");
            http.addAllParameters(maps[0]);

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

            JSONObject result = null;
            if(s!=null){
                try {
                    result = new JSONObject(s);

                    if(result.getString("result").equals("Success")){

                        JSONArray jarray = new JSONObject(s).getJSONArray("stores");
                        for (int i = 0; i < jarray.length(); i++) {

                            JSONObject jObject = jarray.getJSONObject(i);
                            int num = jObject.getInt("num");
                            String store_id = jObject.getString("store_id");
                            String store_name = jObject.getString("store_name");
                            String Is_admin = jObject.getString("Is_admin");

                            adapter.addItem(num, store_id, store_name, Is_admin);
                        }
                        adapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{
                Toast.makeText(getApplicationContext(), "네트워크 혹은 서버 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
