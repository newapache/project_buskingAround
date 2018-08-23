package finalreport.mobile.dduwcom.myapplication.Main;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import finalreport.mobile.dduwcom.myapplication.Map.BuskingMap;
import finalreport.mobile.dduwcom.myapplication.Models.BuskingData;
import finalreport.mobile.dduwcom.myapplication.Models.UserModel;
import finalreport.mobile.dduwcom.myapplication.Mypage.MypageActivity;
import finalreport.mobile.dduwcom.myapplication.SearchActivity;
import io.antmedia.android.liveVideoBroadcaster.R;

public class MainActivity extends AppCompatActivity {


    android.support.v7.widget.Toolbar toolbar;
    private RecyclerView mHorizontalView1;
    private RecyclerView mHorizontalView2;
    private RecyclerView mHorizontalView3;
    private HorizontalAdapter mAdapter1;
    private HorizontalAdapter mAdapter2;
    private HorizontalAdapter mAdapter3;
    private LinearLayoutManager mLayoutManager1;
    private LinearLayoutManager mLayoutManager2;
    private LinearLayoutManager mLayoutManager3;

    private int MAX_ITEM_COUNT = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar); //툴바설정
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);//액션바와 같게 만들어줌
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        // RecyclerView binding
        mHorizontalView1 = (RecyclerView) findViewById(R.id.horizon_list1);
        mHorizontalView2 = (RecyclerView) findViewById(R.id.horizon_list2);
        mHorizontalView3 = (RecyclerView) findViewById(R.id.horizon_list3);

        // init Data
        ArrayList<BuskingData> data1 = new ArrayList<>();
        ArrayList<BuskingData> data2 = new ArrayList<>();
        ArrayList<BuskingData> data3 = new ArrayList<>();

        int i = 0;
        while (i < MAX_ITEM_COUNT) {
            data1.add(new BuskingData(R.mipmap.ic_launcher, "위치 "+i+"번째 데이터"));
            data2.add(new BuskingData(R.mipmap.ic_launcher, "좋아요 "+i+"번째 데이터"));
            data3.add(new BuskingData(R.mipmap.ic_launcher, "선호도 "+i+"번째 데이터"));
            i++;
        }


        // init LayoutManager
        mLayoutManager1 = new LinearLayoutManager(this);
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL

        mLayoutManager2 = new LinearLayoutManager(this);
        mLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);

        mLayoutManager3 = new LinearLayoutManager(this);
        mLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);

        // setLayoutManager
        mHorizontalView1.setLayoutManager(mLayoutManager1);

        mHorizontalView2.setLayoutManager(mLayoutManager2);

        mHorizontalView3.setLayoutManager(mLayoutManager3);

        // init Adapter
        mAdapter1 = new HorizontalAdapter();
        mAdapter2 = new HorizontalAdapter();
        mAdapter3 = new HorizontalAdapter();
        // set Data
        mAdapter1.setData(data1);
        mAdapter2.setData(data2);
        mAdapter3.setData(data3);

        // set Adapter
        mHorizontalView1.setAdapter(mAdapter1);
        mHorizontalView2.setAdapter(mAdapter2);
        mHorizontalView3.setAdapter(mAdapter3);

    }

    public void onclick(View v){

        switch(v.getId()){
            case R.id.busking_map:
                final Intent intent =  new Intent(this, BuskingMap.class);
                startActivity(intent);
                break;
            case  R.id.gomypage:
                final Intent intent2 = new Intent(this, MypageActivity.class);
               FirebaseDatabase.getInstance().getReference("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        intent2.putExtra("user", dataSnapshot.getValue(UserModel.class));
                        startActivity(intent2);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            case R.id.gosearchActivity:
                Intent intent3 =  new Intent(this, SearchActivity.class);
                startActivity(intent3);
                break;


        }

    }

}
