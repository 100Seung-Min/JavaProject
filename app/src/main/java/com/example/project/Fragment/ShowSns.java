package com.example.project.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.Adapter.DetailViewRecyclerAdapter;
import com.example.project.R;
import com.example.project.databinding.FragmentShowSnsBinding;
import com.example.project.model.DetailViewData;

import java.util.ArrayList;

public class ShowSns extends Fragment {

    FragmentShowSnsBinding binding;
    ArrayList<DetailViewData> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        init_list();
        binding = FragmentShowSnsBinding.inflate(inflater, container, false);
        DetailViewRecyclerAdapter adapter = new DetailViewRecyclerAdapter(getContext(), data);
        binding.snsRecyclerview.setAdapter(adapter);
        binding.snsRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        return binding.getRoot();
    }

    private void init_list(){
        data.add(new DetailViewData("","","",1,1,1));
        data.add(new DetailViewData("","","",1,1,1));
        data.add(new DetailViewData("","","",1,1,1));
        data.add(new DetailViewData("","","",1,1,1));
    }
}