

package finalreport.mobile.dduwcom.myapplication.Map;

        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import com.bumptech.glide.Glide;

        import java.util.ArrayList;
        import java.util.List;

        import finalreport.mobile.dduwcom.myapplication.Map.VerticalViewHolder;
        import finalreport.mobile.dduwcom.myapplication.Models.BuskingData;
        import finalreport.mobile.dduwcom.myapplication.Models.PostPromote;
        import io.antmedia.android.liveVideoBroadcaster.R;

/**
 * Created by kohheekyung on 2018. 6. 27..
 */

public class VerticalAdapter extends RecyclerView.Adapter<VerticalViewHolder> {
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
    public void onBindViewHolder(VerticalViewHolder holder, int position) {
        PostPromote postPrmt = verticalPostPrmt.get(position);

        holder.title.setText(postPrmt.postPrmt_busking_title);
        /*holder.genre.setText(postPrmt.postPrmt_content);
        holder.time.setText(postPrmt.postPrmt_content);*/
        holder.distance.setText(String.valueOf(postPrmt.postPrmt_distance) + "km ");
        holder.time.setText(postPrmt.busking_date+postPrmt.busking_time+ "에 시작");
        Glide.with(holder.icon.getContext()).load(postPrmt.getPostPrmt_imageUrl()).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return verticalPostPrmt.size();
    }
}


