package finalreport.mobile.dduwcom.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import io.antmedia.android.liveVideoBroadcaster.R;

public class ReadPostPrmtActivity extends AppCompatActivity {

    ListView lv;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_postprmt);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        ref = db.getReference("/"); /////////////////////////////////fuckinghere!!!!!!
        final ArrayList<String> list_postPrmt = new ArrayList<String>();
        list_postPrmt.add("aaaa");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("firebaseTest","onChildAdded>> " + dataSnapshot.getKey());
                for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                    PostPromote postPrmt = (PostPromote) childSnapShot.getValue(PostPromote.class);
                    Log.i("firebaseTest","onChildAdded>> " + postPrmt.postPrmt_title);

                    list_postPrmt.add(postPrmt.postPrmt_title);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        lv = findViewById(R.id.lv_postPrmt);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list_postPrmt));

    }
}