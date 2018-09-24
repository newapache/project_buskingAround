package finalreport.mobile.dduwcom.myapplication.Main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import finalreport.mobile.dduwcom.myapplication.Map.BuskingMap;
import finalreport.mobile.dduwcom.myapplication.Models.BuskingData;
import finalreport.mobile.dduwcom.myapplication.Models.PostPromote;
import finalreport.mobile.dduwcom.myapplication.Models.Stream;
import finalreport.mobile.dduwcom.myapplication.Models.UserModel;
import finalreport.mobile.dduwcom.myapplication.Mypage.MypageActivity;
import finalreport.mobile.dduwcom.myapplication.ReadPost.PrmtPostDetailActivity;
import finalreport.mobile.dduwcom.myapplication.SearchActivity;
import io.antmedia.android.liveVideoBroadcaster.R;
import io.antmedia.android.liveVideoPlayer.streamPlayerActivity;

import static io.antmedia.android.LiveMainActivity.RTMP_BASE_URL;
import static io.antmedia.android.liveVideoBroadcaster.R.drawable.ic_heart_white;
import static io.antmedia.android.liveVideoBroadcaster.R.mipmap.ic_launcher;

public class MainActivity extends AppCompatActivity implements LocationListener {


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
    //거리포스트
    ArrayList<PostPromote> nearbyposts = new ArrayList<PostPromote>();

    LocationManager locationManager;
    String bestProvider;

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
        getNearbyPost();

        mLayoutManager1 = new LinearLayoutManager(this);
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
        mLayoutManager1.setReverseLayout(true);
        mLayoutManager1.setStackFromEnd(true);

        mLayoutManager2 = new LinearLayoutManager(this);
        mLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mLayoutManager2.setReverseLayout(true);
        mLayoutManager2.setStackFromEnd(true);

        mLayoutManager3 = new LinearLayoutManager(this);
        mLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);

        // setLayoutManager
        mHorizontalView1.setLayoutManager(mLayoutManager1);
        mHorizontalView2.setLayoutManager(mLayoutManager2);
        mHorizontalView3.setLayoutManager(mLayoutManager3);

        // init Adapter
        mAdapter1 = new onAirAdapter();
        mAdapter2 = new HorizontalAdapter();
        mAdapter3 = new HorizontalAdapter();
        // set Data
        mAdapter1.setData(onAirposts);
        mAdapter2.setData(followingposts);
        mAdapter3.setData(nearbyposts);

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

    public void getNearbyPost() {
        FirebaseDatabase.getInstance().getReference("post_promote").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nearbyposts.clear();

                Log.d("getNearbyPost: ",  "");
                LatLng currentPosition = getCurrentPosition();
                Location mCurrentLocation = new Location(LocationManager.GPS_PROVIDER);
                mCurrentLocation.setLatitude(currentPosition.latitude);
                mCurrentLocation.setLongitude(currentPosition.longitude);

                Log.d("getNearbyPost: ", "lat=" + mCurrentLocation.getLatitude() + "lon=" + mCurrentLocation.getLongitude());

                for (DataSnapshot postPrmtData : dataSnapshot.getChildren()) {

                    PostPromote postPrmt = postPrmtData.getValue(PostPromote.class);

                    Location mBuskingLocation = new Location(LocationManager.GPS_PROVIDER);
                    mBuskingLocation.setLatitude(postPrmt.postPrmt_busking_latitude);
                    mBuskingLocation.setLongitude(postPrmt.postPrmt_busking_longitude);
                    LatLng buskingPosition = new LatLng(postPrmt.postPrmt_busking_latitude, postPrmt.postPrmt_busking_longitude);

                    PolylineOptions dist = new PolylineOptions()
                            .add(currentPosition, buskingPosition);
                    postPrmt.setPostPrmt_distance(mCurrentLocation.distanceTo(mBuskingLocation));

                    nearbyposts.add(postPrmt);

                    Log.d("getNearbyPost: ", postPrmt.postPrmt_title + " : " + postPrmt.postPrmt_distance);
                }
                mAdapter3.notifyDataSetChanged();

                Log.d("getNearbyPost: ", "distSort");
                Comparator<PostPromote> distSort = new Comparator<PostPromote>() {
                    @Override
                    public int compare(PostPromote p1, PostPromote p2) {
                        if (p1.postPrmt_distance < p2.postPrmt_distance)
                            return -1;
                        else if (p1.postPrmt_distance == p2.postPrmt_distance)
                            return 0;
                        else
                            return 1;
                    }
                };
                Collections.sort(nearbyposts, distSort);
                mAdapter3.notifyDataSetChanged();

                for (PostPromote postPrmtList : nearbyposts) {
                    Log.d("getNearbyPost: ", postPrmtList.postPrmt_title + " : " + postPrmtList.postPrmt_distance);
                }

                onStop();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public LatLng getCurrentPosition()
    {
        Log.d("getCurPos: ", "");
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        // Get the location manager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        bestProvider = locationManager.getBestProvider(criteria, true);
        if (bestProvider == null) {
            Log.e( "getCurPos: ", "No location provider found!" );
        }
        Location location = locationManager.getLastKnownLocation(bestProvider);
        Double lat,lon;
        try {
            lat = location.getLatitude ();
            lon = location.getLongitude ();
            Log.d("getCurPos: ", "curLat=" + lat + " curLon=" + lon);
            return new LatLng(lat, lon);
        }
        catch (NullPointerException e){
            e.printStackTrace();
            Log.d("getCurPos: ", "error");
            return null;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

        int latitude = (int) (location.getLatitude());
        int longitude = (int) (location.getLongitude());

        Log.i("Geo_Location", "Latitude: " + latitude + ", Longitude: " + longitude);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    public void getOnAirPost(){
        FirebaseDatabase.getInstance().getReference("stream").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onAirposts.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.

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

                        Glide.with(holder.icon.getContext()).load(messageData.getValue(UserModel.class).profileImageUrl).into((holder).icon);
                        holder.busker.setText(messageData.getValue(UserModel.class).getUserName());


                    }
                    dataSnapshot.getKey();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            try{
                holder.icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent =  new Intent(MainActivity.this , streamPlayerActivity.class);
                        intent.putExtra("streamName", data.getStreamName()+ " live=1");
                        startActivity(intent);
                    }
                });}catch (Exception e){
                Toast.makeText(MainActivity.this, "방송연결중 오류가 났습니다.", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public int getItemCount() {
            return horizontalData.size();
        }

    }


    public class onAirViewHolder extends RecyclerView.ViewHolder{


        public ImageView icon;
        public TextView title;
        public TextView busker;
        public onAirViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView)itemView.findViewById(R.id.stream_icon);
            title = (TextView) itemView.findViewById(R.id.stream_title);
            busker = (TextView) itemView.findViewById(R.id.stream_busker);
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
        public void onBindViewHolder(final HorizontalViewHolder holder, int position) {
            final PostPromote data = horizontalData.get(position);
            holder.title.setText(data.getPostPrmt_busking_title());
            Glide.with(holder.icon.getContext()).load(data.getPostPrmt_imageUrl()).into((holder).icon);
            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, PrmtPostDetailActivity.class);
                    intent.putExtra("\"detail",data);
                    startActivity(intent);

                }
            });
            FirebaseDatabase.getInstance().getReference().child("users").orderByChild("uid").equalTo(data.postPrmt_uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){

                        UserModel u = singleSnapshot.getValue(UserModel.class);
                        holder.buskername.setText( u.getUserName());
                    }


                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        @Override
        public int getItemCount() {
            return horizontalData.size();
        }

    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder{


        public ImageView icon;
        public TextView title;
        public TextView buskername;

        public HorizontalViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView)itemView.findViewById(R.id.busking_icon);
            title = (TextView) itemView.findViewById(R.id.busking_title);
            buskername = (TextView) itemView.findViewById(R.id.busking_busker);
        }

    }




}
