package org.team7.sports;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mainToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: add internet checking
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mainToolBar = (Toolbar) findViewById(R.id.main_page_tool_bar);
        setSupportActionBar(mainToolBar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuUser = mAuth.getCurrentUser();
        if (currentuUser == null) {
            // User is signed in
            toStartActivity();
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
        if(item.getItemId() == R.id.main_logout_btn){
            FirebaseAuth.getInstance().signOut();
            toStartActivity();
        }
        return true;
    }

    private void toStartActivity(){
        Intent startIntent = new Intent( MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }
}
