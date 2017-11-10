package org.team7.sports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;
import org.team7.sports.model.Game;

import java.util.HashSet;

public class ProfileActivity extends AppCompatActivity{

    private TextView user_name;
    private TextView friend_count;
    private Button send_friend_request;
    private Button message_user;
    private HashSet<String> friends;
    private String friendId;
    private String usrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user_name = findViewById(R.id.user_name);
        friend_count = findViewById(R.id.friend_count);
        FirebaseUser currentUse = FirebaseAuth.getInstance().getCurrentUser();
        usrid = currentUse.getUid();
        friendId = getIntent().getStringExtra("this_friend_id");
        Log.d("eeeeeeeeeeeeeeeeeeeee", friendId);
        friends = new HashSet<String>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("GameThread")
                .child(usrid).child("friends");
//        send_friend_request.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                if(user_name.contains(usrid))
//            }
//        }
    }



}
