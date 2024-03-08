package com.example.msaponiranch.livestockactivityclassess;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.msaponiranch.Model;
import com.example.msaponiranch.R;

import java.util.ArrayList;

public class RecyclerAdapterBreeding extends RecyclerView.Adapter<RecyclerAdapterBreeding.viewHolder> {

    private static final String Tag = "RecyclerView";
    private Context mcontext;
    private ArrayList<Model> modelArrayList;

    private OnCownameClickListener onCownameClickListener;




    public RecyclerAdapterBreeding(Context mcontext, ArrayList<Model> modelArrayList, OnCownameClickListener listener) {
        this.mcontext = mcontext;
        this.modelArrayList = modelArrayList;
        this.onCownameClickListener = listener;
    }

    public interface OnCownameClickListener {
        void onCownameClick(String cowname, String pregnant);


    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cattlebreedingdisplay, parent, false);



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


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender = modelArrayList.get(position).getGender();
                if(gender.equals("Female")){
                    //alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Cow status")
                            .setMessage("Choose an option:")
                            .setPositiveButton("Pregnant", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Watch option clicked
                                    // best way to pass the string and also use the strings for ifs
                                    String pregnant = "pregnant";
                                    String cowname = modelArrayList.get(position).getCowname();
                                    onCownameClickListener.onCownameClick(cowname, pregnant);




                                }
                            })
                            .setNeutralButton("Just Delivered", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    String pregnant = "Just Delivered";
                                    String cowname = modelArrayList.get(position).getCowname();
                                    onCownameClickListener.onCownameClick(cowname, pregnant);



                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Cancel option clicked
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();



                } else {
                    Toast.makeText(mcontext, "Only select the female cattle", Toast.LENGTH_SHORT).show();
                }


            }
        });







    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView2,textView3,textView4,textView5,textView6;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cattlePoster);
            textView2 = itemView.findViewById(R.id.CowName);
            textView3 = itemView.findViewById(R.id.CowBreed);
            textView4 = itemView.findViewById(R.id.CowGender);
            textView5 = itemView.findViewById(R.id.CowWeight);
            textView6 = itemView.findViewById(R.id.CowAge);

        }
    }

}