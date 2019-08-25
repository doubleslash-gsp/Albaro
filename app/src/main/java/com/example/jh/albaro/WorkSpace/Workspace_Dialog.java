package com.example.jh.albaro.WorkSpace;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jh.albaro.R;

public class Workspace_Dialog extends AppCompatActivity {

    private Context context;
    private ImageView iv_question;
    private TextView title, ment1, ment2;
    private Button bt_ok;
    private String state, storename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workspace_dialog);
    }

    public Workspace_Dialog(Context context, String state, String storename) {
        this.context = context;
        this.state = state;
        this.storename = storename;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.workspace_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        iv_question = (ImageView) dlg.findViewById(R.id.iv_question);
        title = (TextView) dlg.findViewById(R.id.tv_title);
        ment1 = (TextView) dlg.findViewById(R.id.tv_ment1);
        ment2 = (TextView) dlg.findViewById(R.id.tv_ment2);
        bt_ok = (Button) dlg.findViewById(R.id.bt_ok);

        if(state.equals("finish")){
            iv_question.setVisibility(View.GONE);
            title.setText(storename);
            ment1.setText("직장이 추가되었습니다.");
            ment2.setVisibility(View.GONE);
        }


        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(state.equals("finish")){
                    Intent i = new Intent(context, WorkspaceActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                    dlg.dismiss();
                }else{
                    dlg.dismiss();
                }

            }
        });

    }
}
