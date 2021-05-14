package com.example.eling.fitnessapp.training;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TrainingFragment extends Fragment {

    @BindView(R.id.recyclerTraining)
    RecyclerView recyclerViewTraining;

    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private Query query, query2;


    private TrainingDetails trainingDetails;
    private List<TrainingDetails> trainingDetailsList;

    private TrainingAdapter adapter;


    private TrainingClickListener trainingClickListener = new TrainingClickListener() {
        @Override
        public void onClick(TrainingDetails trainingDetails) {
            openTraining(trainingDetails);
        }

        @Override
        public void onLongClick(TrainingDetails trainingDetails) {
            deleteTraining(trainingDetails);
        }
    };


    public TrainingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_training, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        trainingDetailsList = new ArrayList<>();

        adapter = new TrainingAdapter(trainingClickListener, trainingDetailsList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewTraining.setLayoutManager(linearLayoutManager);
        recyclerViewTraining.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTraining.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("training").child(user.getUid());
        databaseReference.keepSynced(true);

        query = databaseReference.orderByChild(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    fetchData(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void fetchData(DataSnapshot dataSnapshot) {

        if (dataSnapshot != null) {
            for (DataSnapshot ds : dataSnapshot.getChildren()){
                trainingDetails = new TrainingDetails();

                trainingDetails.setTrainingID(ds.getValue(TrainingDetails.class).getTrainingID());
                trainingDetails.setTraining(ds.getValue(TrainingDetails.class).getTraining());
                trainingDetails.setDate(ds.getValue(TrainingDetails.class).getDate());

                trainingDetailsList.add(trainingDetails);
            }
            adapter = new TrainingAdapter(trainingClickListener,trainingDetailsList);
            recyclerViewTraining.setAdapter(adapter);
        }

    }

    @OnClick(R.id.fabButtonTraining)
    public void addNewTraining() {
        Activity activity = this.getActivity();
        startActivity(new Intent(activity, NewTrainingActivity.class));
    }

    private void openTraining(TrainingDetails trainingDetails) {

        Activity activity = this.getActivity();
        Intent intent = new Intent(activity, TrainingDetailsActivity.class);
        Bundle extras = new Bundle();

        extras.putString(Constants.TRAINING_DATE, trainingDetails.getDate());
        extras.putString(Constants.TRAINING, trainingDetails.getTraining());
        extras.putString(Constants.TRAINING_ID, trainingDetails.getTrainingID());

        intent.putExtras(extras);
        startActivity(intent);

    }


    private void deleteTraining(TrainingDetails trainingDetails) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        query = databaseReference.orderByChild(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    query2 = databaseReference.orderByChild(trainingDetails.getTrainingID());
                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                builder.setMessage("Do you want to delete this training?")
                                        .setTitle("Delete training")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                    if(ds.getValue(TrainingDetails.class).getTrainingID().equals(trainingDetails.getTrainingID())) {
                                                        ds.getRef().removeValue();
                                                        Toast.makeText(getActivity(), "Training succesfully deleted.", Toast.LENGTH_SHORT).show();
                                                        updateView();
                                                    }
                                                }
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Toast.makeText(getActivity(), "Training was not deleted.", Toast.LENGTH_SHORT).show();
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

    private void updateView() {

        trainingDetailsList.clear();
        adapter = new TrainingAdapter(trainingClickListener, trainingDetailsList);
        recyclerViewTraining.setAdapter(adapter);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    fetchData(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
