package org.team7.sports;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.team7.sports.model.Game;
import org.team7.sports.model.GamePlayer;

public class ViewGameActivity extends AppCompatActivity {

    private TextView mGameName;
    private TextView mSportType;
    private TextView mDate;
    private TextView mStartTime;
    private TextView mLocation;
    private TextView mHostName;
    private TextView mHostEmail;
    private String gameName;
    private Game g;
    private Button mJoinGame;
    private String gameId;

    private FirebaseDatabase database;
    private DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_game);

        gameId = getIntent().getStringExtra("this_game_id");
        myref = FirebaseDatabase.getInstance().getReference().child("GameThread").child(gameId);
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                g = (Game) dataSnapshot.getValue();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mGameName.setText(gameName);
        mSportType.setText(g.getSportType());
        mDate.setText(g.getDate());
        mStartTime.setText(g.getStartTime());
        mLocation.setText(g.getLocation());
        mHostName.setText(g.getHost().getUserId());
        mHostEmail.setText(g.getHost().getEmail());

        mJoinGame = findViewById(R.id.join_Game_B);
        mJoinGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FirebaseUser currentUse = FirebaseAuth.getInstance().getCurrentUser();
                String uid = currentUse.getUid();
                String email = currentUse.getEmail();
                GamePlayer p = new GamePlayer(uid, email);
                g.addNewPlayer(p);
                myref.setValue(p);
            }
        });





    }
}
