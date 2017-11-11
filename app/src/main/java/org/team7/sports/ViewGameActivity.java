package org.team7.sports;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.team7.sports.model.Game;

import java.util.HashMap;
import java.util.HashSet;

public class ViewGameActivity extends AppCompatActivity {

    private TextView mGameName;
    private TextView mSportType;
    private TextView mDate;
    private TextView mStartTime;
    private TextView mLocation;
    private TextView mHostName;
    private TextView mHostEmail;
    private TextView mIsPrivate;
    private String gameName;
    private Game g;
    private Button mJoinGame;
    private Button mChatGame;
    private String gameId;
    private TextInputLayout mPasswd;
    private Boolean isPrivate;
    private String password;
    private String gameType;
    private HashSet<String> players;
    private FirebaseDatabase database;
    private DatabaseReference myref;
    private String usrid;
    private Toolbar toolbar;
    private int nowNumOfppl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_game);

        mGameName = findViewById(R.id.game_name_TV);
        mSportType = findViewById(R.id.Sport_type_TV);
        mDate = findViewById(R.id.Date_TV);
        mStartTime = findViewById(R.id.start_time_TV);
        mLocation = findViewById(R.id.Location_TV);
        mHostName = findViewById(R.id.host_name_TV);
        mHostEmail = findViewById(R.id.host_email_TV);
        mIsPrivate = findViewById(R.id.isPrivate_TV);
        mPasswd = findViewById(R.id.passwd_input_TIL);
        mChatGame = findViewById(R.id.chat_Game_B);

        toolbar = findViewById(R.id.create_game_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Game Details");


        isPrivate = false;
        gameId = getIntent().getStringExtra("this_game_id");
        myref = FirebaseDatabase.getInstance().getReference().child("GameThread").child(gameId);
        Log.d("ddd", gameId);
        g = new Game();
        players = new HashSet<String>();
        FirebaseUser currentUse = FirebaseAuth.getInstance().getCurrentUser();
        usrid = currentUse.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("GameThread")
                .child(gameId).child("player");
        ref.keepSynced(true);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                players.add(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                players.add(dataSnapshot.getValue().toString());

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                players.remove(dataSnapshot.getValue().toString());

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
                switch (dataSnapshot.getKey()) {
                    case "date": {
                        mDate.setText(dataSnapshot.getValue().toString());
                        break;
                    }
                    case "gameName": {
                        gameName = dataSnapshot.getValue().toString();
                        mGameName.setText(gameName);
                        break;
                    }
                    case "hostEmail": {
                        mHostEmail.setText(dataSnapshot.getValue().toString());
                        break;
                    }
                    case "hostName": {
                        mHostName.setText(dataSnapshot.getValue().toString());
                        break;
                    }
                    case "isPrivate": {
                        if (dataSnapshot.getValue().toString().equalsIgnoreCase("true")) {
                            mIsPrivate.setText("Private Game");
                            isPrivate = true;
                        } else mIsPrivate.setText("Public Game");
                    }
                    case "location": {
                        mLocation.setText(dataSnapshot.getValue().toString());
                        break;
                    }
                    case "sportType": {
                        gameType = dataSnapshot.getValue().toString();
                        mSportType.setText(gameType);
                        break;
                    }
                    case "passwd": {
                        password = dataSnapshot.getValue().toString();
                        break;
                    }
                    case "startTime": {
                        String startTime = dataSnapshot.getValue().toString();
                        mStartTime.setText(startTime);
                        break;
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                switch (dataSnapshot.getKey()) {
                    case "date": {
                        mDate.setText(dataSnapshot.getValue().toString());
                        break;
                    }
                    case "gameName": {
                        mGameName.setText(dataSnapshot.getValue().toString());
                        break;
                    }
                    case "hostEmail": {
                        mHostEmail.setText(dataSnapshot.getValue().toString());
                        break;
                    }
                    case "hostName": {
                        mHostName.setText(dataSnapshot.getValue().toString());
                        break;
                    }
                    case "isPrivate": {
                        if (dataSnapshot.getValue().toString().equalsIgnoreCase("true")) {
                            mIsPrivate.setText("Private Game");
                            isPrivate = true;

                        } else mIsPrivate.setText("Public Game");
                    }
                    case "location": {
                        mLocation.setText(dataSnapshot.getValue().toString());
                        break;
                    }
                    case "sportType": {
                        mSportType.setText(dataSnapshot.getValue().toString());
                        break;
                    }
                    case "passwd": {
                        password = dataSnapshot.getValue().toString();
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
        mChatGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (players.contains(usrid) == false) {
                    Toast.makeText(ViewGameActivity.this, "Join game first to chat", Toast.LENGTH_LONG).show();
                } else {
                    Intent chatIntent = new Intent(ViewGameActivity.this, ChatActivity.class);
                    chatIntent.putExtra("is_group", true);
                    chatIntent.putExtra("the_game_id", gameId);
                    startActivity(chatIntent);
                }

            }
        });


        mJoinGame = findViewById(R.id.join_Game_B);
        mJoinGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                joinGame(myref);

            }
        });
    }

    public void joinGame(DatabaseReference ref) {
        String inputPasswd = mPasswd.getEditText().getText().toString();
        ref.child("nowNumOfPlayer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nowNumOfppl = Integer.parseInt(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (players.contains(usrid)) {
            Toast.makeText(ViewGameActivity.this, "You have already joined the game", Toast.LENGTH_LONG).show();
            return;

        } else if (isPrivate && !inputPasswd.equals(password)) {
            Toast.makeText(ViewGameActivity.this, "Your password is incorrect", Toast.LENGTH_LONG).show();
            return;
        } else if (players.size() <= nowNumOfppl) {
            Toast.makeText(ViewGameActivity.this, "This game already has maximum number of players", Toast.LENGTH_LONG).show();
            return;
        } else {
            FirebaseUser currentUse = FirebaseAuth.getInstance().getCurrentUser();
            String usrid = currentUse.getUid();
            ref.child("player").push().setValue(usrid);
            ref.child("nowNumOfPlayer").setValue(nowNumOfppl + 1);
            ref = FirebaseDatabase.getInstance().getReference().child("Users").child(usrid);
            HashMap<String, String> hmap = new HashMap<String, String>();
            hmap.put("gameName", gameName);
            hmap.put("sportType", gameType);
            ref.child("participated games").push().setValue(hmap);
            Toast.makeText(ViewGameActivity.this, "Succeed", Toast.LENGTH_LONG).show();


        }
    }


}
