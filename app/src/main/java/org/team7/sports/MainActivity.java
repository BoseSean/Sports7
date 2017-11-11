package org.team7.sports;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static org.team7.sports.Util.WeatherUtil.getWeatherForecast;
import static org.team7.sports.Util.WeatherUtil.giveRecommendation;
import static org.team7.sports.Util.WeatherUtil.isGoodWeather;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mainToolBar;
    private ViewPager viewPager;
    private SectionPagerAdapter sectionPagerAdapter;
    private TabLayout tabLayout;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: add internet checking
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Authentication
        mAuth = FirebaseAuth.getInstance();

        // TODO: change to UserName/ProfileImage or both
        mainToolBar = findViewById(R.id.main_tool_bar);

        setSupportActionBar(mainToolBar);

        // Tabs
        viewPager = findViewById(R.id.main_tabs);
        sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionPagerAdapter);
        tabLayout = findViewById(R.id.main_pager_bar);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.pager_message);
        tabLayout.getTabAt(1).setIcon(R.drawable.pager_game);
        tabLayout.getTabAt(2).setIcon(R.drawable.pager_team);
        tabLayout.getTabAt(3).setIcon(R.drawable.pager_friend);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {  // user is NOT signed in, go to StartActivity
            toStartActivity();
        } else {
            setMainToolBarTitleAsUsername();
            try {
                JSONObject general = getWeatherForecast();
                String weather = general.get("forecast").toString();
                JSONObject temperature = new JSONObject(general.get("temperature").toString());
                int highTemperature=temperature.getInt("high");
                int lowTemperature=temperature.getInt("low");
                int goodWeather = isGoodWeather(weather);

                //display in long period of time
                Toast.makeText(getApplicationContext(), weather+"\nhighest temperature is "+highTemperature+"°C\nlowest temperature is "+lowTemperature+"°C", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), giveRecommendation(highTemperature,lowTemperature,goodWeather), Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.main_logout_btn) {  // sign out, go back to StartActivity
            FirebaseAuth.getInstance().signOut();
            toStartActivity();
        } else if (item.getItemId() == R.id.main_weather_btn) {
            toWeatherActivity();
        }
        return true;
    }

    private void toStartActivity(){
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    private void toWeatherActivity() {
        Intent startIntent = new Intent(MainActivity.this, WeatherActivity.class);
        startActivity(startIntent);
    }

    private void setMainToolBarTitleAsUsername() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("name");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue().toString();
                mainToolBar.setTitle(username);
                Log.i("Username", username);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
