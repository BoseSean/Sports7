package org.team7.sports;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {


    private View mainView;
    private RecyclerView messageView;

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_message, container, false);
        messageView = (RecyclerView) mainView.findViewById(R.id.message_list);


        return inflater.inflate(R.layout.fragment_message, container, false);
    }

}
