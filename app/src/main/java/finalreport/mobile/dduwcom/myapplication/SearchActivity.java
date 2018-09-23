package finalreport.mobile.dduwcom.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import finalreport.mobile.dduwcom.myapplication.Models.UserModel;
import finalreport.mobile.dduwcom.myapplication.Mypage.MypageActivity;
import io.antmedia.android.liveVideoBroadcaster.R;

public class SearchActivity extends AppCompatActivity {

    private Context mContext = SearchActivity.this;

    //widgets
    private EditText mSearchParam;
    private ListView mListView;
    private ImageView backarrow;

    //vars
    private List<UserModel> mUserList;
    private UserListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchParam = (EditText) findViewById(R.id.search);
        mListView = (ListView) findViewById(R.id.listView);
        backarrow = (ImageView) findViewById(R.id.back);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        hideSoftKeyboard();
        initTextListener();
    }

    private void initTextListener(){
        Log.d("SearchActivity", "initTextListener: initializing");

        mUserList = new ArrayList<>();

        mSearchParam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String text = mSearchParam.getText().toString().toLowerCase(Locale.getDefault());
                searchForMatch(text);
            }
        });
    }
    private void searchForMatch(String keyword){
        Log.d("SearchActivity", "searchForMatch: searching for a match: " + keyword);
        mUserList.clear();
        //update the users list view
        if(keyword.length() ==0){

        }else{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = FirebaseDatabase.getInstance().getReference("users")
                    .orderByChild("userName").equalTo(keyword);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                        Log.d("SearchActivity", "onDataChange: found user:" + singleSnapshot.getValue(UserModel.class).toString());

                        mUserList.add(singleSnapshot.getValue(UserModel.class));
                        //update the users list view
                        updateUsersList();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void updateUsersList(){
        Log.d("SearchActivity", "updateUsersList: updating users list");

        mAdapter = new UserListAdapter(SearchActivity.this, R.layout.layout_user_listitem, mUserList);

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("searchActivity", "onItemClick: selected user: " + mUserList.get(position).toString());

                //navigate to profile activity
                Intent intent =  new Intent(SearchActivity.this, MypageActivity.class);

                intent.putExtra("user", (UserModel) mUserList.get(position));
                startActivity(intent);
            }
        });
    }


    private void hideSoftKeyboard(){
        if(getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
