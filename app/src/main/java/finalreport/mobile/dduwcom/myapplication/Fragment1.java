package finalreport.mobile.dduwcom.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.antmedia.android.liveVideoBroadcaster.R;

public class Fragment1 extends Fragment {
    private RecyclerView mVerticalView;
    private VerticalAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private int MAX_ITEM_COUNT = 50;


    public Fragment1() {
        // Required empty public constructor
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);


        mVerticalView = (RecyclerView)view.findViewById(R.id.vertical_list);
        ArrayList<BuskingData> data = new ArrayList<>();

        int i = 0;
        while (i < MAX_ITEM_COUNT) {
            data.add(new BuskingData(R.mipmap.ic_launcher,  "공연이름", "장르", "시간", i+"m"));
            i++;
        }
        mLayoutManager = new LinearLayoutManager(getContext());
        mVerticalView.setLayoutManager(mLayoutManager);
        mAdapter = new VerticalAdapter();
        mAdapter.setData(data);
        mVerticalView.setAdapter(mAdapter);




        return view;

    }
}
