package org.team7.sports;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.team7.sports.model.Game;

public class CreateGameActivity extends AppCompatActivity {

    private TextInputLayout mGameName;
    private TextInputLayout mSportType;
    private TextInputLayout mDate;
    private TextInputLayout mStartTime;
    private TextInputLayout mPasswd;
    private TextInputLayout mNumberofppl;
    private Button mSubmit;
    private Button mCancel;
    private CheckBox mPrivate;
    private ProgressDialog RegisterProgress;
    private TextInputLayout mLocation;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Toolbar toolbar;

    public void createGame(Game g) {
        database = FirebaseDatabase.getInstance();
        final FirebaseUser currentUse = FirebaseAuth.getInstance().getCurrentUser();
        //g.setGameId(currentUse.getUid());
        myRef = database.getReference().child("GameThread");
        //myRef = database.getReference().child("GameThread").child(g.getGameId());
        //HashMap<String, Game> hashmap = new HashMap<String, Game>();
        //hashmap.put(g.getGameName(), g);
        String gameid = myRef.push().getKey();
        myRef = myRef.child(gameid);
        g.setGameId(gameid);

        //TODO Add toolbar to create game page
//        toolbar = (Toolbar) findViewById(R.id.create_game_bar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Game Details");

        myRef.setValue(g).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    myRef = myRef.child("player");
                    myRef.push().setValue(currentUse.getUid());

                    Toast.makeText(CreateGameActivity.this, "succeeded", Toast.LENGTH_LONG).show();
                    onBackPressed();
                } else {
                    Toast.makeText(CreateGameActivity.this, "failed to create", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        mGameName = findViewById(R.id.gameName_TIL);
        mSportType = findViewById(R.id.sportType_TIL);
        mDate = findViewById(R.id.gameDate_TIL);
        mStartTime = findViewById(R.id.startTime_TIL);
        mPasswd = findViewById(R.id.private_password_TIL);
        mNumberofppl = findViewById(R.id.number_TIL);
        mSubmit = findViewById(R.id.game_create_submit_B);
        mCancel = findViewById(R.id.game_create_cancel_B);
        mPrivate = findViewById(R.id.privacy_check_box);
        mLocation = findViewById(R.id.location_TIL);

        RegisterProgress = new ProgressDialog(this);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {


                String gameName = mGameName.getEditText().getText().toString();
                String sportType = mSportType.getEditText().getText().toString();
                int numberOfppl = Integer.parseInt(mNumberofppl.getEditText().getText().toString());
                String passwd;
                boolean isPrivate;
                String date = mDate.getEditText().getText().toString();
                String time = mStartTime.getEditText().getText().toString();
                String location = mLocation.getEditText().getText().toString();

                FirebaseUser currentUse = FirebaseAuth.getInstance().getCurrentUser();
                String uid = currentUse.getUid();
                String email = currentUse.getEmail();
                //GamePlayer host = new GamePlayer(uid, email);
                //host.setIsHost();


                if (mPrivate.isChecked()) {
                    isPrivate = true;
                    passwd = mPasswd.getEditText().getText().toString();
                } else {
                    isPrivate = false;
                    passwd = "n";
                }


                Game g = new Game(passwd, "null", isPrivate, gameName, sportType, location, date, time, numberOfppl, uid, email);

                if (!TextUtils.isEmpty((gameName)) || !TextUtils.isEmpty(sportType) || !TextUtils.isEmpty(mNumberofppl.getEditText().getText().toString())
                        || !TextUtils.isEmpty(date) || !TextUtils.isEmpty(time)) {

                    createGame(g);
                } else {
                    Toast.makeText(CreateGameActivity.this, "need to fill in all the field", Toast.LENGTH_LONG);

                }

            }


        });
    }
}
