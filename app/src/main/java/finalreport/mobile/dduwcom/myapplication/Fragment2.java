package finalreport.mobile.dduwcom.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import io.antmedia.android.liveVideoBroadcaster.R;

public class Fragment2 extends Fragment {

    private int MAX_ITEM_COUNT = 50;
    GridView gridview;
    GridAdapter gridAdapter;
    DisplayMetrics mMetrics;
    ArrayList<BuskingData> data;
    public Fragment2() {
        // Required empty public constructor
        super();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);
        // Inflate the layout for this fragment
        data = new ArrayList<>();
        int i = 0;
        while (i < MAX_ITEM_COUNT) {
            data.add(new BuskingData(R.mipmap.ic_launcher, "공연이름", "장르", "시간", i + "m"));
            i++;
        }
        gridAdapter = new GridAdapter(getContext(), R.layout.grid_item, data);
        gridview = (GridView) view.findViewById(R.id.gridView1);
        gridview.setAdapter(gridAdapter);
        return view;
    }

}
