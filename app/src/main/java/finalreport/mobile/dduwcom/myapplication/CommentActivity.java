package finalreport.mobile.dduwcom.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import finalreport.mobile.dduwcom.myapplication.Models.Comment;
import finalreport.mobile.dduwcom.myapplication.Models.PostNormal;
import finalreport.mobile.dduwcom.myapplication.Models.UserModel;
import io.antmedia.android.liveVideoBroadcaster.R;

public class CommentActivity extends AppCompatActivity {

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    PostNormal post;
    //widgets
    private ImageView mBackArrow, mCheckMark, goback;
    private EditText mComment;
    private Context mContext;


    //comment 리스트
    private RecyclerView commentRecyclerView;
    private CommentAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private Comment comment;
    private ArrayList<Comment> mComments;
    private ArrayList<String> commentUsers = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        post = (PostNormal) getIntent().getSerializableExtra("post");

        mBackArrow = (ImageView) findViewById(R.id.backArrow);
        mCheckMark = (ImageView) findViewById(R.id.ivPostComment);
        mComment = (EditText) findViewById(R.id.comment);
        mComments = new ArrayList<>();
        mContext = this;
        goback = findViewById(R.id.backArrow);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initCommentSection();



        commentRecyclerView = findViewById(R.id.comment_recyclerview);
        mLayoutManager = new LinearLayoutManager(CommentActivity.this);
        commentRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CommentAdapter();
        mAdapter.setData(mComments);
        commentRecyclerView.setAdapter(mAdapter);

        mCheckMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendComment();
                mComment.setText("");
                closeKeyboard();
            }
        });




    }
    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void initCommentSection() {
//        RecyclerView commentRecyclerView = (RecyclerView) findViewById(R.id.comment_recyclerview);
//        commentRecyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
//
//        FirebaseRecyclerAdapter<Comment, CommentHolder> commentAdapter = new FirebaseRecyclerAdapter<Comment, CommentHolder>(
//                Comment.class,
//                R.layout.layout_comment,
//                CommentHolder.class,
//                FirebaseDatabase.getInstance().getReference("comments")
//                        .child(post.norm_postID)
//
//        ) {
//            @Override
//            protected void populateViewHolder(CommentHolder viewHolder, Comment model, int position) {
//                viewHolder.setUsername(model.getUser().getUserName());
//                viewHolder.setComment(model.getComment());
//                viewHolder.setTime(DateUtils.getRelativeTimeSpanString(model.getTimeCreated()));
//
//                Glide.with(CommentActivity.this)
//                        .load(model.getUser().getProfileImageUrl())
//                        .into(viewHolder.commentOwnerDisplay);
//            }
//        };
//
//        commentRecyclerView.setAdapter(commentAdapter);

        FirebaseDatabase.getInstance().getReference("comments").child(post.getNorm_postID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                mComments.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.
                    Log.d("FragmentActivity", "Single ValueEventListener : " + messageData.child("norm_imageUrl").getValue());
                    Comment comment = (Comment)  messageData.getValue(Comment.class);
                    mComments.add(comment);
                    commentUsers.add(comment.getUseruid());
                    mAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







    }

    private void sendComment() {
        final ProgressDialog progressDialog = new ProgressDialog(CommentActivity.this);
        progressDialog.setMessage("Sending comment..");
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        comment = new Comment();
        String path = FirebaseDatabase.getInstance().getReference().push().toString();
        final String uid = path.substring(path.lastIndexOf("/") + 1);

        String etcomment = mComment.getText().toString();

//        UserModel(String userName, String profileImageUrl, String uid)
//        comment.setUser(new UserModel(mAuth.getCurrentUser().getuse, ));

        comment.setUseruid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        comment.setCommentId(uid);
        comment.setComment(etcomment);
        SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy년 MM월 dd일", Locale.KOREA );
        Date currentTime = new Date ( );
        String dTime = formatter.format ( currentTime );
        comment.setTimeCreated(dTime);

        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ","))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        FirebaseDatabase.getInstance().getReference("/")
                                .child("comments")
                                .child(post.getNorm_postID())
                                .child(uid)
                                .setValue(comment);


                        FirebaseDatabase.getInstance()
                                .getReference("/")
                                .child("post_Normal")
                                .child(post.getNorm_postID())
                                .child("comments")
                                .runTransaction(new Transaction.Handler() {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                        long num = (long) mutableData.getValue();
                                        mutableData.setValue(num + 1);
                                        return Transaction.success(mutableData);
                                    }

                                    @Override
                                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                        progressDialog.dismiss();


                                        FirebaseDatabase.getInstance().getReference("users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",","))
                                        .child("comments").runTransaction(new Transaction.Handler() {
                                            @Override
                                            public Transaction.Result doTransaction(MutableData mutableData) {
                                                ArrayList<String> myRecordCollection;
                                                if(mutableData.getValue() == null){
                                                    myRecordCollection = new ArrayList<String>(1);
                                                    myRecordCollection.add(uid);
                                                }else{
                                                    myRecordCollection = (ArrayList<String>) mutableData.getValue();
                                                    myRecordCollection.add(uid);
                                                }

                                                mutableData.setValue(myRecordCollection);
                                                return Transaction.success(mutableData);
                                            }

                                            @Override
                                            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                                            }
                                        });

                                    }
                                });
                    }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.dismiss();
                        }

                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("BUNDLE_COMMENT", comment);
        super.onSaveInstanceState(outState);
    }


    public class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {
        private ArrayList<Comment> horizontalData;

        public void setData(ArrayList<Comment> list){
            horizontalData = list;
        }

        @Override
        public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {

// 사용할 아이템의 뷰를 생성해준다.
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment, parent, false);

            CommentHolder holder = new CommentHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(final CommentHolder holder, int position) {

            //comment 추가시 사용자 이미지 다른거 저장되는 거 고쳐야함
            FirebaseDatabase.getInstance().getReference("users").child(commentUsers.get(position)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                        UserModel userModel = (UserModel)dataSnapshot.getValue(UserModel.class);

                        Glide.with(holder.commentOwnerDisplay.getContext()).load(userModel.getProfileImageUrl()).into((holder).commentOwnerDisplay);
                        holder.usernameTextView.setText(userModel.getUserName());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Comment data = horizontalData.get(position);

            holder.timeTextView.setText(String.valueOf(data.getTimeCreated()));
            holder.commentTextView.setText(data.getComment());

        }

        @Override
        public int getItemCount() {
            return horizontalData.size();
        }

    }



    public static class CommentHolder extends RecyclerView.ViewHolder {
        ImageView commentOwnerDisplay;
        TextView usernameTextView;
        TextView timeTextView;
        TextView commentTextView;


        public CommentHolder(View itemView) {
            super(itemView);
            commentOwnerDisplay = (CircleImageView) itemView.findViewById(R.id.comment_profile_image);
            usernameTextView = (TextView) itemView.findViewById(R.id.comment_username);
            timeTextView = (TextView) itemView.findViewById(R.id.comment_time_posted);
            commentTextView = (TextView) itemView.findViewById(R.id.comment);
        }

        public void setUsername(String username) {
            usernameTextView.setText(username);
        }

        public void setTime(CharSequence time) {
            timeTextView.setText(time);
        }

        public void setComment(String comment) {
            commentTextView.setText(comment);
        }
    }


}
