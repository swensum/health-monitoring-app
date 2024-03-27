package com.health.myapplication.ui.suggest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import com.health.myapplication.R;
import androidx.fragment.app.Fragment;


public class MusicFragment extends Fragment {

    private RadioGroup radioGroupMusicType;

    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music, container, false);

        return view;
    }
}