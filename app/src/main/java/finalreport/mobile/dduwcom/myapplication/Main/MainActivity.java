package finalreport.mobile.dduwcom.myapplication.Main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import finalreport.mobile.dduwcom.myapplication.Map.BuskingMap;
import finalreport.mobile.dduwcom.myapplication.Models.BuskingData;
import finalreport.mobile.dduwcom.myapplication.Models.PostPromote;
import finalreport.mobile.dduwcom.myapplication.Models.Stream;
import finalreport.mobile.dduwcom.myapplication.Models.UserModel;
import finalreport.mobile.dduwcom.myapplication.Mypage.MypageActivity;
import finalreport.mobile.dduwcom.myapplication.SearchActivity;
import io.antmedia.android.liveVideoBroadcaster.R;
import io.antmedia.android.liveVideoPlayer.streamPlayerActivity;

import static io.antmedia.android.LiveMainActivity.RTMP_BASE_URL;
import static io.antmedia.android.liveVideoBroadcaster.R.drawable.ic_heart_white;
import static io.antmedia.android.liveVideoBroadcaster.R.mipmap.ic_launcher;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth auth;

    android.support.v7.widget.Toolbar toolbar;
    private RecyclerView mHorizontalView1;
    private RecyclerView mHorizontalView2;
    private RecyclerView mHorizontalView3;
    private onAirAdapter mAdapter1;
    private HorizontalAdapter mAdapter2;
    private HorizontalAdapter mAdapter3;
    private LinearLayoutManager mLayoutManager1;
    private LinearLayoutManager mLayoutManager2;
    private LinearLayoutManager mLayoutManager3;


    ArrayList<Stream> onAirposts = new ArrayList<Stream>();
    //팔로우포스트
    ArrayList<PostPromote> followingposts = new ArrayList<PostPromote>();

    private int MAX_ITEM_COUNT = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar); //툴바설정
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);//액션바와 같게 만들어줌
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        // RecyclerView binding
        mHorizontalView1 = (RecyclerView) findViewById(R.id.horizon_list1);
        mHorizontalView2 = (RecyclerView) findViewById(R.id.horizon_list2);
        mHorizontalView3 = (RecyclerView) findViewById(R.id.horizon_list3);

        getOnAirPost();
        getFollowingPost();

        mLayoutManager1 = new LinearLayoutManager(this);
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL

        mLayoutManager2 = new LinearLayoutManager(this);
        mLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
//
//        mLayoutManager3 = new LinearLayoutManager(this);
//        mLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);

        // setLayoutManager
        mHorizontalView1.setLayoutManager(mLayoutManager1);

        mHorizontalView2.setLayoutManager(mLayoutManager2);

//        mHorizontalView3.setLayoutManager(mLayoutManager3);

        // init Adapter
        mAdapter1 = new onAirAdapter();
        mAdapter2 = new HorizontalAdapter();
//        mAdapter3 = new HorizontalAdapter();
        // set Data
        mAdapter1.setData(onAirposts);
        mAdapter2.setData(followingposts);
//        mAdapter3.setData(data3);

        // set Adapter
        mHorizontalView1.setAdapter(mAdapter1);
        mHorizontalView2.setAdapter(mAdapter2);
//        mHorizontalView3.setAdapter(mAdapter3);

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




    public void getFollowingPost(){

        final ArrayList<String> FollowingUids = new ArrayList<>();
        //팔로우한 유저의 uid리스트 뽑아오기
        FirebaseDatabase.getInstance().getReference("following").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                FollowingUids.clear();
                followingposts.clear();
                for (DataSnapshot messageData1 : dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.
                    Log.d("following", String.valueOf(messageData1.child("user_id").getValue()));
                    FollowingUids.add(String.valueOf(messageData1.child("user_id").getValue()));
                    //uid들의 홍보글 뽑아오기
                    FirebaseDatabase.getInstance().getReference("post_promote").orderByChild("postPrmt_uid").equalTo(String.valueOf(messageData1.child("user_id").getValue())).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot messageData2 : dataSnapshot.getChildren()) {
                                // child 내에 있는 데이터만큼 반복합니다.
                                PostPromote postprtm = (PostPromote) messageData2.getValue(PostPromote.class);
                                Log.d("post", String.valueOf((PostPromote) messageData2.getValue(PostPromote.class)));
                                followingposts.add(postprtm);
                                mAdapter2.notifyDataSetChanged();
                            }


                            dataSnapshot.getKey();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getOnAirPost(){
        FirebaseDatabase.getInstance().getReference("stream").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onAirposts.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.
                    Log.d("빡친다", messageData.getKey());

                  for(DataSnapshot data : messageData.getChildren()) {
                        Stream stream = data.getValue(Stream.class);
                        if(stream.isStreaming){
                            Log.d("Stream", stream.streamName);
                            onAirposts.add(stream);
                            mAdapter1.notifyDataSetChanged();
                        }

                    }
                }
                dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public class onAirAdapter extends RecyclerView.Adapter<onAirViewHolder> {
        private ArrayList<Stream> horizontalData;

        public void setData(ArrayList<Stream> list){
            horizontalData = list;
        }

        @Override
        public onAirViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

// 사용할 아이템의 뷰를 생성해준다.
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.onair_item, parent, false);

            onAirViewHolder holder = new onAirViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(final onAirViewHolder holder, int position) {
            final Stream data = horizontalData.get(position);

            holder.title.setText(data.getStreamName());

            FirebaseDatabase.getInstance().getReference().child("users").orderByChild("uid").equalTo(data.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                        // child 내에 있는 데이터만큼 반복합니다.
                        Log.d("빡친다", messageData.getKey());

                        Glide.with(holder.icon.getContext()).load(messageData.getValue(UserModel.class).profileImageUrl).into((holder).icon);



                    }
                    dataSnapshot.getKey();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =  new Intent(MainActivity.this , streamPlayerActivity.class);
                    intent.putExtra("streamName", data.getStreamName()+ " live=1");
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return horizontalData.size();
        }

    }


//    public String getImage(String uid){
//        final String[] s = new String[1];
//        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
//                    // child 내에 있는 데이터만큼 반복합니다.
//                    Log.d("빡친다", messageData.getKey());
//
//                    UserModel user = messageData.getValue(UserModel.class);
//                    s[0] = user.getProfileImageUrl();
//
//                }
//                dataSnapshot.getKey();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        return s[0];
//    }

//    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
//            throws Throwable {
//        Bitmap bitmap = null;
//        MediaMetadataRetriever mediaMetadataRetriever = null;
//        try {
//            mediaMetadataRetriever = new MediaMetadataRetriever();
//            if (Build.VERSION.SDK_INT >= 14)
//                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
//            else
//                mediaMetadataRetriever.setDataSource(videoPath);
//
//            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Throwable(
//                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
//                            + e.getMessage());
//
//        } finally {
//            if (mediaMetadataRetriever != null) {
//                mediaMetadataRetriever.release();
//            }
//        }
//        return bitmap;
//    }

    public class onAirViewHolder extends RecyclerView.ViewHolder{


        public ImageView icon;
        public TextView title;

        public onAirViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView)itemView.findViewById(R.id.stream_icon);
            title = (TextView) itemView.findViewById(R.id.stream_title);
        }

    }





    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalViewHolder> {
        private ArrayList<PostPromote> horizontalData;

        public void setData(ArrayList<PostPromote> list){
            horizontalData = list;
        }

        @Override
        public HorizontalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

// 사용할 아이템의 뷰를 생성해준다.
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item, parent, false);

            HorizontalViewHolder holder = new HorizontalViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(HorizontalViewHolder holder, int position) {
            PostPromote data = horizontalData.get(position);
            holder.title.setText(data.getPostPrmt_busking_title());
            Glide.with(holder.icon.getContext()).load(data.getPostPrmt_imageUrl()).into((holder).icon);

        }

        @Override
        public int getItemCount() {
            return horizontalData.size();
        }

    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder{


        public ImageView icon;
        public TextView title;

        public HorizontalViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView)itemView.findViewById(R.id.busking_icon);
            title = (TextView) itemView.findViewById(R.id.busking_title);
        }

    }



}
