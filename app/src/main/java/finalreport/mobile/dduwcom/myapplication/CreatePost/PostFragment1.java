package finalreport.mobile.dduwcom.myapplication.CreatePost;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import finalreport.mobile.dduwcom.myapplication.Models.PostPromote;
import finalreport.mobile.dduwcom.myapplication.ReadPost.ReadPostPrmtActivity;
import io.antmedia.android.liveVideoBroadcaster.R;


public class PostFragment1 extends Fragment{
    private static final int PICK_FROM_ALBUM = 10;
    private DatabaseReference ref;

    private EditText et_title;
    private EditText et_content;
    private EditText et_bTitle;
    private EditText et_bLat;
    private EditText et_bLon;
    private ImageView iv_postImage;
    private Uri imageUri;

    private Button btnCreatePost;
    private Button btnReadPost;

    public PostFragment1() {
        // Required empty public constructor
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_post1, container, false);


        FirebaseDatabase db = FirebaseDatabase.getInstance();
        ref = db.getReference("/");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("firebaseTest", "onChildChange>> " + dataSnapshot.getKey());
                Log.d("firebaseTest", "onChildChange>> " + dataSnapshot.getChildrenCount());

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d("firebaseTest", "postSnapShot>> " + postSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        et_title = (EditText) view.findViewById(R.id.et_title);
        et_content = (EditText) view.findViewById(R.id.et_content);
        et_bTitle = (EditText) view.findViewById(R.id.et_bTitle);
        et_bLat = (EditText)view.findViewById(R.id.et_bLat);
        et_bLon = (EditText)view.findViewById(R.id.et_bLon);
        iv_postImage = (ImageView)view.findViewById(R.id.postfragment1_image);

        btnCreatePost = (Button) view.findViewById(R.id.btnCreatePost);

        btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*createUser("newnew", "newuser@naver.com");*/
                createPostPromote(et_title.getText().toString(), et_content.getText().toString(),
                        et_bTitle.getText().toString(), Double.parseDouble(et_bLat.getText().toString()), Double.parseDouble(et_bLon.getText().toString()));

                //Log.d("firebaseTest", " ** ");
                startActivity(new Intent( getContext(), ReadPostPrmtActivity.class));
            }
        });




        return view;

    }

    private void createPostPromote(String postPrmt_title, String postPrmt_content,
                                   String postPrmt_busking_title, double postPrmt_busking_latitude, double postPrmt_busking_longitude) {

        PostPromote postPrmt = new PostPromote(postPrmt_title, postPrmt_content, postPrmt_busking_title, postPrmt_busking_latitude, postPrmt_busking_longitude);
        ref.child("post_promote").push().setValue(postPrmt);
    }



}




