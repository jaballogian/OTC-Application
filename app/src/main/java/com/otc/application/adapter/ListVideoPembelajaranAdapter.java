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

    private ListVideoPembelajaranAdapter.OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(ListVideoPembelajaranAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }
    public interface OnItemClickCallback {
        void onItemClicked(ItemVideoPembelajaran itemVideoPembelajaran);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListVideoPembelajaranAdapter.ListViewHolder holder, int position) {
        ItemVideoPembelajaran itemProgram = listVideoPembelajaran.get(position);
        holder.subBabTextView.setText(itemProgram.getShowSubBabVideo());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listVideoPembelajaran.get(holder.getAdapterPosition()));
            }
        });
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
