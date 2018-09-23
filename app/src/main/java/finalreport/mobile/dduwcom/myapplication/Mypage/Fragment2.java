package finalreport.mobile.dduwcom.myapplication.Mypage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import finalreport.mobile.dduwcom.myapplication.Models.PostNormal;
import finalreport.mobile.dduwcom.myapplication.Models.UserModel;
import finalreport.mobile.dduwcom.myapplication.ReadPost.NormPostDetailActivity;
import io.antmedia.android.liveVideoBroadcaster.R;

public class Fragment2 extends Fragment{

    private RecyclerView mGridview;
    private GridAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<String> uidList = new ArrayList<>();
    private FirebaseDatabase mDatabase;

    String uidKey;
    private DatabaseReference mReference;
    private ChildEventListener mChild;
    private  StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    ArrayList<PostNormal> Normalposts = new ArrayList<>();

    private int MAX_ITEM_COUNT = 50;


    public Fragment2() {
        // Required empty public constructor
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){


        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(3,1);
        mStaggeredGridLayoutManager.setReverseLayout(true);


        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);
        mDatabase = FirebaseDatabase.getInstance();
        mGridview = (RecyclerView)view.findViewById(R.id.gridView1);


        UserModel user = (UserModel) getArguments().getSerializable("user");
        mReference = mDatabase.getReference("post_Normal"); // 변경값을 확인할 child 이름
        FirebaseDatabase.getInstance().getReference("post_Normal").orderByChild("norm_userId").equalTo(user.getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Normalposts.clear();
                uidList.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.
                    Log.d("FragmentActivity", "Single ValueEventListener : " + messageData.child("norm_imageUrl").getValue());
                    PostNormal postnorm = (PostNormal) messageData.getValue(PostNormal.class);
                    Normalposts.add(postnorm);
                    uidList.add(messageData.getKey());
                    mAdapter.notifyDataSetChanged();
                }
                dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mLayoutManager = new LinearLayoutManager(getContext());
        mGridview.setLayoutManager(mStaggeredGridLayoutManager );
        mAdapter = new GridAdapter();
        mAdapter.setData(Normalposts);
        mGridview.setAdapter(mAdapter);

        return view;

    }


    public class GridAdapter extends RecyclerView.Adapter<GridViewHolder> {
        private List<PostNormal> gridData;

        public void setData(List<PostNormal> list){
            gridData = list;
        }

        @Override
        public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // 사용할 아이템의 뷰를 생성해준다.
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);

            GridViewHolder holder = new GridViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull GridViewHolder holder, final int position) {

            PostNormal data = gridData.get(position);
            //        holder.gridIcon.setImageResource(data.getNorm_imageUrl().);
            Glide.with(holder.gridIcon.getContext()).load(data.getNorm_imageUrl()).into(((GridViewHolder)holder).gridIcon);
            holder.gridIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext() , NormPostDetailActivity.class);

                    intent.putExtra("detail", (Serializable) Normalposts.get(position));
                    intent.putExtra("uidKey",  uidList.get(position));


                    view.getContext().startActivity(intent);
                }
            });

        }


        @Override
        public int getItemCount() {
            return gridData.size();
        }

    }


    public class GridViewHolder extends RecyclerView.ViewHolder {


        public ImageView gridIcon;


        public GridViewHolder(View itemView) {
            super(itemView);
            gridIcon = (ImageView)itemView.findViewById(R.id.grid_icon);
//            itemView.setOnClickListener(this);

        }

//        @Override
//        public void onClick(View view) {
//
//           Intent intent = new Intent(view.getContext() , NormPostDetailActivity.class);
//
//            intent.putExtra("detail", (Serializable) Normalposts.get(getAdapterPosition()));
//            intent.putExtra("uidKey", uidKey);
//
//
//            view.getContext().startActivity(intent);
//        }
    }



}



