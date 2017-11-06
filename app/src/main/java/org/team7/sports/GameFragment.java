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
import org.team7.sports.model.Game;


/**
 * A simple {@link Fragment} subclass.
 */


public class GameFragment extends Fragment {
    protected Query gameQuery;
    protected FirebaseRecyclerAdapter<Game, GameListViewHolder> gameRecyclerViewAdapter;
    private RecyclerView gameList;
    private DatabaseReference gameDatabase;
    private FirebaseAuth mAuth;
    private View mainView;
    private FirebaseUser currentUse;
    private Button mCreateGame;


    public GameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_game, container, false);

        mCreateGame = mainView.findViewById(R.id.create_new_game_B);

        mAuth = FirebaseAuth.getInstance();
        currentUse = mAuth.getCurrentUser();

        gameList = mainView.findViewById(R.id.Game_RV);
        gameList.setHasFixedSize(true);
        gameList.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        gameDatabase = FirebaseDatabase.getInstance().getReference().child("GameThread");

        return mainView;
    }


    @Override

    public void onStart() {
        super.onStart();

        //mCreateGame= (Button)GameListViewHolder.gView.findViewById(R.id.create_new_game_B);
        gameQuery = FirebaseDatabase.getInstance().getReference().child("gameThread");
        gameQuery.keepSynced(true);


        //Log.d("ddd", "onbind()");
        mCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent create_game_intent = new Intent(getActivity(), CreateGameActivity.class);
                startActivity(create_game_intent);
            }
        });

        FirebaseRecyclerOptions<Game> gameRecyclerOptions = new FirebaseRecyclerOptions.Builder<Game>()
                .setQuery(gameQuery, Game.class)
                .setLifecycleOwner(this)
                .build();

        gameRecyclerViewAdapter = new FirebaseRecyclerAdapter<Game, GameListViewHolder>(gameRecyclerOptions) {

            public GameListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_game_ingamelist, parent, false);
                Log.d("ddd", "create adapter");
                return new GameListViewHolder(view);
            }


            protected void onBindViewHolder(final GameListViewHolder holder, final int position, Game model) {
                holder.setGameName(model.getGameName());
                holder.setSportType(model.getSportType());
                final String thisGameName = model.getGameName();
                final String thisGameId = model.getGameId();


                DatabaseReference single_game_reference = gameDatabase.child(getRef(position).getKey());

                single_game_reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Game g = (Game)dataSnapshot.getValue();
                        String gameName = dataSnapshot.child("gameName").getValue().toString();
                        holder.setGameName(gameName);


                        String sportType = dataSnapshot.child("sportType").getValue().toString();
                        holder.setSportType(sportType);
                        //holder.setGameName(g.getGameName());
                        //holder.setSportType(g.getSportType());
                        //gameRecyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                holder.gView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent gameDetailIntent = new Intent(getActivity(), ViewGameActivity.class);
                        gameDetailIntent.putExtra("this_game_id", getRef(position).getKey());
                        startActivity(gameDetailIntent);
                    }
                });
            }

        };
        Log.d("ddd", "onbind()");
        gameList.setAdapter(gameRecyclerViewAdapter);




    }



    private static class GameListViewHolder extends RecyclerView.ViewHolder {
        public View gView;
        TextView gameNameView;
        TextView sportTypeView;


        public GameListViewHolder(View itemView) {
            super(itemView);
            gView = itemView;
            gameNameView = gView.findViewById(R.id.game_single_name);
            sportTypeView = gView.findViewById(R.id.game_single_type);
        }

        public void setGameName(String gName) {

            gameNameView.setText(StringUtils.abbreviate(gName, 26));
        }

        public void setSportType(String sType) {

            sportTypeView.setText(StringUtils.abbreviate(sType, 26));
        }
    }


}
