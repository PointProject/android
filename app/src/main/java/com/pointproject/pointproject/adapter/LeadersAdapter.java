package com.pointproject.pointproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.model.LeadersModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xdewnik on 30.12.2017.
 */

public class LeadersAdapter extends RecyclerView.Adapter<LeadersAdapter.LeadersViewHolder> {

    private View view;
    private List<LeadersModel> list;
    private Context context;

    public LeadersAdapter(Context context, List<LeadersModel> list ){
        this.context = context;
        this.list = list;
    }

    @Override
    public LeadersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaders_item, parent, false);
        return new LeadersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LeadersViewHolder holder, int position) {
        LeadersModel item = list.get(position);
        holder.nickname.setText(item.getNick());
        holder.wins.setText(item.getWins());
        Glide.with(context).load(item.getImage())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class LeadersViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.leadersImage)
        ImageView image;
        @BindView(R.id.nicknameLeaders)
        TextView nickname;
        @BindView(R.id.winsLeaders)
        TextView wins;

        LeadersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
