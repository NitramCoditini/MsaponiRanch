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

public class RecyclerAdapter4 extends RecyclerView.Adapter<RecyclerAdapter4.viewHolder> {

    private static final String Tag = "RecyclerView";
    private Context mcontext;
    private ArrayList<Model4> modelArrayList;




    public RecyclerAdapter4(Context mcontext, ArrayList<Model4> modelArrayList) {
        this.mcontext = mcontext;
        this.modelArrayList = modelArrayList;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feedmadedisplay, parent, false);



        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.textView2.setText(modelArrayList.get(position).getCowName());
        holder.textView3.setText(modelArrayList.get(position).getFeedNameOf());
        // Format quantity with a maximum of 2 decimal places and append "kg"
        String quantityText = String.format("%.2f kg", modelArrayList.get(position).getQuantity());
        holder.textView4.setText(quantityText);

        // Convert current time from milliseconds to a human-readable format (e.g., "4:35 PM, Jul 15, 2024")
        String currentTimeText = DateFormat.getDateTimeInstance().format(new Date(modelArrayList.get(position).getCurrentTime()));
        holder.textView5.setText(currentTimeText);





    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView textView2,textView3,textView4,textView5;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            textView2 = itemView.findViewById(R.id.cowFedName);
            textView3 = itemView.findViewById(R.id.feedFedName);
            textView4 = itemView.findViewById(R.id.feedFedWeight);
            textView5 = itemView.findViewById(R.id.feedFedDate);

        }
    }
}