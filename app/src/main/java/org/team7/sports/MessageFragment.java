package org.team7.sports;


import android.content.Intent;
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

import org.team7.sports.model.Chat;
import org.team7.sports.model.Message;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {


    protected static Query mChatQuery;
    private View mainView;
    private RecyclerView messageList;
    private DatabaseReference chatsDatabase;
    private DatabaseReference accountsDatabase;
    private FirebaseAuth mAuth;
    private String current_user_id;
    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_message, container, false);
        messageList = (RecyclerView) mainView.findViewById(R.id.chat_list);
        messageList.setHasFixedSize(true);
        messageList.setLayoutManager(new LinearLayoutManager(getActivity()));

        TextView text = mainView.findViewById(R.id.textView3);
        text.setText("hello");
        Log.d("ddd", text.getText().toString());

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        chatsDatabase = FirebaseDatabase.getInstance().getReference().child("UserChats").child(current_user_id);
        accountsDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView text = mainView.findViewById(R.id.textView3);
        text.setText("hello");
        mChatQuery = FirebaseDatabase.getInstance().getReference().child("UserChats").child(current_user_id);
        FirebaseRecyclerOptions chatsRecyclerOptions = new FirebaseRecyclerOptions.Builder<Chat>()
                .setQuery(mChatQuery, Chat.class)
                .setLifecycleOwner(this)
                .build();
        FirebaseRecyclerAdapter<Chat, ChatsViewHolder> chatsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Chat, ChatsViewHolder>(chatsRecyclerOptions) {
                    @Override
                    public ChatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.messages_single_chat, parent, false);
                        Log.d("ddd", "79");
                        return new ChatsViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(final ChatsViewHolder holder, int position, Chat model) {

                        holder.setMessage(model.getLatestMessage());
                        final String message_sender_id = getRef(position).getKey();

                        DatabaseReference senderDatabase = chatsDatabase.child(message_sender_id);
//                        senderDatabase.keepSynced(true);
                        Log.d("ddd", "134");

                        senderDatabase.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.d("json", dataSnapshot.toString());
                                Log.d("ddd", "134");
                                String newMessage = dataSnapshot.child("latestMessage").getValue().toString();
                                holder.setMessage(newMessage);

                                String senderID = dataSnapshot.getKey();
                                accountsDatabase.child(senderID).child("name").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        holder.setSenderName(dataSnapshot.child("name").getValue().toString());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

        };
        messageList.setAdapter(chatsRecyclerViewAdapter);
    }


    public static class ChatsViewHolder extends RecyclerView.ViewHolder {

        public View mView;

        public ChatsViewHolder(View itemView) {
            super(itemView);
            Log.d("ddd", "134");
            mView = itemView;

        }

        public void setSenderName(String name) {
            TextView userNameView = mView.findViewById(R.id.message_single_name);
            userNameView.setText(name);
        }
        public void setTime(String message) {

            TextView userNameView = mView.findViewById(R.id.message_single_name);
            userNameView.setText(message);

        }
        public void setMessage(String message) {

            TextView userNameView = mView.findViewById(R.id.message_single_message);
            userNameView.setText(message);

        }
    }

}
