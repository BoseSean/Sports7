package org.team7.sports;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.team7.sports.model.Message;

public class ChatActivity extends AppCompatActivity {

    private static final DatabaseReference messageDatabase =
            FirebaseDatabase.getInstance().getReference().child("ChatThreads");
    private FirebaseAuth mAuth;
    private Query mMessageQuery;
    private String otherUserId;
    private RecyclerView messageList;
    private Toolbar mainToolBar;
    private String mCurrentUserId;
    private String mCurrentChatThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.chat_tool_bar);
        setSupportActionBar(toolbar);

        otherUserId = getIntent().getStringExtra("other_user_id");
        messageList = findViewById(R.id.chat_messages_list);
        messageList.setHasFixedSize(true);
        messageList.setLayoutManager(new LinearLayoutManager(ChatActivity.this));

        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();
        mCurrentChatThread = hashChatThread(otherUserId, mCurrentUserId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMessageQuery = messageDatabase.child(mCurrentChatThread);
        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(mMessageQuery, Message.class)
                        .setLifecycleOwner(this)
                        .build();
        FirebaseRecyclerAdapter<Message, MessagesViewAddapter> messagesRecyclerViewAdapter = new FirebaseRecyclerAdapter<Message, MessagesViewAddapter>(options) {

            @Override
            public MessagesViewAddapter onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_single_message, parent, false);
                return new MessagesViewAddapter(view);
            }

            @Override
            protected void onBindViewHolder(MessagesViewAddapter holder, int position, Message model) {
                holder.setMessage(model.getMessage());

            }
        };
        messageList.setAdapter(messagesRecyclerViewAdapter);
    }

    private String hashChatThread(String uid1, String uid2) {
        if (uid1.compareTo(uid2) < 0) {
            return uid1 + "+" + uid2;
        } else {
            return uid2 + "+" + uid1;
        }
    }

    public static class MessagesViewAddapter extends RecyclerView.ViewHolder {
        public View mView;

        public MessagesViewAddapter(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setSenderName(String name) {
            TextView userNameView = mView.findViewById(R.id.message_sender_name);
            userNameView.setText(name);
        }

        public void setTime(String time) {

            TextView userNameView = mView.findViewById(R.id.message_time);
            userNameView.setText(time);

        }

        public void setMessage(String message) {

            TextView userNameView = mView.findViewById(R.id.message_message_content);
            userNameView.setText(message);

        }
    }
}
