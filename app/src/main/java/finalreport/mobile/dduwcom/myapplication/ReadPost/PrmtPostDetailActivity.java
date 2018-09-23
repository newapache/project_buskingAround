package finalreport.mobile.dduwcom.myapplication.ReadPost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import finalreport.mobile.dduwcom.myapplication.CommentActivity;
import finalreport.mobile.dduwcom.myapplication.Models.PostNormal;
import finalreport.mobile.dduwcom.myapplication.Models.PostPromote;
import finalreport.mobile.dduwcom.myapplication.Utils.Heart;
import finalreport.mobile.dduwcom.myapplication.Utils.SquareImageView;
import finalreport.mobile.dduwcom.myapplication.prmtCommentActivity;
import io.antmedia.android.liveVideoBroadcaster.R;

public class PrmtPostDetailActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private Heart mHeart;
    private String uidKey;
    private PostPromote p;
    //widgets
    private SquareImageView mPostImage;
    private TextView mBackLabel, mCaption, mUsername, mTimestamp, mLikes, mComments, mcontent, mwhen, mwhere;
    private ImageView mBackArrow, mEllipses, mHeartRed, mHeartWhite, mProfileImage, mComment, goback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prmt_post_detail);

        auth = FirebaseAuth.getInstance();
        mPostImage = (SquareImageView) findViewById(R.id.post_image);
        mCaption = (TextView) findViewById(R.id.image_caption);
        mUsername = (TextView) findViewById(R.id.username);
        mTimestamp = (TextView) findViewById(R.id.image_time_posted);
        mEllipses = (ImageView) findViewById(R.id.ivEllipses);
        mHeartRed = (ImageView) findViewById(R.id.image_heart_red);
        mHeartWhite = (ImageView) findViewById(R.id.image_heart);
        mProfileImage = (ImageView) findViewById(R.id.profile_photo);
        mLikes = (TextView) findViewById(R.id.image_likes);
        mComment = (ImageView) findViewById(R.id.speech_bubble);
        mComments = (TextView) findViewById(R.id.image_comments_link);
        mcontent = (TextView) findViewById(R.id.post_content);
        goback = (ImageView) findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mwhen = (TextView) findViewById(R.id.post_when);
        mwhere = (TextView) findViewById(R.id.post_where);
        mHeart = new Heart(mHeartWhite, mHeartRed);

        final PostPromote post;

        Intent intent = getIntent();
        post = (PostPromote)intent.getSerializableExtra("detail");
        uidKey = intent.getStringExtra("uidKey");

        Log.d("post", post.getPostPrmt_busking_title());

        Glide.with(mPostImage.getContext()).load(post.getPostPrmt_imageUrl()).into(mPostImage);
        mcontent.setText(post.getPostPrmt_content());
        mwhen.setText(post.getBusking_date()+" "+post.busking_time);
        mwhere.setText("홍대 놀이터");

        if (post.stars.containsKey(auth.getCurrentUser().getUid())) {
            // Unstar the post and remove self from stars
            mHeartWhite.setImageResource(R.drawable.ic_heart_red);
            Log.d("like", "bb");
        } else {
            // Star the post and add self to stars
            mHeartWhite.setImageResource(R.drawable.ic_heart_white);
            Log.d("like", "dd");
        }


        mHeartWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStarClicked( FirebaseDatabase.getInstance().getReference("post_promote").child(uidKey));
            }
        });


        mComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrmtPostDetailActivity.this, prmtCommentActivity.class);
                intent.putExtra("post",post);
                startActivity(intent);
            }
        });

//        if (p.stars.containsKey(auth.getCurrentUser().getUid())){
//            mHeartWhite.setVisibility(View.GONE);
//            mHeartRed.setVisibility(View.VISIBLE);
//        }
//        else{
//            mHeartWhite.setVisibility(View.VISIBLE);
//            mHeartRed.setVisibility(View.GONE);
//        }

    }

    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                p = mutableData.getValue(PostPromote.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(auth.getCurrentUser().getUid())) {
                    // Unstar the post and remove self from stars
                    mHeartWhite.setImageResource(R.drawable.ic_heart_white);
                    p.starCount = p.starCount - 1;
                    p.stars.remove(auth.getCurrentUser().getUid());
                    Log.d("like", "bb");
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1;
                    p.stars.put(auth.getCurrentUser().getUid(), true);
                    Log.d("like", "dd");
                    mHeartWhite.setImageResource(R.drawable.ic_heart_red);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d("LikeError", "postTransaction:onComplete:" + databaseError);
            }
        });
    }

}
