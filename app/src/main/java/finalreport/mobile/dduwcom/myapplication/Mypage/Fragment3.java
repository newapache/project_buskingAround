package finalreport.mobile.dduwcom.myapplication.Mypage;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import finalreport.mobile.dduwcom.myapplication.Models.PostPromote;
import finalreport.mobile.dduwcom.myapplication.Models.Stream;
import finalreport.mobile.dduwcom.myapplication.Models.UserModel;
import io.antmedia.android.liveVideoBroadcaster.R;
import io.antmedia.android.liveVideoPlayer.LiveVideoPlayerActivity;
import io.antmedia.android.liveVideoPlayer.streamPlayerActivity;

public class Fragment3 extends Fragment {

    private RecyclerView mRecylcerView;
    private StreamAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<String> uidList = new ArrayList<>();
    private FirebaseDatabase mDatabase;

    String uidKey;
    private DatabaseReference mReference;
    private ChildEventListener mChild;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    ArrayList<Stream> streams = new ArrayList<Stream>();

    public Fragment3() {
        // Required empty public constructor
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        View view = inflater.inflate(R.layout.fragment_fragment3, container, false);
        mDatabase = FirebaseDatabase.getInstance();
        mRecylcerView = (RecyclerView)view.findViewById(R.id.stream_list);


        UserModel user = (UserModel) getArguments().getSerializable("user");
        mReference = mDatabase.getReference("post_promote"); // 변경값을 확인할 child 이름
        FirebaseDatabase.getInstance().getReference("stream").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                streams.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.
                    Stream stream = (Stream) messageData.getValue(Stream.class);
                    if(!stream.isStreaming) {
                        streams.add(stream);
                        mAdapter.notifyDataSetChanged();
                    }
                 }
                dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRecylcerView.setLayoutManager(mLayoutManager);
        mAdapter = new StreamAdapter();
        mAdapter.setData(streams);
        mRecylcerView.setAdapter(mAdapter);


        return view;

    }

    public class StreamAdapter extends RecyclerView.Adapter<StreamViewHolder> {
        private ArrayList<Stream> streamData;

        public void setData(ArrayList<Stream> list){
            streamData = list;
        }

        @Override
        public StreamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

// 사용할 아이템의 뷰를 생성해준다.
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stream_item, parent, false);

            StreamViewHolder holder = new StreamViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(StreamViewHolder holder, int position) {
            final Stream data = streamData.get(position);


            String str = data.getStreamTime();
            String result = str.substring(0, 10);

            holder.date.setText(result+" 방송본");
            holder.name.setText(data.getStreamName());
            holder.gostream.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), data.getStreamUrl() , Toast.LENGTH_SHORT).show();
                    Intent intent =  new Intent(getContext() , streamPlayerActivity.class);
                    intent.putExtra("streamName", data.getStreamName());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return streamData.size();
        }

    }

    public class StreamViewHolder extends RecyclerView.ViewHolder{

        public TextView date;
        public TextView name;
        public Button gostream;

        public StreamViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.streamDate);
            name = itemView.findViewById(R.id.streamName);
            gostream = itemView.findViewById(R.id.gostream);

        }

    }
}
