package com.example.msaponiranch.InventoryStuff;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.msaponiranch.ManagerStuff.Model2;
import com.example.msaponiranch.R;

import java.util.ArrayList;

public class RecyclerAdapter3 extends RecyclerView.Adapter<RecyclerAdapter3.viewHolder> {

    private static final String Tag = "RecyclerView";
    private Context mcontext;
    private ArrayList<Model3> modelArrayList;




    public RecyclerAdapter3(Context mcontext, ArrayList<Model3> modelArrayList) {
        this.mcontext = mcontext;
        this.modelArrayList = modelArrayList;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inventorydisplay, parent, false);



        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.textView3.setText(modelArrayList.get(position).getFeedName());
        holder.textView4.setText(modelArrayList.get(position).getFeedWeight());
        holder.textView5.setText(modelArrayList.get(position).getDate());


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
        TextView textView3,textView4,textView5;


        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.itemInv);
            textView3 = itemView.findViewById(R.id.itemInvName);
            textView4 = itemView.findViewById(R.id.itemInvWeight);
            textView5 = itemView.findViewById(R.id.itemInvDate);

        }
    }
}