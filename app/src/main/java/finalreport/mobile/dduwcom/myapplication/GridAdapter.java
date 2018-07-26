package finalreport.mobile.dduwcom.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import io.antmedia.android.liveVideoBroadcaster.R;

/**
 * Created by kohheekyung on 2018. 7. 3..
 */


public class GridAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<BuskingData> DataList;
    private LayoutInflater layoutInflater;

    public GridAdapter(Context context, int layout, ArrayList<BuskingData> dataList) {
        this.context = context;
        this.layout = layout;
        this.DataList = dataList;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return DataList.size();
    }

    @Override
    public Object getItem(int i) {
        return DataList.get(i);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int positon = i;
        if(view == null)
            view = layoutInflater.inflate(layout, viewGroup, false);

        ImageView imageView = (ImageView)view.findViewById(R.id.grid_icon);

        return view;
    }

}