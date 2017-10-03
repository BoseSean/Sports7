package org.team7.sports;

import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout Email;
    private TextInputLayout Password;
    private Button LoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // TODO: add better keyboard for email and password
        Email = (TextInputLayout) findViewById(R.id.login_email);
        Password = (TextInputLayout) findViewById(R.id.login_password);
        LoginBtn = (Button) findViewById(R.id.login_login_btn);

        LoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // TODO: add exception handlling on null input of each fields
                // TODO: add validation and handle invalide
                String email = Email.getEditText().getText().toString();
                String username = Email.getEditText().getText().toString();
                String password = Password.getEditText().getText().toString();

                loginUser(email, password);
                toMainActivity();
            }
        });
    }

    private void loginUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            toMainActivity();
                        }
                    }
                });
    }
    private void toMainActivity(){
        Intent startIntent = new Intent( LoginActivity.this, MainActivity.class);
        startActivity(startIntent);
        finish();
    }

}
