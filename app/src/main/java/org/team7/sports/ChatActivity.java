package org.team7.sports;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {

    private static final DatabaseReference messageDatabase =
            FirebaseDatabase.getInstance().getReference().child("ChatThreads");
    private String otherUserId;
    private RecyclerView messageList;
    private Toolbar mainToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.chat_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        otherUserId = getIntent().getStringExtra("other_user_id");
        messageList = findViewById(R.id.chat_messages_list);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
