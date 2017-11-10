package org.team7.sports;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.util.HashMap;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        fUsername = findViewById(R.id.user_name);
        fFriendcount = findViewById(R.id.friend_count);
        friendId = getIntent().getStringExtra("this_friend_id");

        friends = new HashSet<String>();

        FirebaseUser currentUse = FirebaseAuth.getInstance().getCurrentUser();
        usrid = currentUse.getUid();

        myref = FirebaseDatabase.getInstance().getReference().child("Users").child(friendId);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(usrid).child("friends");
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

        send_friend_request = findViewById(R.id.send_request);
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
