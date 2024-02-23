package com.example.msaponiranch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.msaponiranch.livestockactivityclassess.AnimalList;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.viewHolder> {

    private static final String Tag = "RecyclerView";
    private Context mcontext;
    private ArrayList<Model> modelArrayList;




    public RecyclerAdapter(Context mcontext, ArrayList<Model> modelArrayList) {
        this.mcontext = mcontext;
        this.modelArrayList = modelArrayList;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cattleinfodisplay, parent, false);



        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.textView2.setText(modelArrayList.get(position).getCowname());
        holder.textView3.setText(modelArrayList.get(position).getCowbreed());
        holder.textView4.setText(modelArrayList.get(position).getGender());
        holder.textView5.setText(modelArrayList.get(position).getCowweight());
        holder.textView6.setText(modelArrayList.get(position).getAgeWithUnit());




        //image view: Glide library
        Glide.with(mcontext)
                .load(modelArrayList.get(position).getImageId())
                .into(holder.imageView);


        holder.help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.textView5.setVisibility(View.VISIBLE);
                holder.textView6.setVisibility(View.VISIBLE);
                holder.help.setVisibility(View.GONE);

            }
        });




    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView imageView, help;
        TextView textView2,textView3,textView4,textView5,textView6;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            help = itemView.findViewById(R.id.helpIcon);
            imageView = itemView.findViewById(R.id.cattlePoster);
            textView2 = itemView.findViewById(R.id.CowName);
            textView3 = itemView.findViewById(R.id.CowBreed);
            textView4 = itemView.findViewById(R.id.CowGender);
            textView5 = itemView.findViewById(R.id.CowWeight);
            textView6 = itemView.findViewById(R.id.CowAge);

        }
    }
}