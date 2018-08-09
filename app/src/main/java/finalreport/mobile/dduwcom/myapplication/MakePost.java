package finalreport.mobile.dduwcom.myapplication;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import io.antmedia.android.liveVideoBroadcaster.R;

public class MakePost extends AppCompatActivity implements View.OnClickListener{


    private final int BusikingPostFRAGMENT1 = 1;
    private final int NormPostFRAGMENT2 = 2;


    private Button bt1, bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_post);

        // 위젯에 대한 참조
        bt1 = (Button)findViewById(R.id.buskingpost_btn);
        bt2 = (Button)findViewById(R.id.normpost_btn);

        // 탭 버튼에 대한 리스너 연결
        bt1.setOnClickListener(MakePost.this);
        bt2.setOnClickListener(MakePost.this);
        // 임의로 액티비티 호출 시점에 어느 프레그먼트를 프레임레이아웃에 띄울 것인지를 정함
        callFragment(BusikingPostFRAGMENT1);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buskingpost_btn:
                // '버튼1' 클릭 시 '프래그먼트1' 호출
                callFragment(BusikingPostFRAGMENT1);
                break;

            case R.id.normpost_btn :
                // '버튼2' 클릭 시 '프래그먼트2' 호출
                callFragment(NormPostFRAGMENT2);
                break;
        }

    }
    private void callFragment(int frament_no){

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no){
            case 1:
                // '프래그먼트1' 호출
                PostFragment1 postfragment1 = new PostFragment1();
                transaction.replace(R.id.post_fragment_container, postfragment1);
                transaction.commit();
                break;

            case 2:
                // '프래그먼트2' 호출
                PostFragment2 postfragment2 = new PostFragment2();
                transaction.replace(R.id.post_fragment_container, postfragment2);
                transaction.commit();
                break;
        }

    }
}
