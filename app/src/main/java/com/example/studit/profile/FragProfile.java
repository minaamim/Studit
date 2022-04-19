package com.example.studit.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studit.R;

public class FragProfile extends Fragment {
    private View view;

    private final ArrayList<FragProfileViewModel> ViewModelArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    FragProfileViewAdapter FragProfileViewAdapter;


    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_main_profile, container, false);


        super.onCreate(savedInstanceState);
        recyclerView = view.findViewById(R.id.profile_recycler_badge);
        recyclerView.setHasFixedSize(true);

        for(int i=0; i<5; i++){
            ViewModelArrayList.add(new FragProfileViewModel("iconName","badge"+i));
        }
        FragProfileViewAdapter = new FragProfileViewAdapter(ViewModelArrayList);
        recyclerView.setAdapter(FragProfileViewAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        return view;
    }


}
