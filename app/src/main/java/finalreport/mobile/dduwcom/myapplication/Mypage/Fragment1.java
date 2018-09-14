package finalreport.mobile.dduwcom.myapplication.Mypage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import finalreport.mobile.dduwcom.myapplication.Models.BuskingData;
import finalreport.mobile.dduwcom.myapplication.Models.PostPromote;
import finalreport.mobile.dduwcom.myapplication.Models.UserModel;
import finalreport.mobile.dduwcom.myapplication.ReadPost.NormPostDetailActivity;
import finalreport.mobile.dduwcom.myapplication.ReadPost.PrmtPostDetailActivity;
import io.antmedia.android.liveVideoBroadcaster.R;

public class Fragment1 extends Fragment {

    private RecyclerView mCardview;
    private CardviewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<String> uidList = new ArrayList<>();
    private FirebaseDatabase mDatabase;

    String uidKey;
    private DatabaseReference mReference;
    private ChildEventListener mChild;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    ArrayList<PostPromote> prmtposts = new ArrayList<PostPromote>();

    public Fragment1() {
        // Required empty public constructor
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        mLayoutManager = new LinearLayoutManager(getContext());
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);
        mDatabase = FirebaseDatabase.getInstance();
        mCardview = (RecyclerView)view.findViewById(R.id.cardview_list);


        UserModel user = (UserModel) getArguments().getSerializable("user");
        mReference = mDatabase.getReference("post_promote"); // 변경값을 확인할 child 이름
        FirebaseDatabase.getInstance().getReference("post_promote").orderByChild("postPrmt_userID").equalTo(user.getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                prmtposts.clear();
                uidList.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.
                    Log.d("FragmentActivity", "Single ValueEventListener : " + messageData.child("norm_imageUrl").getValue());
                    PostPromote postprtm = (PostPromote) messageData.getValue(PostPromote.class);
                    prmtposts.add(postprtm);
                    uidList.add(messageData.getKey());
                    mAdapter.notifyDataSetChanged();
                }
                dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mCardview.setLayoutManager(mLayoutManager);
        mAdapter = new CardviewAdapter();
        mAdapter.setData(prmtposts);
        mCardview.setAdapter(mAdapter);


        return view;

    }

    public class CardviewAdapter extends RecyclerView.Adapter<CardviewViewHolder> {
        private ArrayList<PostPromote> cardviewData;

        public void setData(ArrayList<PostPromote> list){
            cardviewData = list;
        }

        @Override
        public CardviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

// 사용할 아이템의 뷰를 생성해준다.
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item, parent, false);

            CardviewViewHolder holder = new CardviewViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(CardviewViewHolder holder, final int position) {
            PostPromote data = cardviewData.get(position);

            holder.title.setText(data.getPostPrmt_busking_title());
            holder.genre.setText(data.getPostPrmt_title());
            holder.time.setText(data.getTimeCreated());
            holder.btime.setText(data.getBusking_date() +" "+ data.getBusking_time());
            holder.location.setText("홍대 놀이터");
            Glide.with(holder.icon.getContext()).load(data.getPostPrmt_imageUrl()).into(((CardviewViewHolder)holder).icon);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context , PrmtPostDetailActivity.class);

                    intent.putExtra("detail", (Serializable) prmtposts.get(position));
                    intent.putExtra("uidKey",  uidList.get(position));

                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return cardviewData.size();
        }

    }

    public class CardviewViewHolder extends RecyclerView.ViewHolder{


        public ImageView icon;
        public TextView title;
        public TextView genre;
        public TextView time;
        public TextView btime;
        public TextView location;
        public final View mView;

        public CardviewViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            icon = (ImageView)itemView.findViewById(R.id.vertical_icon);
            title = (TextView) itemView.findViewById(R.id.vertical_title);
            genre = (TextView) itemView.findViewById(R.id.vertical_genre);
            time = (TextView) itemView.findViewById(R.id.vertical_time);
            btime = (TextView) itemView.findViewById(R.id.busking_time);
            location = (TextView) itemView.findViewById(R.id.vertical_location);
        }

    }

}


