package finalreport.mobile.dduwcom.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.antmedia.android.liveVideoBroadcaster.R;

public class PostFragment2 extends Fragment{

    public PostFragment2() {
        // Required empty public constructor
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_post2, container, false);


        return view;

    }
}

