package org.team7.sports;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private String hName;
    private CheckBox mPrivate;
    private ProgressDialog RegisterProgress;
    private TextInputLayout mLocation;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Toolbar toolbar;


    public void createGame(String password, Boolean isPrivate, String name,
                           String typeofsport, String location, String date, String starttime,
                           int numberofplayer, String hName, String hEmail) {
        if (name.equals("") || typeofsport.equals("") || location.equals("") || date.equals("") || starttime.equals("")) {
            Toast.makeText(CreateGameActivity.this, "need to fill in all the field", Toast.LENGTH_LONG).show();
            return;
        }
        if (isPrivate && password.equals("")) {
            Toast.makeText(CreateGameActivity.this, "need to fill in all the field", Toast.LENGTH_LONG).show();
            return;
        }
        if (isDateValid(date) == false) {
            Toast.makeText(CreateGameActivity.this, "wrong date format", Toast.LENGTH_LONG).show();
            return;
        }
        if (isTimeValid(starttime) == false) {
            Toast.makeText(CreateGameActivity.this, "wrong time format", Toast.LENGTH_LONG).show();
            return;
        }
        if (numberofplayer < 2) {
            Log.d("number", "number of players : " + numberofplayer);
            Toast.makeText(CreateGameActivity.this, "At least two players required", Toast.LENGTH_LONG).show();
            return;
        }

            Game g = new Game(password, "null", isPrivate, name, typeofsport, location, date, starttime, numberofplayer, hName, hEmail);

            database = FirebaseDatabase.getInstance();
            final FirebaseUser currentUse = FirebaseAuth.getInstance().getCurrentUser();
            myRef = database.getReference().child("GameThread");
            String gameid = myRef.push().getKey();
            myRef = myRef.child(gameid);
            g.setGameId(gameid);
            g.setHostName(hName);

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


    public boolean isDateValid(String date) {
        if (date.charAt(2) != '/' || date.charAt(5) != '/') return false;
        if ((date.charAt(0) - '0') * 10 + (date.charAt(1) - '0') > 31) return false;
        return (date.charAt(3) - '0') * 10 + (date.charAt(4) - '0') <= 12;
    }

    public boolean isTimeValid(String time) {
        if (time.charAt(2) != ':') return false;
        if ((time.charAt(0) - '0') * 10 + (time.charAt(1) - '0') > 24) return false;
        return (time.charAt(0) - '0') * 10 + (time.charAt(1) - '0') <= 59;
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
                int numberOfppl = 0;
                if (mNumberofppl.getEditText().getText().toString().equals("") == false)
                    numberOfppl = Integer.parseInt(mNumberofppl.getEditText().getText().toString());
                String passwd;
                boolean isPrivate;
                String date = mDate.getEditText().getText().toString();
                String time = mStartTime.getEditText().getText().toString();
                String location = mLocation.getEditText().getText().toString();
                FirebaseUser currentUse = FirebaseAuth.getInstance().getCurrentUser();
                String uid = currentUse.getUid();
                String email = currentUse.getEmail();
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("name");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("ddddd", dataSnapshot.getKey());
                        if (dataSnapshot.getKey() == "name") {
                            hName = dataSnapshot.getValue().toString();
                            Log.d("testttt", hName);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if (mPrivate.isChecked()) {
                    isPrivate = true;
                    passwd = mPasswd.getEditText().getText().toString();
                } else {
                    isPrivate = false;
                    passwd = "n";
                }
                createGame(passwd, isPrivate, gameName, sportType, location, date, time, numberOfppl, hName, email);

            }


        });
    }
}
