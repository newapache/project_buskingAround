package finalreport.mobile.dduwcom.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.antmedia.android.liveVideoBroadcaster.R;

public class CustomDialog {

    private Context context;

    public CustomDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final String heart_title, String heart_cost) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.activity_custom_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.

        final TextView title = (TextView)dlg.findViewById(R.id.dialog_title);
        final TextView cost = (TextView)dlg.findViewById(R.id.dialog_cost);
        final Button close = (Button)dlg.findViewById(R.id.dialog_close);

        title.setText(heart_title);
        cost.setText(heart_cost);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, heart_title+ "가 충전되었습니다!", Toast.LENGTH_SHORT).show();
                dlg.dismiss();
            }
        });

    }
}
