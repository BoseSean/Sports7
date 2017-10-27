package org.team7.sports;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import org.team7.sports.model.Message;

import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    private static final DatabaseReference messageBaseDatabase =
            FirebaseDatabase.getInstance().getReference().child("ChatThreads");
    private FirebaseAuth mAuth;

    private String thatUserId;
    private String thisUserId;
    private String mCurrentChatThread;
    private DatabaseReference thatUserChatDatabase;
    private DatabaseReference thisUserChatDatabase;
    private DatabaseReference chatDatabase;

    private DatabaseReference accountsDatabase;

    private RecyclerView messageList;
    private Toolbar mainToolBar;
    private ImageButton sendBtn;
    private EditText chatMessageInput;

    public ChatActivity() {
    }

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

        chatMessageInput = (EditText) findViewById(R.id.chat_message_input);
        sendBtn = (ImageButton) findViewById(R.id.chat_send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = chatMessageInput.getText().toString().trim();
                if (!message.equals("")) {
                    send_message(message);
                }
            }
        });
        messageList = findViewById(R.id.chat_messages_list);
        messageList.setHasFixedSize(true);
        LinearLayoutManager messageLinearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        messageLinearLayoutManager.setReverseLayout(true);
        messageList.setLayoutManager(messageLinearLayoutManager);

        thatUserId = getIntent().getStringExtra("that_user_id");
        thisUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mCurrentChatThread = hashChatThread(thatUserId, thisUserId);

        thisUserChatDatabase = FirebaseDatabase.getInstance().getReference().child("UserChats").child(thisUserId).child(thatUserId);
        thatUserChatDatabase = FirebaseDatabase.getInstance().getReference().child("UserChats").child(thatUserId).child(thisUserId);
        chatDatabase = messageBaseDatabase.child(mCurrentChatThread);
        thisUserChatDatabase.keepSynced(true);
        thatUserChatDatabase.keepSynced(true);
        chatDatabase.keepSynced(true);

        accountsDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(chatDatabase, Message.class)
                        .setLifecycleOwner(this)
                        .build();
        FirebaseRecyclerAdapter<Message, MessagesViewAdapter> messagesRecyclerViewAdapter = new FirebaseRecyclerAdapter<Message, MessagesViewAdapter>(options) {

            @Override
            public MessagesViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_single_message, parent, false);
                return new MessagesViewAdapter(view);
            }

            @Override
            protected void onBindViewHolder(final MessagesViewAdapter holder, int position, Message model) {
                holder.setMessage(model.getMessage());
                if (model.getSender().equals(thisUserId)) {
                    accountsDatabase.child(thisUserId).child("name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            holder.setSenderName(dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else if ((model.getSender().equals(thatUserId))) {
                    accountsDatabase.child(thatUserId).child("name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            holder.setSenderName(dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

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

    private void send_message(String message) {

        // TODO improve data consistency of pushing new messages
        HashMap messageMap = new HashMap();
        messageMap.put("message", message);
        messageMap.put("sender", thisUserId);
        messageMap.put("time", ServerValue.TIMESTAMP);

        HashMap messageSnapMap = new HashMap();
        messageSnapMap.put("latestMessage", message);
        messageSnapMap.put("lastTime:", ServerValue.TIMESTAMP);


        chatDatabase.push().setValue(messageMap);

//        HashMap updateMap = new HashMap();
//        updateMap.put(chatDatabase, messageMap);
//        updateMap.put(thisUserChatDatabase, messageSnapMap);
//        updateMap.put(thatUserChatDatabase, messageSnapMap);

        messageSnapMap.put("latestMessage", message);
        messageSnapMap.put("lastTime:", ServerValue.TIMESTAMP);
        thisUserChatDatabase.updateChildren(messageSnapMap);
        thisUserChatDatabase.setValue(messageSnapMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    chatMessageInput.setText("");
                }
            }
        });

    }

    public static class MessagesViewAdapter extends RecyclerView.ViewHolder {
        public View mView;

        public MessagesViewAdapter(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setSenderName(String name) {
            TextView userNameView = mView.findViewById(R.id.message_sender_name);
            userNameView.setText(name);
        }

        public void setTime(String time) {
            //TODO humanize time print
            TextView userNameView = mView.findViewById(R.id.message_time);
            userNameView.setText(time);

        }

        public void setMessage(String message) {

            TextView userNameView = mView.findViewById(R.id.message_message_content);
            userNameView.setText(message);

        }
    }
}
