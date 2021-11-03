package com.example.reciperecommenderapp.Adapter;

import com.example.reciperecommenderapp.Category.FragmentBeef;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapter extends FragmentStateAdapter {

    private int numTabs = 12;

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        //pass generated fragment position
        //so can get this position in the onCreateView() of the fragment
        return FragmentBeef.addfrag(position);
    }

    @Override
    public int getItemCount() {
        return numTabs;
    }
}



