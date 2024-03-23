package com.example.msaponiranch.ManagerStuff;

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
import com.example.msaponiranch.Model;
import com.example.msaponiranch.R;
import com.example.msaponiranch.livestockactivityclassess.MilkingRecord;

import java.util.ArrayList;

public class RecyclerAdapterWorker extends RecyclerView.Adapter<RecyclerAdapterWorker.viewHolder> {

    private static final String Tag = "RecyclerView";
    private Context mcontext;
    private ArrayList<Worker> modelArrayList;




    public RecyclerAdapterWorker(Context mcontext, ArrayList<Worker> modelArrayList) {
        this.mcontext = mcontext;
        this.modelArrayList = modelArrayList;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workerdisplay, parent, false);



        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.textView2.setText(modelArrayList.get(position).getName());
        holder.textView3.setText(modelArrayList.get(position).getEmail());





        //image view: Glide library
        Glide.with(mcontext)
                .load(modelArrayList.get(position).getImageId())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String workerName = modelArrayList.get(position).getName();
                String UserId = modelArrayList.get(position).getUserId();


                Intent intent = new Intent(view.getContext(), AssignmentTask.class);


                intent.putExtra("worker_name_key", workerName);
                intent.putExtra("worker_userId_key", UserId);


                view.getContext().startActivity(intent);

            }
        });







    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView2,textView3;


        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.itemWor);
            textView2 = itemView.findViewById(R.id.workerDisName);
            textView3 = itemView.findViewById(R.id.workerDisEmail);


        }
    }
}