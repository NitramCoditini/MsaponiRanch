package com.example.msaponiranch.ManagerStuff;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msaponiranch.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerAdapterCondition extends RecyclerView.Adapter<RecyclerAdapterCondition.viewHolder> {

    private static final String Tag = "RecyclerView";
    private Context mcontext;
    private ArrayList<ModelCondition> modelArrayList;




    public RecyclerAdapterCondition(Context mcontext, ArrayList<ModelCondition> modelArrayList) {
        this.mcontext = mcontext;
        this.modelArrayList = modelArrayList;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.currentconditiondisplay, parent, false);



        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.textView2.setText(modelArrayList.get(position).getCowname1());
        holder.textView3.setText(modelArrayList.get(position).getCowappetite());
        holder.textView4.setText(modelArrayList.get(position).getCowtemperature());
        holder.textView5.setText(modelArrayList.get(position).getCowappearance());
        holder.textView6.setText(modelArrayList.get(position).getRanchHandName());


    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView textView2,textView3,textView4,textView5, textView6;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            textView2 = itemView.findViewById(R.id.cowConditionName);
            textView3 = itemView.findViewById(R.id.appeName);
            textView4 = itemView.findViewById(R.id.tempName);
            textView5 = itemView.findViewById(R.id.genName);
            textView6 = itemView.findViewById(R.id.ranchHandFedName);

        }
    }
}