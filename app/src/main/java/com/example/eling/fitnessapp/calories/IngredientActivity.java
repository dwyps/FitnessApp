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

public class IngredientActivity extends AppCompatActivity {

    @BindView(R.id.editText_enterIngredient)
    EditText editText_enterIngredient;
    @BindView(R.id.editText_measurement)
    EditText editText_measurement;
    @BindView(R.id.button_addIngredient)
    Button button_addIngredient;
    @BindView(R.id.editText_enterCalories)
    EditText editText_enterCalories;
    @BindView(R.id.button_saveToBase)
    Button button_saveToBase;
    @BindView(R.id.textView_totalCaloriesIngredient)
    TextView textView_totalCalories;

    FirebaseDatabase database;
    DatabaseReference reference;
    Query query;

    float calories;
    float totalCalories;
    Ingredient ingredientCalories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        ButterKnife.bind(this);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("ingredients");

    }

    @OnClick(R.id.button_search)
    public void searchIngredients() {

        String ingredient = editText_enterIngredient.getText().toString();
        if(ingredient.isEmpty()){
            Toast.makeText(this, "Enter ingredient name", Toast.LENGTH_SHORT).show();
        }else {
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
    }

    @OnClick(R.id.button_addIngredient)
    public void addIngredient() {
        calories = ingredientCalories.getCalories();
        float measurement = Float.valueOf(editText_measurement.getText().toString());
        totalCalories = totalCalories + ((calories/100)* measurement);
        textView_totalCalories.setText("Total calories: " + totalCalories + "kcal");
        editText_enterIngredient.setText("");
        editText_measurement.setText("");
        editText_measurement.setVisibility(View.INVISIBLE);
        button_addIngredient.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.button_saveEntry)
    public void saveEntra() {

        Intent intent = new Intent(IngredientActivity.this, AddNewCaloriesActivity.class);
        Bundle extras = new Bundle();

        extras.putFloat(Constants.CALORIE_AMOUNT, totalCalories);
        intent.putExtras(extras);
        startActivity(intent);
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

    @OnClick(R.id.button_saveToBase)
    public void addNewIngredient(){

        String ingredientName = editText_enterIngredient.getText().toString();
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
