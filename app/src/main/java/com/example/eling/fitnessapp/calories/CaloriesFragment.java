package com.example.eling.fitnessapp.calories;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CaloriesFragment extends Fragment {

    @BindView(R.id.recyclerCalories)
    RecyclerView recyclerViewCalories;

    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private Query query, query2;

    private CalorieDetails calorieDetails;
    private List<CalorieDetails> calorieDetailsList;

    private CaloriesAdapter adapter;

    private CalorieClickListener calorieClickListener = new CalorieClickListener() {
        @Override
        public void onClick(CalorieDetails calorieDetails) {
            openCalories(calorieDetails);
        }

        @Override
        public void onLongClick(CalorieDetails calorieDetails) {
            deleteCalories(calorieDetails);
        }
    };

    public CaloriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        calorieDetailsList = new ArrayList<>();

        adapter = new CaloriesAdapter(calorieClickListener, calorieDetailsList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewCalories.setLayoutManager(linearLayoutManager);
        recyclerViewCalories.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCalories.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("userCalories").child(user.getUid());
        databaseReference.keepSynced(true);

        query = databaseReference.orderByChild(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    fetchData(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calories, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        ButterKnife.bind(this,view);

    }

    private void fetchData(DataSnapshot dataSnapshot){
        if(dataSnapshot!=null){
            for (DataSnapshot ds: dataSnapshot.getChildren()){
                calorieDetails = new CalorieDetails();

                calorieDetails.setCaloriesID(ds.getValue(CalorieDetails.class).getCaloriesID());
                calorieDetails.setDate(ds.getValue(CalorieDetails.class).getDate());
                calorieDetails.setTotalCalories(ds.getValue(CalorieDetails.class).getTotalCalories());
                calorieDetailsList.add(calorieDetails);
            }
            adapter = new CaloriesAdapter(calorieClickListener, calorieDetailsList);
            recyclerViewCalories.setAdapter(adapter);
        }
    }

    @OnClick(R.id.fabButtonCalories)
    public void addNewCalories(){
        startActivity(new Intent(getActivity(), AddNewCaloriesActivity.class));
    }


    private void deleteCalories(CalorieDetails calorieDetails) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        query = databaseReference.orderByChild(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    query2 = databaseReference.orderByChild(calorieDetails.getCaloriesID());
                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                builder.setMessage("Do you want to delete this calorie entry?")
                                        .setTitle("Delete calories for this day")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                for (DataSnapshot ds:dataSnapshot.getChildren()){
                                                    if(ds.getValue(CalorieDetails.class).getCaloriesID().equals(calorieDetails.getCaloriesID())){
                                                        ds.getRef().removeValue();
                                                        Toast.makeText(getActivity(),"Succesfully deleted", Toast.LENGTH_SHORT).show();
                                                        updateView();
                                                    }
                                                }
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        }).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void openCalories(CalorieDetails calorieDetails) {

        Activity activity = this.getActivity();
        Intent intent = new Intent(activity, CalorieDetailsActivity.class);
        Bundle extras = new Bundle();

        extras.putString(Constants.CALORIE_DATE, calorieDetails.getDate());
        extras.putFloat(Constants.CALORIE_TOTAL, calorieDetails.getTotalCalories());
        extras.putString(Constants.CALORIE_ID, calorieDetails.getCaloriesID());

        intent.putExtras(extras);
        startActivity(intent);

    }

    private void updateView(){
        calorieDetailsList.clear();
        adapter = new CaloriesAdapter(calorieClickListener,calorieDetailsList);
        recyclerViewCalories.setAdapter(adapter);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    fetchData(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
