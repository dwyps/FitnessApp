package com.example.eling.fitnessapp.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eling.fitnessapp.MainActivity;
import com.example.eling.fitnessapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.editText_emailRegister)
    EditText editText_Email;
    @BindView(R.id.editText_usernameRegister)
    EditText editText_Username;
    @BindView(R.id.editText_passwordRegister)
    EditText editText_Password;


    private FirebaseAuth auth;
    private DatabaseReference reference;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.button_Register)
    public void registerUser() {
        final String email = editText_Email.getText().toString();
        String password = editText_Password.getText().toString();
        final String username = editText_Username.getText().toString();

        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please fill out all requiered fields", Toast.LENGTH_SHORT).show();
        } else {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_LONG).show();
                        user = auth.getCurrentUser();
                        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                        reference.keepSynced(true);
                        reference.child("email").setValue(email);
                        reference.child("username").setValue(username);
                        reference.child("userID").setValue(user.getUid());
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @OnClick(R.id.loginHere_label)
    public void backToLogin() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

}


