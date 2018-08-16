package finalreport.mobile.dduwcom.myapplication.CreatePost;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import finalreport.mobile.dduwcom.myapplication.Models.PostNormal;
import io.antmedia.android.liveVideoBroadcaster.R;

public class PostFragment2 extends Fragment{

    public static final int GALLERY_CODE = 10;
    private ImageView iv_normImage;
    private EditText et_title;
    private EditText et_content;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private Uri normImageUri;
    private String imagePath;

    private Button btnCreatePost;

    public PostFragment2() {
        // Required empty public constructor
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_post2, container, false);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("/");

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

        et_title = (EditText) view.findViewById(R.id.et_Normtitle);
        et_content = (EditText) view.findViewById(R.id.et_Normcontent);
        btnCreatePost = (Button) view.findViewById(R.id.btnCreateNormPost);
        iv_normImage = (ImageView) view.findViewById(R.id.postfragment2_image);




        iv_normImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GALLERY_CODE);

            }
        });

        btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FirebaseStorage.getInstance().getReference().child("normPostImages").child(et_title.getText().toString()).putFile(normImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        @SuppressWarnings("VisibleForTests")
                        String imageUrl = task.getResult().getDownloadUrl().toString();

                        PostNormal postNormal = new PostNormal();
                        postNormal.norm_imageUrl = imageUrl;
                        postNormal.norm_title = et_title.getText().toString();
                        postNormal.norm_content = et_content.getText().toString();
                        postNormal.norm_uid = auth.getCurrentUser().getUid();
                        postNormal.norm_userId = auth.getCurrentUser().getEmail();

                        ref.child("post_Normal").push().setValue(postNormal);


                    }
                });




            }
        });



        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_CODE && resultCode == Activity.RESULT_OK && data != null) {

            Log.d("PostFragment2", "Photo selected");
            iv_normImage.setImageURI(data.getData()); // 가운데 뷰를 바꿈
            normImageUri = data.getData();// 이미지 경로 원본


        }



    }


}

