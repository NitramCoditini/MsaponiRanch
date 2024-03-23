package com.example.msaponiranch.ranchhandtasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.msaponiranch.ManagerStuff.TaskDetail;
import com.example.msaponiranch.Model;
import com.example.msaponiranch.R;
import com.example.msaponiranch.livestockactivityclassess.MilkingRecord;

import java.util.ArrayList;

public class RecyclerAdapterTasks extends RecyclerView.Adapter<RecyclerAdapterTasks.viewHolder> {

    private static final String Tag = "RecyclerView";
    private Context mcontext;
    private ArrayList<TaskDetail> modelArrayList;




    public RecyclerAdapterTasks(Context mcontext, ArrayList<TaskDetail> modelArrayList) {
        this.mcontext = mcontext;
        this.modelArrayList = modelArrayList;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.taskdisplay, parent, false);



        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.textView1.setText(modelArrayList.get(position).getCowName());
        holder.textView2.setText(modelArrayList.get(position).getTitle());
        holder.textView3.setText(modelArrayList.get(position).getDescription());
        holder.textView4.setText(modelArrayList.get(position).getStartTime());
        holder.textView5.setText(modelArrayList.get(position).getEndTime());



        holder.progressBarTask.getProgressDrawable().mutate();
        int progress = modelArrayList.get(position).getProgressText();
        Log.d("ProgressValue", "Progress for task at position " + position + ": " + progress);
        holder.progressBarTask.setProgress(progress);

        String progressionText = String.format("%d%%", progress);
        holder.textView6.setText(progressionText);





    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView textView1,textView2,textView3,textView4,textView5,textView6;
        ProgressBar progressBarTask; // Add this line


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            textView1= itemView.findViewById(R.id.cowNameTask);
            textView2 = itemView.findViewById(R.id.titleTask);
            textView3 = itemView.findViewById(R.id.descriptionTask);
            textView4 = itemView.findViewById(R.id.startTimeTask);
            textView5 = itemView.findViewById(R.id.endTimeTask);
            textView6 = itemView.findViewById(R.id.progressTextTask);
            progressBarTask = itemView.findViewById(R.id.progressbarTask);

        }
    }
}