package finalreport.mobile.dduwcom.myapplication.Map;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import finalreport.mobile.dduwcom.myapplication.Utils.SquareImageView;
import io.antmedia.android.liveVideoBroadcaster.R;

/**
 * Created by kohheekyung on 2018. 6. 27..
 */


public class VerticalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView icon;
    public TextView title;
    public TextView busker;
    /*public TextView genre;
    public TextView time;*/
    public TextView distance;
    public TextView time;
    private ItemClickListener itemClickListener;
    public VerticalViewHolder(View itemView) {
        super(itemView);
        icon = (ImageView)itemView.findViewById(R.id.vertical_icon);
        title = (TextView) itemView.findViewById(R.id.vertical_title);
        busker = (TextView) itemView.findViewById(R.id.whobusk);
        /*genre = (TextView) itemView.findViewById(R.id.vertical_genre);
        time = (TextView) itemView.findViewById(R.id.vertical_time);*/
        distance = (TextView) itemView.findViewById(R.id.vertical_location);
        time = (TextView) itemView.findViewById(R.id.vertical_time);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getPosition());
    }

}
