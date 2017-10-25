package org.team7.sports;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.team7.sports.model.Message;

import java.lang.invoke.MethodHandle;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {


    protected static Query mChatQuery;
    private View mainView;
    private RecyclerView messageList;
    private DatabaseReference messagesDatabase;
    private DatabaseReference accountsDatabase;
    private FirebaseAuth mAuth;
    private String current_user_id;
    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_message, container, false);
        messageList = (RecyclerView) mainView.findViewById(R.id.message_list);
        messageList.setHasFixedSize(true);
        messageList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        messagesDatabase = FirebaseDatabase.getInstance().getReference().child("Chats").child(current_user_id);
        accountsDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mChatQuery = FirebaseDatabase.getInstance().getReference()
                .child("Messages")
                .child(current_user_id);
        FirebaseRecyclerOptions messagesRecyclerOption = new FirebaseRecyclerOptions.Builder<Message>()
                .setIndexedQuery(mChatQuery, )
                .setLifecycleOwner(this)
                .build();
        FirebaseRecyclerAdapter<Message, MessagesViewHolder> messagesRecyclerViewAdapter =
                new FirebaseRecyclerAdapter<Message, MessagesViewHolder>(Message.class, R.layout.messages_single_message, MessagesViewHolder.class, messagesDatabase) {
                    @Override
                    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        return null;
                    }

                    @Override
                    protected void onBindViewHolder(MessagesViewHolder holder, int position, Message model) {

                    }

                    @Override
                    protected void populateViewHolder(final MessagesViewHolder viewHolder, Message message, int position) {
                        final String message_sender_id = getRef(position).getKey();
                        messagesDatabase.child(message_sender_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final String userName = dataSnapshot.child("name").getValue().toString();
                                Log.d("snapshot", dataSnapshot.toString());
                                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
        };
        messageList.setAdapter(messagesRecyclerViewAdapter);
    }


    public static class MessagesViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public MessagesViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }
        public void setTime(String message) {

            TextView userNameView = (TextView) mView.findViewById(R.id.message_single_name);
            userNameView.setText(message);

        }
        public void setMessage(String message) {

            TextView userNameView = (TextView) mView.findViewById(R.id.message_single_name);
            userNameView.setText(message);

        }
    }

}
