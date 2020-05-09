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

    @Override
    public void onBindViewHolder(@NonNull GridProgramAdapter.GridViewHolder holder, int position) {
        ItemProgram itemProgram = listProgram.get(position);
        holder.namaProgramTextView.setText(itemProgram.getNamaProgram());
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
