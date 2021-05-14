package com.example.eling.fitnessapp.training;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eling.fitnessapp.MainActivity;
import com.example.eling.fitnessapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewTrainingActivity extends AppCompatActivity {

    @BindView(R.id.editText_day)
    EditText editText_day;
    @BindView(R.id.editText_month)
    EditText editText_month;
    @BindView(R.id.editText_year)
    EditText editText_year;
    @BindView(R.id.editText_trainingDetails)
    EditText editText_trainingDetails;

    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private Query query;

    public static final String data = "abcdefghijklmnopqrstuvwxyz0123456789";
    public static Random random = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_training);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("training").child(user.getUid());
    }


    @OnClick(R.id.fabButtonAddTraining)
    public void saveTraining() {


        String day = editText_day.getText().toString();
        String month = editText_month.getText().toString();
        String year = editText_year.getText().toString();
        String training = editText_trainingDetails.getText().toString();
        String date = day + "." + month + "." + year;
        String id = randomIDGenerator();

        if (day.isEmpty() || month.isEmpty() || year.isEmpty() || training.isEmpty()) {
            Toast.makeText(this, "Every field needs to be entered", Toast.LENGTH_SHORT).show();
        } else {
            databaseReference.child(id).child("date").setValue(date);
            databaseReference.child(id).child("training").setValue(training);
            databaseReference.child(id).child("trainingID").setValue(id);
            Toast.makeText(this, "Training succesfully saved.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(NewTrainingActivity.this, MainActivity.class));
            finish();
        }
    }

    public String randomIDGenerator() {

        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            sb.append(data.charAt(random.nextInt(data.length())));
        }

        return sb.toString();

    }

}
