package com.example.eling.fitnessapp.calories;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eling.fitnessapp.Constants;
import com.example.eling.fitnessapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalorieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.textView_calorieDateDetails)
    TextView textView_date;
    @BindView(R.id.textView_calorieDetailsDetails)
    TextView textView_calorieDetails;
    @BindView(R.id.button_updateCalories)
    Button button_updateCalories;
    @BindView(R.id.editText_enterIngredientDetails)
    EditText editText_ingredient;
    @BindView(R.id.button_searchDetails)
    Button button_search;
    @BindView(R.id.editText_measurementDetails)
    EditText editText_measurement;
    @BindView(R.id.button_addIngredientDetails)
    Button button_addIngredient;
    @BindView(R.id.editText_enterCaloriesDetails)
    EditText editText_enterCalories;
    @BindView(R.id.button_saveToBaseDetails)
    Button button_saveToBase;
    @BindView(R.id.button_saveEntryDetails)
    Button button_saveEntry;

    Intent intent;
    Bundle extras;

    FirebaseDatabase database;
    DatabaseReference reference;
    Query query;

    float calories;
    float totalCalories;
    Ingredient ingredientCalories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_details);
        ButterKnife.bind(this);

        database = FirebaseDatabase.getInstance();

        intent = getIntent();
        extras = intent.getExtras();

        totalCalories = extras.getFloat(Constants.CALORIE_TOTAL);

        setDetails();

    }

    private void setDetails() {
        textView_date.setText(extras.getString(Constants.CALORIE_DATE));
        textView_calorieDetails.setText("Total calories: " + totalCalories+ "kcal");
    }

    @OnClick(R.id.button_updateCalories)
    public void updateCalories() {
        button_updateCalories.setVisibility(View.INVISIBLE);
        button_saveEntry.setVisibility(View.VISIBLE);
        editText_ingredient.setVisibility(View.VISIBLE);
        button_search.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.button_searchDetails)
    public void searchIngredients(){
        String ingredient = editText_ingredient.getText().toString();
        reference = database.getReference().child("ingredients");
        query = reference.child(ingredient);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ingredientCalories = new Ingredient();
                    ingredientCalories.setCalories(dataSnapshot.getValue(Ingredient.class).getCalories());
                    ingredientCalories.setIngredientName(dataSnapshot.getValue(Ingredient.class).getIngredientName());
                    editText_measurement.setVisibility(View.VISIBLE);
                    button_addIngredient.setVisibility(View.VISIBLE);
                } else {
                    showAlertDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.button_addIngredientDetails)
    public void addIngredient(){
        calories = ingredientCalories.getCalories();
        float measurement = Float.valueOf(editText_measurement.getText().toString());
        totalCalories = totalCalories+((calories/100)*measurement);
        textView_calorieDetails.setText("Total calories: "+totalCalories+"kcal");
        editText_ingredient.setText("");
        editText_measurement.setText("");
        editText_measurement.setVisibility(View.INVISIBLE);
        button_addIngredient.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.button_saveEntryDetails)
    public void saveUpdate(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        String calorieID = extras.getString(Constants.CALORIE_ID);

        reference = database.getReference().child("userCalories").child(user.getUid()).child(calorieID);
        reference.child("totalCalories").setValue(totalCalories);
        Toast.makeText(this, "Calories updated", Toast.LENGTH_SHORT).show();
        editText_ingredient.setVisibility(View.INVISIBLE);
        button_search.setVisibility(View.INVISIBLE);

    }

    private void showAlertDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Do you want to add this ingredient to database?")
                .setTitle("Not in database.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editText_enterCalories.setVisibility(View.VISIBLE);
                        button_saveToBase.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    @OnClick(R.id.button_saveToBaseDetails)
    public void addNewIngredient(){

        String ingredientName = editText_ingredient.getText().toString();
        String calories = editText_enterCalories.getText().toString();

        if(ingredientName.isEmpty() || calories.isEmpty()){
            Toast.makeText(this, "All fields needs to be entered", Toast.LENGTH_SHORT).show();
        }else {
            reference.child(ingredientName).child("ingredientName").setValue(ingredientName);
            reference.child(ingredientName).child("calories").setValue(Float.valueOf(calories));
            Toast.makeText(this,"Succesfully added to database", Toast.LENGTH_SHORT).show();
            button_saveToBase.setVisibility(View.INVISIBLE);
            editText_enterCalories.setVisibility(View.INVISIBLE);
        }

    }
}
