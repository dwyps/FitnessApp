package com.example.eling.fitnessapp.profile;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.EditText;
import android.widget.Toast;

import com.example.eling.fitnessapp.MainActivity;
import com.example.eling.fitnessapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.editText_emailLogin)
    EditText editText_Email;
    @BindView(R.id.editText_passwordLogin)
    EditText editText_Password;

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

//        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        checkIfUserLoggedIn();
    }

    public void checkIfUserLoggedIn() {
        user = auth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    @OnClick(R.id.button_login)
    public void loginUser(){

        String email = editText_Email.getText().toString();
        String password = editText_Password.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Enter email and/or password", Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @OnClick(R.id.registerHere_label)
    public void goToRegister() {

        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }
}
