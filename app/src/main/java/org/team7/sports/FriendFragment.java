package org.team7.sports;

import android.content.Context;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;
import org.team7.sports.model.Account;
import org.team7.sports.model.Game;

import org.team7.sports.R;


public class FriendFragment extends Fragment {
    protected Query friendQuery;
    protected FirebaseRecyclerAdapter<Account, FriendListViewHolder> friendRecyclerViewAdapter;
    private View mainView;
    private RecyclerView friendList;
    private DatabaseReference AccountDatabase;
    private FirebaseAuth mAuth;
    private String current_user_id;
    private FirebaseUser currentUse;
    private Button AddFriend;
    private Button DeleteFriend;

    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_friend, container, false);

        AddFriend = mainView.findViewById(R.id.Friend_Function);
        DeleteFriend = mainView.findViewById(R.id.Friend_Function);

        mAuth = FirebaseAuth.getInstance();
        currentUse = mAuth.getCurrentUser();

        friendList = mainView.findViewById(R.id.friend_list);
        friendList.setHasFixedSize(true);
        friendList.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        AccountDatabase = FirebaseDatabase.getInstance().getReference().child("User");

        mainView = inflater.inflate(R.layout.fragment_friend, container, false);
        friendList = (RecyclerView) mainView.findViewById(R.id.friend_list);
        return mainView;
    }

    public void onStart() {
        super.onStart();

        friendQuery = FirebaseDatabase.getInstance().getReference().child("User"); //GameThread
        friendQuery.keepSynced(true);

        AddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_friend_intent = new Intent(getActivity(), FriendFragment.class);
                startActivity(add_friend_intent);
            }
        });

        FirebaseRecyclerOptions friendRecyclerOptions = new FirebaseRecyclerOptions.Builder<Account>()
                .setQuery(friendQuery, Account.class)
                .setLifecycleOwner(this)
                .build();

        friendRecyclerViewAdapter = new FirebaseRecyclerAdapter<Account, FriendListViewHolder>(friendRecyclerOptions) {

            public FriendListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_friend, parent, false);
                return new FriendListViewHolder(view);
            }

            protected void onBindViewHolder(final FriendListViewHolder holder, final int position, Account model) { // int positon
                holder.setFriendName(model.getName());
                DatabaseReference friend_reference = AccountDatabase.child(getRef(position).getKey());
//                Log.d("haha", "item count is " + friendRecyclerViewAdapter.getItemCount());

                friend_reference.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String friendName = dataSnapshot.child("User").child("friendlist").getValue().toString();
                        holder.setFriendName(friendName);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                holder.fView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent friendDetailIntent = new Intent(getActivity(), FriendFragment.class);
                        friendDetailIntent.putExtra("this_friend_id", getRef(position).getKey()); //single_game_reference
                        startActivity(friendDetailIntent);
                    }
                });
            }

        };
        Log.d("ddd", "onbind()");
        friendList.setAdapter(friendRecyclerViewAdapter);

    }

        public static class FriendListViewHolder extends RecyclerView.ViewHolder {
            public View fView;
            TextView friendListView;

            public FriendListViewHolder(View itemView) {
                super(itemView);
                fView = itemView;
                friendListView = fView.findViewById(R.id.chats_single_name);
            }

            public void setFriendName(String gFriendName) {

                friendListView.setText(StringUtils.abbreviate(gFriendName, 26));
            }
        }
    }


