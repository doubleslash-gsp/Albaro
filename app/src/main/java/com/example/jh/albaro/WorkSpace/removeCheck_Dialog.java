package com.example.jh.albaro.WorkSpace;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.jh.albaro.R;
import com.example.jh.albaro.ServerData.HttpClient;
import com.example.jh.albaro.ServerData.StaticVariable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class removeCheck_Dialog {

    private int num, position;
    private Context context;
    private Button bt_ok, bt_cancel;
    private Dialog dlg;

    private CustomDialogListener customDialogListener;


    public removeCheck_Dialog(Context context, int num) {
        this.context = context;
        this.num = num;
    }

    //인터페이스 설정
    interface CustomDialogListener{
        void onPositiveClicked(int result);
    }

    //호출할 리스너 초기화
    public void setDialogListener(CustomDialogListener customDialogListener){
        this.customDialogListener = customDialogListener;
    }





    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.removecheck_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        bt_ok = (Button) dlg.findViewById(R.id.bt_ok);
        bt_cancel = (Button) dlg.findViewById(R.id.bt_cancel);


        bt_ok.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                bt_ok.setBackgroundResource(R.color.base_color);
                bt_ok.setTextColor(R.color.white);
                bt_cancel.setBackgroundResource(R.drawable.rectangle_stroke_basecolor);
                bt_ok.setTextColor(R.color.base_color);

                removeStore remove = new removeStore();
                //remove.execute(Integer.toString(num));

                try {
                    String ret = remove.execute(Integer.toString(num)).get();

                    if(ret!=null){
                        JSONObject result = null;
                        try{
                            result = new JSONObject(ret);

                            if(result.getString("result").equals("Success")){
                                Log.i("result", "succuess");
                                customDialogListener.onPositiveClicked(1);
                                dlg.dismiss();
                            }else{
                                Log.i("result", "fail from server");

                            }

                        }catch (NullPointerException e){
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Log.i("result", "connection fail");
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                bt_cancel.setBackgroundResource(R.color.base_color);
                bt_cancel.setTextColor(R.color.white);
                bt_ok.setBackgroundResource(R.drawable.rectangle_stroke_basecolor);
                bt_ok.setTextColor(R.color.base_color);

                dlg.dismiss();
            }
        });

    }



    class removeStore extends AsyncTask<String, Integer, String> {

        String server = "http://" + StaticVariable.server_ip + StaticVariable.server_web_port;

        @Override protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            // HTTP 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", server+ "/removeStore.do");

            http.addOrReplace("num", params[0]);

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


        }
    }
}
