package com.example.msaponiranch.ManagerStuff;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msaponiranch.AccountantStuff.SoldItemDetail;
import com.example.msaponiranch.R;
import com.example.msaponiranch.livestockactivityclassess.MilkingDetail;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerAdapterSales extends RecyclerView.Adapter<RecyclerAdapterSales.viewHolder> {

    private static final String Tag = "RecyclerView";
    private Context mcontext;
    private ArrayList<SoldItemDetail> modelArrayList;




    public RecyclerAdapterSales(Context mcontext, ArrayList<SoldItemDetail> modelArrayList) {
        this.mcontext = mcontext;
        this.modelArrayList = modelArrayList;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saleshistorydisplay, parent, false);



        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.textView2.setText(modelArrayList.get(position).getSdItemName());
        holder.textView3.setText(modelArrayList.get(position).getSdItemQuantity());
        holder.textView4.setText(modelArrayList.get(position).getSdItemDate());
        holder.textView6.setText(modelArrayList.get(position).getSdItemTotal());






    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView textView2,textView3,textView4, textView6;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            textView2 = itemView.findViewById(R.id.itemSaleName);
            textView3 = itemView.findViewById(R.id.itemSaleWeight);
            textView4 = itemView.findViewById(R.id.itemSaleDate);
            textView6 = itemView.findViewById(R.id.itemSalePrice);

        }
    }
}