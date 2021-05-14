package com.example.eling.fitnessapp;

import com.example.eling.fitnessapp.calories.CaloriesFragment;
import com.example.eling.fitnessapp.profile.UserProfileFragment;
import com.example.eling.fitnessapp.training.TrainingFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

class MainAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    int numOfTabs;

    public MainAdapter(FragmentManager fragmentManager, int numOfTabs){

        super(fragmentManager);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new CaloriesFragment();
            case 1:
                return new TrainingFragment();
            case 2:
                return new UserProfileFragment();
        }
        return this.fragmentList.get(position);
    }

    public void setItems(List<Fragment> fragments){
        fragmentList.clear();
        fragmentList.addAll(fragments);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
