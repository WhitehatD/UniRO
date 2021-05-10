package com.example.uniro;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class UniversitateView extends RecyclerView.ViewHolder {

    public TextView uni_name;
    public TextView uni_desc;

    public UniversitateView(@NotNull View item){
        super(item);

        uni_name = item.findViewById(R.id.uni_nume);
        uni_desc = item.findViewById(R.id.uni_desc);
    }
}
