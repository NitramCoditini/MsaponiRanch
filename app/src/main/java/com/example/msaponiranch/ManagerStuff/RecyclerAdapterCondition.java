package com.example.msaponiranch.ManagerStuff;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msaponiranch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerAdapterCondition extends RecyclerView.Adapter<RecyclerAdapterCondition.viewHolder> {

    private static final String Tag = "RecyclerView";
    private Context mcontext;
    private ArrayList<ModelCondition> modelArrayList;




    public RecyclerAdapterCondition(Context mcontext, ArrayList<ModelCondition> modelArrayList) {
        this.mcontext = mcontext;
        this.modelArrayList = modelArrayList;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.currentconditiondisplay, parent, false);



        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.textView2.setText(modelArrayList.get(position).getCowname1());
        holder.textView3.setText(modelArrayList.get(position).getCowappetite());
        holder.textView4.setText(modelArrayList.get(position).getCowtemperature());
        holder.textView5.setText(modelArrayList.get(position).getCowappearance());
        holder.textView6.setText(modelArrayList.get(position).getRanchHandName());




            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get the position directly within the click listener
                    int clickedPosition = holder.getAdapterPosition();

                    // Ensure the position is valid (check for potential out-of-bounds)
                    if (clickedPosition != -1) {
                        String cowName = modelArrayList.get(clickedPosition).getCowname1();

                        // Create an AlertDialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Delete Item")
                                .setMessage("Has the cattle been treated?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User confirmed deletion, proceed with deletion logic
                                        deleteItemFromFirebase(cowName, clickedPosition);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Cancel option clicked
                                        dialog.dismiss();
                                    }
                                });
                        builder.create().show();
                    } else {
                        // Handle invalid position case (e.g., display a message)
                        Toast.makeText(view.getContext(), "Invalid item position", Toast.LENGTH_SHORT).show();
                    }
                }
            });



    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView textView2,textView3,textView4,textView5, textView6;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            textView2 = itemView.findViewById(R.id.cowConditionName);
            textView3 = itemView.findViewById(R.id.appeName);
            textView4 = itemView.findViewById(R.id.tempName);
            textView5 = itemView.findViewById(R.id.genName);
            textView6 = itemView.findViewById(R.id.ranchHandFedName);

        }
    }
    private void deleteItemFromFirebase(String cowName, int position) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Cattle Current Condition");
        Query query = databaseReference.orderByChild("cowname1").equalTo(cowName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Adjust position if multiple items are being deleted
                                int adjustedPosition = modelArrayList.indexOf(snapshot.getValue(ModelCondition.class));
                                if (adjustedPosition != -1) {
                                    modelArrayList.remove(adjustedPosition);
                                    notifyItemRemoved(adjustedPosition);
                                    notifyItemRangeChanged(adjustedPosition, modelArrayList.size() - position);
                                }
                            } else {
                                Toast.makeText(mcontext, "Failed to delete item", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mcontext, "Failed to delete item", Toast.LENGTH_SHORT).show();
            }
        });
    }

}