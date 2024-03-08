package com.example.msaponiranch.livestockactivityclassess;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msaponiranch.ManagerStuff.AdministrationProcess;
import com.example.msaponiranch.ManagerStuff.Model4;
import com.example.msaponiranch.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdministrationProcessAdapter extends RecyclerView.Adapter<AdministrationProcessAdapter.viewHolder> {

    private static final String Tag = "RecyclerView";
    private Context mcontext;
    private ArrayList<AdministrationProcess> processes;




    public AdministrationProcessAdapter(Context mcontext, ArrayList<AdministrationProcess> processes) {
        this.mcontext = mcontext;
        this.processes = processes;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_administration_process, parent, false);



        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {

        AdministrationProcess process = processes.get(position);
        holder.tvProcessName.setText(process.getProcessName());
        holder.tvDateAdministered.setText(process.getDateAdministered());
        holder.tvDosageInstruction.setText(process.getDosageInstruction());
        holder.tvDosageAmount.setText(process.getDosageAmount());






    }

    @Override
    public int getItemCount() {
        return   processes.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView tvProcessName, tvDateAdministered, tvDosageInstruction, tvDosageAmount;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvProcessName = itemView.findViewById(R.id.tvProcessName);
            tvDateAdministered = itemView.findViewById(R.id.tvDateAdministered);
            tvDosageInstruction = itemView.findViewById(R.id.tvDosageInstruction);
            tvDosageAmount = itemView.findViewById(R.id.tvDosageAmount);

        }
    }
}