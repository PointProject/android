package com.pointproject.pointproject.ui.leaders;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pointproject.pointproject.AbstractFragment;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.adapter.LeadersAdapter;
import com.pointproject.pointproject.model.LeadersModel;
import com.pointproject.pointproject.ui.crystals.CrystalsContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeadersMainFragment extends AbstractFragment implements LeadersContract.View{

    private final static int LAYOUT = R.layout.leaders_fragment;

    @Inject LeadersContract.Presenter presenter;

    private LeadersAdapter adapter;

    @BindView(R.id.leadersRecycleView)
    RecyclerView recyclerView;

    @Inject
    public LeadersMainFragment(){

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(llm);
//        adapter = new LeadersAdapter(getContext(), populateList());
//        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onPause() {
        presenter.dropView();
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private List<LeadersModel> populateList() {
        List<LeadersModel> list = new ArrayList<>();
        list.add(new LeadersModel("nick", "100", "https://cdn.pixabay.com/photo/2016/03/28/12/35/cat-1285634_960_720.png"));
        list.add(new LeadersModel("nick", "100", "https://static.pexels.com/photos/248797/pexels-photo-248797.jpeg"));
        list.add(new LeadersModel("nick", "100", "https://cdn.pixabay.com/photo/2017/01/06/23/21/soap-bubble-1959327_960_720.jpg"));
        list.add(new LeadersModel("nick", "100", "https://wallpaperbrowse.com/media/images/3848765-wallpaper-images-download.jpg"));
        return list;
    }

}
