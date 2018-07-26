package finalreport.mobile.dduwcom.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.antmedia.android.liveVideoBroadcaster.R;

public class Fragment3 extends Fragment {

    public Fragment3() {
        // Required empty public constructor
        super();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view1 = inflater.inflate(R.layout.fragment_fragment3, container, false);

        return  view1;
    }
}
