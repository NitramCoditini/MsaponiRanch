package com.example.msaponiranch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.msaponiranch.livestockactivityclassess.AnimalList;
import com.example.msaponiranch.livestockactivityclassess.MilkingRecord;

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


        String gender = modelArrayList.get(position).getGender();

        //image view: Glide library
        Glide.with(mcontext)
                .load(modelArrayList.get(position).getImageId())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gender.equals("Female")) {

                    String cowMilkedName = modelArrayList.get(position).getCowname();
                    String cowMilkedBreed = modelArrayList.get(position).getCowbreed();

                    // Create an Intent to start the MilkingRecord activity
                    Intent intent = new Intent(view.getContext(), MilkingRecord.class);

                    // Attach the cow's name and breed to the Intent
                    intent.putExtra("cow_name_key", cowMilkedName);
                    intent.putExtra("cow_breed_key", cowMilkedBreed);

                    // Start the MilkingRecord activity
                    view.getContext().startActivity(intent);

                }else{
                    Toast.makeText(mcontext, "You cannot milk a bull", Toast.LENGTH_SHORT).show();
                }

            }
        });


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