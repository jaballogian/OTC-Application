package com.otc.application.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.otc.application.R;
import com.otc.application.item.ItemProgram;

import java.util.ArrayList;

public class GridProgramAdapter extends RecyclerView.Adapter<GridProgramAdapter.GridViewHolder> {

    private ArrayList<ItemProgram> listProgram;
    public GridProgramAdapter(ArrayList<ItemProgram> list) {
        this.listProgram = list;
    }

    @NonNull
    @Override
    public GridProgramAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_program, parent, false);
        return new GridViewHolder(view);
    }

    private OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }
    public interface OnItemClickCallback {
        void onItemClicked(ItemProgram itemProgram);
    }

    @Override
    public void onBindViewHolder(@NonNull final GridProgramAdapter.GridViewHolder holder, int position) {
        ItemProgram itemProgram = listProgram.get(position);
        holder.namaProgramTextView.setText(itemProgram.getNamaProgram());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listProgram.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProgram.size();
    }

//    public class GridViewHolder extends RecyclerView.ViewHolder {
//        public GridViewHolder(@NonNull View itemView) {
//            super(itemView);
//        }
//    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        TextView namaProgramTextView;

        GridViewHolder(View itemView) {
            super(itemView);
            namaProgramTextView = itemView.findViewById(R.id.namaProgramTextView);
        }
    }
}
