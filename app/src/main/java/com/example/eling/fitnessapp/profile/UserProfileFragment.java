package com.example.eling.fitnessapp.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eling.fitnessapp.R;
import com.example.eling.fitnessapp.calories.CalorieDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserProfileFragment extends Fragment {

    @BindView(R.id.textView_helloUser)
    TextView textView_helloUser;
    @BindView(R.id.textView_usernameProfile)
    TextView textView_username;
    @BindView(R.id.editText_usernameProfile)
    EditText editText_username;
    @BindView(R.id.textView_totalCalories)
    TextView textView_totalCalories;
    @BindView(R.id.textView_totalTrainings)
    TextView textView_totalTrainings;
    @BindView(R.id.imageButton_editUsername)
    ImageButton imageButton_editUsername;
    @BindView(R.id.imageButton_saveUsername)
    ImageButton imageButton_saveUsername;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    Query query;

    String username;
    String calories;
    String trainings;


    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        fetchData();

    }

    private void fetchData() {

        query = reference.child("users").child(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    username = dataSnapshot.getValue(User.class).getUsername();
                    textView_helloUser.setText(username);
                    textView_username.setText(username);
                    editText_username.setText(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        query = reference.child("training").child(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int counter = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        counter++;
                    }
                    trainings = String.valueOf(counter);
                    textView_totalTrainings.setText("Total trainings: " + trainings);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        query = reference.child("userCalories").child(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    float counter = 0;
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        float totalCalories = ds.getValue(CalorieDetails.class).getTotalCalories();
                        counter = counter+totalCalories;
                    }
                    textView_totalCalories.setText("Total calories: "+counter+"kcal");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.imageButton_editUsername)
    public void editUsername() {
        textView_username.setVisibility(View.INVISIBLE);
        imageButton_editUsername.setVisibility(View.INVISIBLE);
        editText_username.setVisibility(View.VISIBLE);
        imageButton_saveUsername.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.imageButton_saveUsername)
    public void saveUsername() {

        username = editText_username.getText().toString();

        textView_username.setText(username);
        textView_helloUser.setText("Hello " + username);
        textView_username.setVisibility(View.VISIBLE);
        imageButton_editUsername.setVisibility(View.VISIBLE);
        editText_username.setVisibility(View.INVISIBLE);
        imageButton_saveUsername.setVisibility(View.INVISIBLE);

        DatabaseReference update = reference.child("users").child(user.getUid());
        update.child("username").setValue(username);

        Toast.makeText(getActivity(), "Username successfully updated.", Toast.LENGTH_SHORT).show();


    }

    @OnClick(R.id.textView_logout)
    public void logoutUser() {
        auth.signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

}
