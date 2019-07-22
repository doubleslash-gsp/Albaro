package com.example.jh.albaro;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class EmailCheck_Dialog{

    private Context context;
    private String email;
    private Button bt_ok;
    private TextView tv_email;

    public EmailCheck_Dialog(Context context, String email) {
        this.context = context;
        this.email = email;
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
        tv_email = (TextView) dlg.findViewById(R.id.tv_email);
        tv_email.setText(email);


        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });

    }
}
