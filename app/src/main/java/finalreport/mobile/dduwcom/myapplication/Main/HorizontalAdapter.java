package finalreport.mobile.dduwcom.myapplication.Main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import finalreport.mobile.dduwcom.myapplication.Models.BuskingData;
import io.antmedia.android.liveVideoBroadcaster.R;

/**
 * Created by kohheekyung on 2018. 6. 27..
 */

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalViewHolder> {
    private ArrayList<BuskingData> horizontalData;

    public void setData(ArrayList<BuskingData> list){
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
        BuskingData data = horizontalData.get(position);

        holder.title.setText(data.getTitle());
        holder.icon.setImageResource(data.getImg());

    }

    @Override
    public int getItemCount() {
        return horizontalData.size();
    }

}
