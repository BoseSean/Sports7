package org.team7.sports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;

public class ProfileActivity extends AppCompatActivity {

    private TextView fUsername;
    private TextView fFriendcount;
    private Button send_friend_request;
    private Button message_user;
    private HashSet<String> friends;
    private String friendId;
    private String usrid;
    private DatabaseReference myref;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fUsername = findViewById(R.id.user_name);
        fFriendcount = findViewById(R.id.friend_count);
        friendId = getIntent().getStringExtra("this_friend_id");
        send_friend_request = findViewById(R.id.send_request);
        message_user = findViewById(R.id.message_user);
        toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        friends = new HashSet<String>();

        FirebaseUser currentUse = FirebaseAuth.getInstance().getCurrentUser();
        usrid = currentUse.getUid();

        myref = FirebaseDatabase.getInstance().getReference().child("Users").child(friendId);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(usrid).child("friends");

        ref.orderByValue().equalTo(friendId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null)
                    send_friend_request.setText("You are already friend");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ref.keepSynced(true);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                friends.add(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                friends.add(dataSnapshot.getValue().toString());

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                friends.remove(dataSnapshot.getValue().toString());

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                switch (dataSnapshot.getKey()){
                    case "name": {
                        fUsername.setText(dataSnapshot.getValue().toString());
                        break;
                    }
                    case "numoffriends": {
                        fFriendcount.setText("2");
                        break;
                    }

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                switch (dataSnapshot.getKey()) {
                    case "name": {
                        fUsername.setText(dataSnapshot.getValue().toString());
                        break;
                    }
                    case "numoffriends": {
                        fFriendcount.setText("2");
                        break;
                    }

                }
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


        message_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(friends.contains(myref.getKey())){
                    Intent chatIntent = new Intent(ProfileActivity.this, ChatActivity.class);
                    chatIntent.putExtra("that_user_id", myref.getKey());
                    startActivity(chatIntent);
                }
            }
        });
        send_friend_request.setOnClickListener((new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if(friends.contains(myref.getKey())){
                    send_friend_request.setText("Friend Already");
                }
                else {
                    addFriend(myref);
                }
            }
        }));

    }

    public void addFriend(DatabaseReference ref) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ref.child("friends").push().setValue(currentUser.getUid());

        DatabaseReference currUserRef;
        currUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
        currUserRef.child("friends").push().setValue(ref.getKey());
    }



}
