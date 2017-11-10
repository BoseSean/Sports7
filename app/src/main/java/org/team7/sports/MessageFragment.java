package org.team7.sports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.apache.commons.lang3.StringUtils;
import org.team7.sports.model.Chat;

import static org.team7.sports.Util.TimeUtil.getTimeAgo;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    protected Query mChatQuery;
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
        messageList = mainView.findViewById(R.id.chat_list);
        messageList.setHasFixedSize(true);
        messageList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        chatsDatabase = FirebaseDatabase.getInstance().getReference().child("UserChats").child(current_user_id);
        accountsDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        return mainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        mChatQuery = FirebaseDatabase.getInstance().getReference().child("UserChats").child(current_user_id);
        mChatQuery.keepSynced(true);
        FirebaseRecyclerOptions chatsRecyclerOptions = new FirebaseRecyclerOptions.Builder<Chat>()
                .setQuery(mChatQuery, Chat.class)
                .setLifecycleOwner(this)
                .build();
        FirebaseRecyclerAdapter<Chat, ChatsViewHolder> chatsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Chat, ChatsViewHolder>(chatsRecyclerOptions) {
                    @Override
                    public ChatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.messages_single_chat, parent, false);
                        return new ChatsViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(final ChatsViewHolder holder, int position, Chat model) {
                        holder.setTime(model.getLastTime());
                        holder.setMessage(model.getLatestMessage());
                        boolean isgroup = model.getIsGroup();


                        if (isgroup == false) {
                            final String message_sender_id = getRef(position).getKey();


                            DatabaseReference senderDatabase = chatsDatabase.child(message_sender_id);
                            senderDatabase.keepSynced(true);

                            senderDatabase.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String newMessage = dataSnapshot.child("latestMessage").getValue().toString();
                                    holder.setMessage(newMessage);

                                    String senderID = dataSnapshot.getKey();
                                    accountsDatabase.child(senderID).child("name").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue() != null)
                                                holder.setSenderName(dataSnapshot.getValue().toString());
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
                            holder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent profileIntent = new Intent(getActivity(), ChatActivity.class);
                                    profileIntent.putExtra("that_user_id", message_sender_id);
                                    startActivity(profileIntent);
                                }
                            });
                        } else {
                            final String gameid = getRef(position).getKey();
                            holder.setTime(model.getLastTime());
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("GameThread").child(gameid);
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String name = dataSnapshot.child("gameName").getValue().toString();
                                    holder.setSenderName(name);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            DatabaseReference gameChatReference = FirebaseDatabase.getInstance().getReference().child("UserChats").child(current_user_id).child(gameid);
                            gameChatReference.keepSynced(true);
                            gameChatReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String newMessage = dataSnapshot.child("latestMessage").getValue().toString();
                                    holder.setMessage(newMessage);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            holder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent groupChatIntent = new Intent(getActivity(), ChatActivity.class);
                                    groupChatIntent.putExtra("is_group", true);
                                    groupChatIntent.putExtra("the_game_id", gameid);
                                    startActivity(groupChatIntent);
                                }
                            });

                        }

                    }





        };
        messageList.setAdapter(chatsRecyclerViewAdapter);

    }


    public static class ChatsViewHolder extends RecyclerView.ViewHolder {
        public View mView;

        public ChatsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setSenderName(String name) {
            TextView userNameView = mView.findViewById(R.id.friend_single_name);
            userNameView.setText(StringUtils.abbreviate(name, 14));
        }

        public void setTime(long time) {
            TextView userNameView = mView.findViewById(R.id.chats_single_time);
            userNameView.setText(getTimeAgo(time));

        }
        public void setMessage(String message) {
            TextView userNameView = mView.findViewById(R.id.chats_single_message);
            userNameView.setText(StringUtils.abbreviate(message, 26));

        }
    }

}
