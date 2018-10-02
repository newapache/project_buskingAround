package finalreport.mobile.dduwcom.myapplication;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import io.antmedia.android.liveVideoBroadcaster.R;

public class BuyHeartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_heart);
    }
    public void buyHeart(View v){
        String title = null;
        String cost = null;

        switch (v.getId()){
            case R.id.heart10:
                title = "크레딧 10개";
                cost = "₩100";
                break;
            case R.id.heart50:
                title = "크레딧 50개";
                cost = "₩500";
                break;
            case R.id.heart100:
                title = "크레딧 100개";
                cost = "₩1,000";
                break;
            case R.id.heart500:
                title = "크레딧 500개";
                cost = "₩5,000";
                break;
            case R.id.heart1000:
                title = "크레딧 1000개";
                cost = "₩10,000";
                break;
        }



//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(title)        // 제목 설정
//                .setMessage("\n"+ cost + "\n\n테스트 주문이므로 청구되지 않습니다.")        // 메세지 설정
//                .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
//                .setPositiveButton("충전하기", new DialogInterface.OnClickListener(){
//                    // 확인 버튼 클릭시 설정
//                    public void onClick(DialogInterface dialog, int whichButton){
//                        dialog.cancel();
//                    }
//
//                })
//                .setNegativeButton("취소", new DialogInterface.OnClickListener(){
//                    public void onClick(DialogInterface dialog, int whichButton){
//
//                        dialog.cancel();
//
//                    }
//
//                });
//        AlertDialog dialog = builder.create();    // 알림창 객체 생성
//        dialog.show();

        CustomDialog customDialog = new CustomDialog(BuyHeartActivity.this);
        customDialog.callFunction(title, cost);
    }
}
