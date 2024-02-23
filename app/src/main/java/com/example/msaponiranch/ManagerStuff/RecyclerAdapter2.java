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

public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.viewHolder> {

    private static final String Tag = "RecyclerView";
    private Context mcontext;
    private ArrayList<Model2> modelArrayList;




    public RecyclerAdapter2(Context mcontext, ArrayList<Model2> modelArrayList) {
        this.mcontext = mcontext;
        this.modelArrayList = modelArrayList;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.purchasehistorydisplay, parent, false);



        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.textView2.setText(modelArrayList.get(position).getTotalPriceStr());
        holder.textView3.setText(modelArrayList.get(position).getFeedName());
        holder.textView4.setText(modelArrayList.get(position).getFeedWeight());
        holder.textView5.setText(modelArrayList.get(position).getDate());





    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView textView2,textView3,textView4,textView5;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            textView2 = itemView.findViewById(R.id.itemPurPrice);
            textView3 = itemView.findViewById(R.id.itemPurName);
            textView4 = itemView.findViewById(R.id.itempurWeight);
            textView5 = itemView.findViewById(R.id.itemPurDate);

        }
    }
}