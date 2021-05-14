package com.example.eling.fitnessapp.training;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eling.fitnessapp.Constants;
import com.example.eling.fitnessapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TrainingDetailsActivity extends AppCompatActivity {

    @BindView(R.id.textView_trainingDateDetails)
    TextView textView_trainingDate;
    @BindView(R.id.editText_TrainingDetailsDetails)
    EditText editText_trainingDetails;
    @BindView(R.id.button_updateTraining)
    Button button_updateTraining;
    @BindView(R.id.button_saveUpdate)
    Button button_saveUpdate;
    @BindView(R.id.textView_TrainingDetailsDetails)
    TextView textView_trainingDetails;

    Intent intent;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_details);
        ButterKnife.bind(this);

        intent = getIntent();
        extras = intent.getExtras();

        setDetails();
    }

    private void setDetails() {
        textView_trainingDate.setText(extras.getString(Constants.TRAINING_DATE));
        textView_trainingDetails.setText(extras.getString(Constants.TRAINING));
        editText_trainingDetails.setText(extras.getString(Constants.TRAINING));
    }


    @OnClick(R.id.button_updateTraining)
    public void updateTraining() {

        textView_trainingDetails.setVisibility(View.GONE);
        editText_trainingDetails.setVisibility(View.VISIBLE);
        button_updateTraining.setVisibility(View.GONE);
        button_saveUpdate.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.button_saveUpdate)
    public void saveUpdate() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String trainingID = extras.getString(Constants.TRAINING_ID);

        DatabaseReference reference = database.getReference().child("training").child(user.getUid()).child(trainingID);

        String trainingDetails = editText_trainingDetails.getText().toString();
        reference.child("training").setValue(trainingDetails);

        textView_trainingDetails.setText(editText_trainingDetails.getText().toString());
        editText_trainingDetails.setVisibility(View.GONE);
        textView_trainingDetails.setVisibility(View.VISIBLE);
        button_updateTraining.setVisibility(View.VISIBLE);
        button_saveUpdate.setVisibility(View.GONE);


    }
}
