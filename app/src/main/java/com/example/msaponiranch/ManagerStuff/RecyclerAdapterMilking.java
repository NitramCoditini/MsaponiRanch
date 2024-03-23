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
import com.example.msaponiranch.livestockactivityclassess.MilkingDetail;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerAdapterMilking extends RecyclerView.Adapter<RecyclerAdapterMilking.viewHolder> {

    private static final String Tag = "RecyclerView";
    private Context mcontext;
    private ArrayList<MilkingDetail> modelArrayList;




    public RecyclerAdapterMilking(Context mcontext, ArrayList<MilkingDetail> modelArrayList) {
        this.mcontext = mcontext;
        this.modelArrayList = modelArrayList;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.milkingdisplay, parent, false);



        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.textView2.setText(modelArrayList.get(position).getCowMiName());
        holder.textView3.setText(modelArrayList.get(position).getCowMiBreed());
        holder.textView6.setText(modelArrayList.get(position).getRanchHandName());
        holder.textView4.setText(modelArrayList.get(position).getMilkSize());

        // Convert current time from milliseconds to a human-readable format (e.g., "4:35 PM, Jul 15, 2024")
        String currentTimeText = DateFormat.getDateTimeInstance().format(new Date(modelArrayList.get(position).getCurrentTime()));
        holder.textView5.setText(currentTimeText);





    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView textView2,textView3,textView4,textView5, textView6;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            textView2 = itemView.findViewById(R.id.cowMilkName);
            textView3 = itemView.findViewById(R.id.cowMilkBreed);
            textView4 = itemView.findViewById(R.id.milkSize);
            textView5 = itemView.findViewById(R.id.milkDate);
            textView6 = itemView.findViewById(R.id.ranchHandMilkName);

        }
    }
}