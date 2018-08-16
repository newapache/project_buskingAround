package finalreport.mobile.dduwcom.myapplication.Main;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.antmedia.android.liveVideoBroadcaster.R;

/**
 * Created by kohheekyung on 2018. 6. 27..
 */

public class HorizontalViewHolder extends RecyclerView.ViewHolder{


    public ImageView icon;
    public TextView title;

    public HorizontalViewHolder(View itemView) {
        super(itemView);
        icon = (ImageView)itemView.findViewById(R.id.busking_icon);
        title = (TextView) itemView.findViewById(R.id.busking_title);
    }

}
