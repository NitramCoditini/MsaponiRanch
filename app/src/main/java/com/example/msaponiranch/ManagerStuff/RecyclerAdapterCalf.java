package com.example.msaponiranch.ManagerStuff;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.msaponiranch.Model;
import com.example.msaponiranch.R;

import java.util.ArrayList;

public class RecyclerAdapterCalf extends RecyclerView.Adapter<RecyclerAdapterCalf.viewHolder> {

    private static final String Tag = "RecyclerView";
    private Context mcontext;
    private ArrayList<ModelCalf> modelArrayList;




    public RecyclerAdapterCalf(Context mcontext, ArrayList<ModelCalf> modelArrayList) {
        this.mcontext = mcontext;
        this.modelArrayList = modelArrayList;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calfinfodisplay, parent, false);



        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.textView1.setText(modelArrayList.get(position).getRanchHandName());
        holder.textView2.setText(modelArrayList.get(position).getCalfName());
        holder.textView3.setText(modelArrayList.get(position).getCalfBreed());
        holder.textView4.setText(modelArrayList.get(position).getCalfGender());
        holder.textView5.setText(modelArrayList.get(position).getCalfWeight());
        holder.textView6.setText(modelArrayList.get(position).getCalfAge());
        holder.textView7.setText(modelArrayList.get(position).getFemaleParent());
        holder.textView8.setText(modelArrayList.get(position).getMaleParent());




        //image view: Glide library
        Glide.with(mcontext)
                .load(modelArrayList.get(position).getImageId())
                .into(holder.imageView);







    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView1,textView2,textView3,textView4,textView5,textView6,textView7,textView8;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.ranchcalfName);
            imageView = itemView.findViewById(R.id.CalfPoster);
            textView2 = itemView.findViewById(R.id.CalfName);
            textView3 = itemView.findViewById(R.id.CalfBreed);
            textView4 = itemView.findViewById(R.id.CalfGender);
            textView5 = itemView.findViewById(R.id.CalfWeight);
            textView6 = itemView.findViewById(R.id.CalfAge);
            textView7 = itemView.findViewById(R.id.CalfFemaleParent);
            textView8 = itemView.findViewById(R.id.CalfMaleParent);

        }
    }
}