package finalreport.mobile.dduwcom.myapplication.Mypage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.io.EOFException;

import finalreport.mobile.dduwcom.myapplication.CreatePost.MakePost;
import finalreport.mobile.dduwcom.myapplication.EditProfileActivity;
import finalreport.mobile.dduwcom.myapplication.Models.PostNormal;
import finalreport.mobile.dduwcom.myapplication.Models.UserModel;
import finalreport.mobile.dduwcom.myapplication.Models.Stream;
import io.antmedia.android.liveVideoBroadcaster.LiveVideoBroadcasterActivity;
import io.antmedia.android.liveVideoBroadcaster.R;
import io.antmedia.android.liveVideoPlayer.LiveVideoPlayerActivity;
import io.antmedia.android.liveVideoPlayer.streamPlayerActivity;

    public class MypageActivity extends AppCompatActivity implements View.OnClickListener{


    private final int FRAGMENT1 = 1;
    private final int FRAGMENT2 = 2;
    private final int FRAGMENT3 = 3;

    private Button bt_tab1, bt_tab2, bt_tab3;

    //
    private FirebaseAuth auth;

    private TextView mypage_name,mDescription;
    private TextView mFollowers, mFollowing,  mFollow, mUnfollow;
    private ImageView mUserImage, maddPost;
    private ImageView goHome;
    private TextView editProfile;
    private Button goBroadcast, watchBroadcast;
    private int mFollowersCount = 0;
    private int mFollowingCount = 0;

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
        goBroadcast =(Button)findViewById(R.id.goBroadcast);
        watchBroadcast = (Button)findViewById(R.id.watchBroadcast);

        goHome = findViewById(R.id.gohome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mFollowers = (TextView) findViewById(R.id.tvFollowers);
        mFollowing = (TextView) findViewById(R.id.tvFollowing);
        editProfile  = (TextView) findViewById(R.id.textEditProfile);
        mFollow = (TextView) findViewById(R.id.follow);
        mUnfollow = (TextView) findViewById(R.id.unfollow);
        mUserImage = (ImageView)findViewById(R.id.mypage_user_image);
        maddPost = (ImageView)findViewById(R.id.ic_add_post);
        mDescription = (TextView)findViewById(R.id.description);

        // 탭 버튼에 대한 리스너 연결
        bt_tab1.setOnClickListener(this);
        bt_tab2.setOnClickListener(this);
        bt_tab3.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        mypage_name = (TextView)findViewById(R.id.display_name);
        // 임의로 액티비티 호출 시점에 어느 프레그먼트를 프레임레이아웃에 띄울 것인지를 정함
        callFragment(FRAGMENT1);


        Glide.with(mUserImage.getContext()).load(user.getProfileImageUrl()).into(mUserImage);
        mypage_name.setText(user.getUserName());
        mDescription.setText(user.getDescription());


        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUid())){
            setCurrentUsersProfile();
        }else{
            isFollowing();
        }
        getFollowingCount();
        getFollowersCount();

        mFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("follow", "onClick: now following: " + user.getUserName());

                FirebaseDatabase.getInstance().getReference()
                        .child("following")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(user.getUid())
                        .child("user_id")
                        .setValue(user.getUid());

                FirebaseDatabase.getInstance().getReference()
                        .child("followers")
                        .child(user.getUid())
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("user_id")
                        .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                setFollowing();
            }
        });


        mUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("unfollow", "onClick: now unfollowing: " + user.getUserName());

                FirebaseDatabase.getInstance().getReference()
                        .child("following")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(user.getUid())
                        .removeValue();

                FirebaseDatabase.getInstance().getReference()
                        .child("followers")
                        .child(user.getUid())
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .removeValue();
                setUnfollowing();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, EditProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });


    }

    private void isFollowing() {
        Log.d("istFollowing", "isFollowing: checking if following this users.");
        setUnfollowing();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("following")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderByChild("user_id").equalTo(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d("istFollowing", "onDataChange: found user:" + singleSnapshot.getValue());

                    setFollowing();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setFollowing(){
        Log.d("setfollowing", "setFollowing: updating UI for following this user");
        mFollow.setVisibility(View.GONE);
        mUnfollow.setVisibility(View.VISIBLE);
        editProfile.setVisibility(View.GONE);
        maddPost.setVisibility(View.GONE);
        watchBroadcast.setVisibility(View.VISIBLE);
        goBroadcast.setVisibility(View.GONE);
        getFollowingCount();
        getFollowersCount();
    }

    private void setUnfollowing(){
        Log.d("setunfollowing", "setFollowing: updating UI for unfollowing this user");
        mFollow.setVisibility(View.VISIBLE);
        mUnfollow.setVisibility(View.GONE);
        editProfile.setVisibility(View.GONE);
        maddPost.setVisibility(View.GONE);
        watchBroadcast.setVisibility(View.VISIBLE);
        goBroadcast.setVisibility(View.GONE);
        getFollowingCount();
        getFollowersCount();
    }
    private void setCurrentUsersProfile(){
        Log.d("currentuser", "setFollowing: updating UI for showing this user their own profile");
        mFollow.setVisibility(View.GONE);
        mUnfollow.setVisibility(View.GONE);
        editProfile.setVisibility(View.VISIBLE);
        watchBroadcast.setVisibility(View.GONE);
        goBroadcast.setVisibility(View.VISIBLE);

    }

    private void getFollowersCount(){
        mFollowersCount = 0;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("followers")
                .child(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d("getFollowersCount", "onDataChange: found follower:" + singleSnapshot.getValue());
                    mFollowersCount++;
                }
                mFollowers.setText(String.valueOf(mFollowersCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getFollowingCount(){
        mFollowingCount = 0;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("following")
                .child(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d("getFollowingCount", "onDataChange: found following user:" + singleSnapshot.getValue());
                    mFollowingCount++;
                }
                mFollowing.setText(String.valueOf(mFollowingCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                Intent intent2 =  new Intent(this,  streamPlayerActivity.class);
                final String[] s = new String[1];
                FirebaseDatabase.getInstance().getReference("stream").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                            // child 내에 있는 데이터만큼 반복합니다.
                            Stream stream = (Stream) messageData.getValue(Stream.class);
                            if(!stream.isStreaming) {
                                s[0] = stream.getStreamName();
                            }
                        }
                        dataSnapshot.getKey();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Log.d("뭐지",s[0]);
                try{
                    Log.d("뭐지",s[0]);
                    intent2.putExtra("streamName live=1",s[0]);
                    startActivity(intent2);
                }
                catch (RuntimeException e){
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("방송중이 아닙니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                }
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
