package com.health.myapplication.ui.suggest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.health.myapplication.R;

public class SuggestFragment extends Fragment {

    public SuggestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_suggest, container, false);

        ViewPager viewPager = view.findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        final Fragment dietFragment = new DietFragment();
        final Fragment exerciseFragment = new ExerciseFragment();
        final Fragment musicFragment = new MusicFragment();

        Button btnDiet = view.findViewById(R.id.btnDiet);
        Button btnExercise = view.findViewById(R.id.btnExercise);
        Button btnMusic = view.findViewById(R.id.btnMusic);

        btnDiet.setOnClickListener(v -> replaceFragment(dietFragment));
        btnExercise.setOnClickListener(v -> replaceFragment(exerciseFragment));
        btnMusic.setOnClickListener(v -> replaceFragment(musicFragment));

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new DietFragment(), "Diet");
        adapter.addFragment(new ExerciseFragment(), "Exercise");
        adapter.addFragment(new MusicFragment(), "Music");
        viewPager.setAdapter(adapter);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
