package com.health.myapplication.ui.suggest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.health.myapplication.R;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class ExerciseFragment extends Fragment {

    private CardView cardViewExercise;
    private TextView textViewCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);

        // Initialize views
        cardViewExercise = view.findViewById(R.id.cardViewExercise);
        textViewCard = view.findViewById(R.id.textViewCard);

        // Set up any additional logic or data binding here

        return view;
    }
}
