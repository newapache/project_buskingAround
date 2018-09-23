package finalreport.mobile.dduwcom.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;
import finalreport.mobile.dduwcom.myapplication.Models.UserModel;
import io.antmedia.android.liveVideoBroadcaster.R;

public class EditProfileActivity extends AppCompatActivity{
    private static final String TAG = "EditProfileFragment";
    private static final int PICK_FROM_ALBUM = 10;
    //firebase
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef =  FirebaseDatabase.getInstance().getReference();
    private String userID;
    private boolean isImageChange = false;


    //EditProfile Fragment widgets
    private EditText mDisplayName, mUsername, mWebsite, mDescription, mEmail, mPhoneNumber;
    private TextView mChangeProfilePhoto;
    private CircleImageView mProfilePhoto;
    public Uri imageUri;

    //vars
    private UserModel mUser ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Intent intent = getIntent();
        mUser = (UserModel) intent.getSerializableExtra("user");
        mProfilePhoto = (CircleImageView) findViewById(R.id.profile_photo);
        mUsername = (EditText) findViewById(R.id.username);
        mDescription = (EditText) findViewById(R.id.description);
        mChangeProfilePhoto = (TextView) findViewById(R.id.changeProfilePhoto);

        //setProfileImage();
        setupFirebaseAuth();

        //back arrow for navigating back to "ProfileActivity"
        ImageView backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to ProfileActivity");
                finish();
            }
        });

        ImageView checkmark = (ImageView) findViewById(R.id.saveChanges);
        checkmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: attempting to save changes.");
                saveProfileSettings();
                Toast.makeText(EditProfileActivity.this, "바뀜", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void saveProfileSettings(){



        final String username = mUsername.getText().toString();
        final String description = mDescription.getText().toString();

        //case1: if the user made a change to their username
        if(!mUser.getUserName().equals(username)){
            checkIfUsernameExists(username);
        }

        /**
         * change the rest of the settings that do not require uniqueness
         */
        if(!mUser.getDescription().equals(description)){
            //update description
            mFirebaseDatabase.getReference().child("users")
                    .child(mAuth.getCurrentUser().getUid())
                    .child("description")
                    .setValue(description);
        }
        if(!mUser.getProfileImageUrl().equals(imageUri)){
            //update description
//            mFirebaseDatabase.getReference().child("users")
//                    .child(mAuth.getCurrentUser().getUid())
//                    .child("profileImageUrl")
//                    .setValue(imageUri.toString());
            FirebaseStorage.getInstance().getReference().child("userImages").child(mUser.getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    @SuppressWarnings("VisibleForTests")
                    String imageUrl = task.getResult().getDownloadUrl().toString();
                    mFirebaseDatabase.getReference().child("users")
                            .child(mAuth.getCurrentUser().getUid())
                            .child("profileImageUrl")
                            .setValue(imageUrl);
                }
            });
        }

    }

    private void checkIfUsernameExists(final String username) {
        Log.d(TAG, "checkIfUsernameExists: Checking if  " + username + " already exists.");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("users")
                .orderByChild("userName")
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){
                    //add the username

                    Log.d(TAG, "updateUsername: upadting username to: " + username);

                    myRef.child("users")
                            .child(mAuth.getCurrentUser().getUid())
                            .child("userName")
                            .setValue(username);

                    Toast.makeText(EditProfileActivity.this, "saved username.", Toast.LENGTH_SHORT).show();

                }
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    if (singleSnapshot.exists()){
                        Toast.makeText(EditProfileActivity.this, "That username already exists.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userID = mAuth.getCurrentUser().getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                setProfileWidgets();

                //retrieve images for the user in question

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setProfileWidgets(){

        Glide.with(mProfilePhoto.getContext()).load(mUser.getProfileImageUrl()).into(mProfilePhoto);
//        mProfilePhoto.setImageURI(Uri.parse(mUser.getProfileImageUrl()));
        mUsername.setText(mUser.getUserName());
        mDescription.setText(mUser.getDescription());
        mChangeProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: changing profile photo");
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,PICK_FROM_ALBUM);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(requestCode == PICK_FROM_ALBUM && resultCode ==RESULT_OK && data != null){
            mProfilePhoto.setImageURI(data.getData()); // 가운데 뷰를 바꿈
            Log.v("imgage", data.getData().toString());
            imageUri = data.getData();// 이미지 경로 원본
//            FirebaseStorage.getInstance().getReference("/").child("userImages").child(mUser.getUid()).putFile(imageUri);
        }
    }

}
