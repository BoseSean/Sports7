package org.team7.sports;

import android.util.Log;
import android.view.View;
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
import java.util.ArrayList;

/**
 * Created by Gerald on 10-Nov-17.
 */

public class FriendActivity extends Fragment{
    protected Query friendQuery;
    protected FirebaseRecyclerAdapter<Account, FriendFragment.FriendListViewHolder> friendRecyclerViewAdapter;
    private View mainView;
    private RecyclerView friendList;
    private DatabaseReference AccountDatabase;
    private FirebaseAuth mAuth;
    private String current_user_id;
    private FirebaseUser currentUse;
    private Button AddFriend;
    private Button DeleteFriend;
    private DatabaseReference FriendNames;


    public FriendActivity(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_game, container, false);
        AddFriend = mainView.findViewById(R.id.Friend_Function);
        DeleteFriend = mainView.findViewById(R.id.Friend_Function);
        mAuth = FirebaseAuth.getInstance();
        currentUse = mAuth.getCurrentUser();
        friendList = mainView.findViewById(R.id.friend_list);
        friendList.setHasFixedSize(true);
        friendList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        AccountDatabase = FirebaseDatabase.getInstance().getReference().child("User");
        FriendNames = FirebaseDatabase.getInstance().getReference().child("User").child("friendList");
        mainView = inflater.inflate(R.layout.fragment_friend, container, false);
        friendList = (RecyclerView) mainView.findViewById(R.id.friend_list);

        return mainView;
    }


    private void AddRemoveUser(final String username, final String id, Boolean add, View thisPlayer)
    {
        Log.d("USERNAME",username + " is");
        Boolean isAdded;

        if(){
            isAdded = false;
        } else {
            isAdded = activePlayers.contains(username);
        }
        if(add){
            //check if player is allready add to the game
            if(!isAdded){
                //add this user to the game
                //remove form all players list
                activePlayers.add(username);
                activeIDs.add(id);
                allUsers.removeView(thisPlayer);
                usersClass.addToGameList(PlayerSelect.this, activeLayout, username, id, new AddUserCallback() {
                    @Override
                    public void addUser(Boolean add, View thisPlayer) {
                        if(add){
                            AddRemoveUser(username,id,true,thisPlayer);
                        } else {
                            AddRemoveUser(username,id,false,thisPlayer);
                        }
                    }
                });
            }
        } else {
            if(isAdded){
                int current = activePlayers.indexOf(username);
                activePlayers.remove(current);
                activeIDs.remove(current);
                activeLayout.removeView(thisPlayer);
                usersClass.addToGameList(PlayerSelect.this, allUsers, username, id, new AddUserCallback() {
                    @Override
                    public void addUser(Boolean add, View thisPlayer) {
                        if(add){
                            AddRemoveUser(username,id,true,thisPlayer);
                        } else {
                            AddRemoveUser(username,id,false,thisPlayer);
                        }
                    }
                });
            }
        }
        Log.d("VIEW",activePlayers.size() + "");
        noActivePlayers.setVisibility((activePlayers.size() > 0)? View.GONE : View.VISIBLE);
        noSavedPlayers.setVisibility((allUsers.getChildCount() > 0) ? View.GONE : View.VISIBLE);
    }
}
