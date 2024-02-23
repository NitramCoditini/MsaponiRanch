package com.example.msaponiranch.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.msaponiranch.MainActivity;
import com.example.msaponiranch.R;
import com.example.msaponiranch.SelectClass;
import com.example.msaponiranch.databinding.FragmentGalleryBinding;
import com.example.msaponiranch.livestockactivityclassess.AnimalBreeding;
import com.example.msaponiranch.livestockactivityclassess.AnimalFeeding;
import com.example.msaponiranch.livestockactivityclassess.AnimalList;
import com.example.msaponiranch.livestockactivityclassess.HealthActivity;

public class GalleryFragment extends Fragment {

    CardView cd1,cd2,cd3,cd4;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        cd1 = root.findViewById(R.id.cattleCard);
        cd2 = root.findViewById(R.id.healthCard);
        cd3 = root.findViewById(R.id.feedingCard);
        cd4 = root.findViewById(R.id.breedingCard);

        cd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AnimalList.class);
                startActivity(i);
            }
        });
        cd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), HealthActivity.class);
                startActivity(i);
            }
        });
        cd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AnimalFeeding.class);
                startActivity(i);
            }
        });
        cd4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AnimalBreeding.class);
                startActivity(i);
            }
        });


        return root;
    }

}