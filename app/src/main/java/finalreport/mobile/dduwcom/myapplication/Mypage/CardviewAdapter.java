package finalreport.mobile.dduwcom.myapplication.Mypage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import finalreport.mobile.dduwcom.myapplication.Models.BuskingData;
import io.antmedia.android.liveVideoBroadcaster.R;

public class CardviewAdapter extends RecyclerView.Adapter<CardviewViewHolder> {
    private ArrayList<BuskingData> cardviewData;

    public void setData(ArrayList<BuskingData> list){
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
    public void onBindViewHolder(CardviewViewHolder holder, int position) {
        BuskingData data = cardviewData.get(position);

        holder.title.setText(data.getTitle());
        holder.genre.setText(data.getGenre());
        holder.time.setText(data.getTime());
        holder.location.setText(data.getLocation());
        holder.icon.setImageResource(data.getImg());

    }

    @Override
    public int getItemCount() {
        return cardviewData.size();
    }

}
