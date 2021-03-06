

package finalreport.mobile.dduwcom.myapplication.Map;

        import android.content.Intent;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import com.bumptech.glide.Glide;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
        import java.util.List;

        import finalreport.mobile.dduwcom.myapplication.Main.MainActivity;
        import finalreport.mobile.dduwcom.myapplication.Map.VerticalViewHolder;
        import finalreport.mobile.dduwcom.myapplication.Models.BuskingData;
        import finalreport.mobile.dduwcom.myapplication.Models.PostPromote;
        import finalreport.mobile.dduwcom.myapplication.Models.UserModel;
        import finalreport.mobile.dduwcom.myapplication.ReadPost.PrmtPostDetailActivity;
        import io.antmedia.android.liveVideoBroadcaster.R;

/**
 * Created by kohheekyung on 2018. 6. 27..
 */

public class VerticalAdapter extends RecyclerView.Adapter<VerticalViewHolder>{

    private ArrayList<PostPromote> verticalPostPrmt;

    public void setData(ArrayList<PostPromote> list){
        verticalPostPrmt = list;
    }



    @Override
    public VerticalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_item, parent, false);
 
        VerticalViewHolder holder = new VerticalViewHolder(view);



        return holder;
    }

    @Override
    public void onBindViewHolder(final VerticalViewHolder holder, final int position) {
        final PostPromote postPrmt = verticalPostPrmt.get(position);

        holder.title.setText(postPrmt.postPrmt_busking_title);
        /*holder.genre.setText(postPrmt.postPrmt_content);
        holder.time.setText(postPrmt.postPrmt_content);*/

        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("uid").equalTo(postPrmt.postPrmt_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){

                    UserModel u = singleSnapshot.getValue(UserModel.class);
                    holder.busker.setText( u.getUserName());
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        String[] s = (String.valueOf(postPrmt.postPrmt_distance)).split("\\.");
        holder.distance.setText(s[0] + "m");
        holder.date.setText(postPrmt.busking_date);
        holder.time.setText(postPrmt.busking_time);
        Glide.with(holder.icon.getContext()).load(postPrmt.getPostPrmt_imageUrl()).into(holder.icon);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(view.getContext(), PrmtPostDetailActivity.class);
                intent.putExtra("detail", postPrmt);
                view.getContext().startActivity(intent);


            }
        });

    }
    @Override
    public int getItemCount() {
        return verticalPostPrmt.size();
    }

}


