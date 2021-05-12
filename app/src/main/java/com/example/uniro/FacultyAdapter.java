package com.example.uniro;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.ViewHolder>{

    Context context;

    ArrayList<Faculty> list;

    public static Faculty fac;

    public FacultyAdapter(Context context, ArrayList<Faculty> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new FacultyAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FacultyAdapter.ViewHolder holder, int position) {
        Faculty faculty = list.get(position);
        holder.name.setText(faculty.getName());

        if (list.get(position).getMainimg() != null) {
            String url = faculty.getMainimg();
            Glide.with(context)
                    .load(url)
                    .into(holder.imageView);
        } else {
            // make sure Glide doesn't load anything into this view until told otherwise
            Glide.with(context).clear(holder.imageView);
            // remove the placeholder (optional); read comments below
            holder.imageView.setImageDrawable(null);
        }

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,FacultyDetailsActivity.class);
                fac = list.get(position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView name;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            constraintLayout = itemView.findViewById(R.id.itemLayout);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }
}
