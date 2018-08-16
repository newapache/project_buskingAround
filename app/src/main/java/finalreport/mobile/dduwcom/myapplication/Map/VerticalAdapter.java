package finalreport.mobile.dduwcom.myapplication.Map;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import finalreport.mobile.dduwcom.myapplication.Map.VerticalViewHolder;
import finalreport.mobile.dduwcom.myapplication.Models.BuskingData;
import io.antmedia.android.liveVideoBroadcaster.R;

/**
 * Created by kohheekyung on 2018. 6. 27..
 */

public class VerticalAdapter extends RecyclerView.Adapter<VerticalViewHolder> {
    private ArrayList<BuskingData> verticalData;

    public void setData(ArrayList<BuskingData> list){
        verticalData = list;
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
        BuskingData data = verticalData.get(position);

        holder.title.setText(data.getTitle());
        holder.genre.setText(data.getGenre());
        holder.time.setText(data.getTime());
        holder.location.setText(data.getLocation());
        holder.icon.setImageResource(data.getImg());
    }

    @Override
    public int getItemCount() {
        return verticalData.size();
    }

}
