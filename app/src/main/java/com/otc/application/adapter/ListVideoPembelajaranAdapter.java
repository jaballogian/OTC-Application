package com.otc.application.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.otc.application.R;
import com.otc.application.item.ItemProgram;
import com.otc.application.item.ItemVideoPembelajaran;

import java.util.ArrayList;

public class ListVideoPembelajaranAdapter extends RecyclerView.Adapter<ListVideoPembelajaranAdapter.ListViewHolder> {

    private ArrayList<ItemVideoPembelajaran> listVideoPembelajaran;
    public ListVideoPembelajaranAdapter(ArrayList<ItemVideoPembelajaran> list){
        this.listVideoPembelajaran = list;
    }

    @NonNull
    @Override
    public ListVideoPembelajaranAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_pembelajaran, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListVideoPembelajaranAdapter.ListViewHolder holder, int position) {
        ItemVideoPembelajaran itemProgram = listVideoPembelajaran.get(position);
        holder.subBabTextView.setText(itemProgram.getSubBabVideo());
    }

    @Override
    public int getItemCount() {
        return listVideoPembelajaran.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView subBabTextView;

        ListViewHolder(View itemView) {
            super(itemView);
            subBabTextView = itemView.findViewById(R.id.namaCoursetTextView);
        }
    }
}
