package com.example.msaponiranch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.msaponiranch.livestockactivityclassess.AnimalList;
import com.example.msaponiranch.livestockactivityclassess.MilkingRecord;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.viewHolder> {

    private static final String Tag = "RecyclerView";
    private Context mcontext;
    private ArrayList<Model> modelArrayList;

    DatabaseReference databaseReference;




    public RecyclerAdapter(Context mcontext, ArrayList<Model> modelArrayList) {
        this.mcontext = mcontext;
        this.modelArrayList = modelArrayList;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cattleinfodisplay, parent, false);



        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.textView2.setText(modelArrayList.get(position).getCowname());
        holder.textView3.setText(modelArrayList.get(position).getCowbreed());
        holder.textView4.setText(modelArrayList.get(position).getGender());
        holder.textView5.setText(modelArrayList.get(position).getCowweight());
        holder.textView6.setText(modelArrayList.get(position).getAgeWithUnit());


        String gender = modelArrayList.get(position).getGender();

        //image view: Glide library
        Glide.with(mcontext)
                .load(modelArrayList.get(position).getImageId())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gender.equals("Female")) {

                    String cowMilkedName = modelArrayList.get(position).getCowname();
                    String cowMilkedBreed = modelArrayList.get(position).getCowbreed();

                    // Create an Intent to start the MilkingRecord activity
                    Intent intent = new Intent(view.getContext(), MilkingRecord.class);

                    // Attach the cow's name and breed to the Intent
                    intent.putExtra("cow_name_key", cowMilkedName);
                    intent.putExtra("cow_breed_key", cowMilkedBreed);
                    taskProgress();

                    // Start the MilkingRecord activity
                    view.getContext().startActivity(intent);

                }else{
                    Toast.makeText(mcontext, "You cannot milk a bull", Toast.LENGTH_SHORT).show();
                }

            }
        });


        holder.help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.textView5.setVisibility(View.VISIBLE);
                holder.textView6.setVisibility(View.VISIBLE);
                holder.help.setVisibility(View.GONE);

            }
        });




    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView imageView, help;
        TextView textView2,textView3,textView4,textView5,textView6;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            help = itemView.findViewById(R.id.helpIcon);
            imageView = itemView.findViewById(R.id.cattlePoster);
            textView2 = itemView.findViewById(R.id.CowName);
            textView3 = itemView.findViewById(R.id.CowBreed);
            textView4 = itemView.findViewById(R.id.CowGender);
            textView5 = itemView.findViewById(R.id.CowWeight);
            textView6 = itemView.findViewById(R.id.CowAge);

        }
    }
    private  void taskProgress(){
        String titleYours = "Milking";
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            Query query = databaseReference.child("Assigned Tasks").orderByChild("workerUserId").equalTo(uid);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean taskFound = false;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Check if the task title matches "Feeding"
                        if (titleYours.equals(snapshot.child("title").getValue(String.class))) {
                            // Retrieve the current progress value
                            int currentProgress = snapshot.child("progressText").getValue(Integer.class);
                            // Increment the progress value
                            if(currentProgress == 10) {
                                int newProgress = currentProgress + 60; // Assuming you want to increment by 10
                                // Update the progress value only if it's not already at the desired value
                                if (newProgress == 70) { // Assuming the maximum progress value is 100
                                    snapshot.getRef().child("progressText").setValue(newProgress)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Task updated successfully
                                                    Toast.makeText(mcontext, "Task progress updated successfully", Toast.LENGTH_SHORT).show();
                                                    // Navigate to AnimalFeeding activity

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to update task
                                                    Toast.makeText(mcontext, "Failed to update task progress", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                }
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle errors if needed
                }
            });
        } else {
            Toast.makeText(mcontext, "No signed in user", Toast.LENGTH_SHORT).show();
        }


    }


}