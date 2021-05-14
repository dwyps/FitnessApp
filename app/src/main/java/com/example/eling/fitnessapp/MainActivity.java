package com.example.eling.fitnessapp;

import com.example.eling.fitnessapp.calories.CaloriesFragment;
import com.example.eling.fitnessapp.profile.UserProfileFragment;
import com.example.eling.fitnessapp.training.TrainingFragment;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fragmentContanier)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        tabLayout.addTab(tabLayout.newTab().setText("Calories"));
        tabLayout.addTab(tabLayout.newTab().setText("Training"));
        tabLayout.addTab(tabLayout.newTab().setText("My Profile"));


        MainAdapter adapter = new MainAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        List<Fragment> pages = new ArrayList<>();
        pages.add(new CaloriesFragment());
        pages.add(new TrainingFragment());
        pages.add(new UserProfileFragment());
        adapter.setItems(pages);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });

        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart(){
        super.onStart();
        user = auth.getCurrentUser();
    }
}
