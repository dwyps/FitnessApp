package com.example.eling.fitnessapp.calories;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eling.fitnessapp.Constants;
import com.example.eling.fitnessapp.MainActivity;
import com.example.eling.fitnessapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Random;

public class AddNewCaloriesActivity extends AppCompatActivity {

    @BindView(R.id.editText_dayCalories)
    EditText editText_day;
    @BindView(R.id.editText_monthCalories)
    EditText editText_month;
    @BindView(R.id.editText_yearCalories)
    EditText editText_year;
    @BindView(R.id.textView_totalCaloriesNewEntry)
    TextView textView_totalCalories;

    Intent intent;
    Bundle extras;

    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private Query query;

    public static final String data = "abcdefghijklmnopqrstuvwxyz0123456789";
    public static Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_calories);
        ButterKnife.bind(this);
        intent = getIntent();
        extras = intent.getExtras();

        if(extras!=null){
            textView_totalCalories.setText("Total calories for today: "+extras.getFloat(Constants.CALORIE_AMOUNT)+"kcal");
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("userCalories").child(user.getUid());

    }

    @OnClick(R.id.button_searchIngredients)
    public void searchIngredients(){
        startActivity(new Intent(AddNewCaloriesActivity.this, IngredientActivity.class));
    }

    @OnClick(R.id.fabButton_saveCalories)
    public void saveCalories(){

        String day = editText_day.getText().toString();
        String month = editText_month.getText().toString();
        String year = editText_year.getText().toString();
        float calories = extras.getFloat(Constants.CALORIE_AMOUNT);
        String date = day + "." + month + "." + year;
        String id = randomIDGenerator();

        if (day.isEmpty() || month.isEmpty() || year.isEmpty()) {
            Toast.makeText(this, "Every field needs to be entered", Toast.LENGTH_SHORT).show();
        } else {
            databaseReference.child(id).child("date").setValue(date);
            databaseReference.child(id).child("totalCalories").setValue(calories);
            databaseReference.child(id).child("caloriesID").setValue(id);
            Toast.makeText(this, "Calories succesfully saved.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddNewCaloriesActivity.this, MainActivity.class));
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
