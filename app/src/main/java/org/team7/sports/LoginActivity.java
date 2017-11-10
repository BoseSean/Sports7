package org.team7.sports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout Email;
    private TextInputLayout Password;
    private Button LoginBtn;
    private ProgressDialog LoginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        Email = (TextInputLayout) findViewById(R.id.login_email);
        Password = (TextInputLayout) findViewById(R.id.login_password);
        LoginBtn = (Button) findViewById(R.id.login_login_btn);
        LoginProgress = new ProgressDialog(this);
        LoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email = Email.getEditText().getText().toString();
                String password = Password.getEditText().getText().toString();
                if (email.isEmpty()) Email.setError("Email cannot be empty");
                else if (password.isEmpty()) Password.setError("Password cannot be empty");
                else if (!isEmailValid(email)) Email.setError("Email is invalid");
                else {
                    LoginProgress.setTitle(R.string.login);
                    LoginProgress.setCanceledOnTouchOutside(false);
                    LoginProgress.show();
                    loginUser(email, password);
                }
            }
        });
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private void loginUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        LoginProgress.dismiss();
                        if (task.isSuccessful()) {
                            Intent mainIntent = new Intent( LoginActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this,R.string.login_failed, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
