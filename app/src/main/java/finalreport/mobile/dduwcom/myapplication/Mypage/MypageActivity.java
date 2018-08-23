package finalreport.mobile.dduwcom.myapplication.Mypage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import finalreport.mobile.dduwcom.myapplication.CreatePost.MakePost;
import finalreport.mobile.dduwcom.myapplication.Models.PostNormal;
import finalreport.mobile.dduwcom.myapplication.Models.UserModel;
import io.antmedia.android.liveVideoBroadcaster.LiveVideoBroadcasterActivity;
import io.antmedia.android.liveVideoBroadcaster.R;
import io.antmedia.android.liveVideoPlayer.LiveVideoPlayerActivity;

public class MypageActivity extends AppCompatActivity implements View.OnClickListener{


    private final int FRAGMENT1 = 1;
    private final int FRAGMENT2 = 2;
    private final int FRAGMENT3 = 3;

    private Button bt_tab1, bt_tab2, bt_tab3;

    //
    private FirebaseAuth auth;
    private TextView mypage_name;
    private TextView mFollowers, mFollowing,  mFollow, mUnfollow;
    private ImageView mUserImage;

    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        Intent intent = getIntent();
        user = (UserModel) intent.getSerializableExtra("user");

        // 위젯에 대한 참조
        bt_tab1 = (Button)findViewById(R.id.bt_tab1);
        bt_tab2 = (Button)findViewById(R.id.bt_tab2);
        bt_tab3 = (Button)findViewById(R.id.bt_tab3);

        mFollowers = (TextView) findViewById(R.id.tvFollowers);
        mFollowing = (TextView) findViewById(R.id.tvFollowing);
        mFollow = (TextView) findViewById(R.id.follow);
        mUnfollow = (TextView) findViewById(R.id.unfollow);
        mUserImage = (ImageView)findViewById(R.id.mypage_user_image);


        // 탭 버튼에 대한 리스너 연결
        bt_tab1.setOnClickListener(this);
        bt_tab2.setOnClickListener(this);
        bt_tab3.setOnClickListener(this);
        // 임의로 액티비티 호출 시점에 어느 프레그먼트를 프레임레이아웃에 띄울 것인지를 정함
        callFragment(FRAGMENT1);

        auth = FirebaseAuth.getInstance();
        mypage_name = (TextView)findViewById(R.id.display_name);

        Glide.with(mUserImage.getContext()).load(user.getProfileImageUrl()).into(mUserImage);
        mypage_name.setText(user.getUserName());

//        FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                    UserModel user= (UserModel) dataSnapshot.getValue(UserModel.class);
//                Glide.with(mUserImage.getContext()).load(user.getProfileImageUrl()).into(mUserImage);
//                mypage_name.setText(user.getUserName());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//
//        mFollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: now following: " + mUser.getUsername());
//
//                FirebaseDatabase.getInstance().getReference()
//                        .child(getString(R.string.dbname_following))
//                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                        .child(mUser.getUser_id())
//                        .child(getString(R.string.field_user_id))
//                        .setValue(mUser.getUser_id());
//
//                FirebaseDatabase.getInstance().getReference()
//                        .child(getString(R.string.dbname_followers))
//                        .child(mUser.getUser_id())
//                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                        .child(getString(R.string.field_user_id))
//                        .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                setFollowing();
//            }
//        });
//
//
//        mUnfollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: now unfollowing: " + mUser.getUsername());
//
//                FirebaseDatabase.getInstance().getReference()
//                        .child(getString(R.string.dbname_following))
//                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                        .child(mUser.getUser_id())
//                        .removeValue();
//
//                FirebaseDatabase.getInstance().getReference()
//                        .child(getString(R.string.dbname_followers))
//                        .child(mUser.getUser_id())
//                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                        .removeValue();
//                setUnfollowing();
//            }
//        });

    }











    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_tab1 :
                // '버튼1' 클릭 시 '프래그먼트1' 호출
                callFragment(FRAGMENT1);
                break;

            case R.id.bt_tab2 :
                // '버튼2' 클릭 시 '프래그먼트2' 호출
                callFragment(FRAGMENT2);
                break;

            case R.id.bt_tab3 :
                // '버튼3' 클릭 시 '프래그먼트3' 호출
                callFragment(FRAGMENT3);
                break;
        }

    }
    public void onAir(View v){
        switch (v.getId()){
            case R.id.goBroadcast:
                Intent intent1 =  new Intent(this,  LiveVideoBroadcasterActivity.class);
                startActivity(intent1);
                break;
            case R.id.watchBroadcast:
                Intent intent2 =  new Intent(this,  LiveVideoPlayerActivity.class);
                startActivity(intent2);
                break;
            case R.id.ic_add_post :
                Intent intent3 = new Intent(this, MakePost.class);
                startActivity(intent3);
                break;
        }
    }
    private void callFragment(int frament_no){

        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no){
            case 1:
                // '프래그먼트1' 호출
                Fragment1 fragment1 = new Fragment1();
                fragment1.setArguments(bundle);
                transaction.replace(R.id.fragment_container, fragment1);
                transaction.commit();
                break;

            case 2:
                // '프래그먼트2' 호출
                Fragment2 fragment2 = new Fragment2();
                fragment2.setArguments(bundle);
                transaction.replace(R.id.fragment_container, fragment2);
                transaction.commit();
                break;
            case 3:
                // '프래그먼트2' 호출
                Fragment3 fragment3 = new Fragment3();
                fragment3.setArguments(bundle);
                transaction.replace(R.id.fragment_container, fragment3);
                transaction.commit();
                break;
        }

    }
}
