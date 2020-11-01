package com.aditya.walktracker.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aditya.walktracker.R;
import com.aditya.walktracker.database.WalkEntity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WalkAdapter extends RecyclerView.Adapter<WalkAdapter.ViewHolder>{

    public ClickListener clickListener;
    private List<WalkEntity> walkEntityList = new ArrayList<>();
    Context ctx;

    public WalkAdapter(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
    public void refresh(List<WalkEntity> walkEntityList){
        this.walkEntityList = walkEntityList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.walk_item, viewGroup, false);
        ctx = viewGroup.getContext();
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) { viewHolder.bind(i); }
    @Override public int getItemCount() { return walkEntityList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView walkIv;
        CardView cardView;
        TextView timeTv, distanceTv, dateTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView  = itemView.findViewById(R.id.card_lay);
            walkIv = itemView.findViewById(R.id.walk_iv);
            dateTv = itemView.findViewById(R.id.date_tv);
            distanceTv = itemView.findViewById(R.id.distance_tv);
            timeTv = itemView.findViewById(R.id.time_tv);
        }

        public void bind(int pos){
            WalkEntity walk = walkEntityList.get(pos);
            cardView.setOnClickListener(this);
            dateTv.setText(ctx.getString(R.string.dateText,walk.getDate()));
            distanceTv.setText(ctx.getString(R.string.distanceText,walk.getDistance()));
            timeTv.setText(ctx.getString(R.string.timeText,walk.getStartTime(),walk.getEndTime()));
            Picasso.get().load(walk.getWalkType()).error(R.drawable.ic_walk_med).into(walkIv);
        }

        @Override public void onClick(View v) { clickListener.walkClicked(walkEntityList.get(getAdapterPosition()).getId()); }
    }

    public interface ClickListener{ void walkClicked(int pos);}
}