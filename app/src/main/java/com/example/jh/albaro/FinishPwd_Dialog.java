package com.example.jh.albaro;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class FinishPwd_Dialog {

    private Context context;
    private Button bt_ok;
    private TextView ment1, ment2,ment3, tv_email;

    public FinishPwd_Dialog(Context context) {
        this.context = context;

    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.emailcheck_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        bt_ok = (Button) dlg.findViewById(R.id.bt_ok);
        ment1 = (TextView) dlg.findViewById(R.id.tv_ment1);
        ment2 = (TextView) dlg.findViewById(R.id.tv_ment2);
        ment3 = (TextView) dlg.findViewById(R.id.tv_reset_ok);
        tv_email = (TextView) dlg.findViewById(R.id.tv_email);

        tv_email.setVisibility(View.GONE);
        ment1.setText("비밀번호가");
        ment2.setText("변경되었습니다.");
        ment3.setVisibility(View.VISIBLE);


        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });

    }
}
