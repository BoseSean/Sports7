package org.team7.sports;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.team7.sports.R;


public class FriendFragment extends Fragment {
    private View mainView;
    private RecyclerView friendView;
    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mainView = inflater.inflate(R.layout.fragment_friend, container, false);
        friendView = (RecyclerView) mainView.findViewById(R.id.friend_list);
        return mainView;
    }

}
