package com.example.msaponiranch.livestockactivityclassess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.msaponiranch.Model;
import com.example.msaponiranch.R;
import com.example.msaponiranch.RecyclerAdapter;
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

public class AnimalList extends AppCompatActivity {

    RecyclerView.Adapter recyclerAdapter;

    DatabaseReference databaseReference;

    ArrayList<Model> modelArrayList;

    RecyclerView recyclerViewCategoryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_list);

        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCategoryList = findViewById(R.id.cattleRecyclerView);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference();


        // Initialize ArrayLists separately for each data source
        modelArrayList = new ArrayList<>();
        getDataFromFirebase();


        // Clear data for each RecyclerView separately
        clearAll();
    }
    private void getDataFromFirebase() {

        Log.d("May", "Fetching data from Firebase");
        Query query = databaseReference.child("CattleDetails");
        Log.d("May", "Query created");
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                modelArrayList.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Log.d("May", "Listener added");

                    Model model = new Model();

                    model.setImageId(snapshot.child("imageId").getValue().toString());
                    model.setCowname(snapshot.child("cowname").getValue().toString());
                    model.setCowbreed(snapshot.child("cowbreed").getValue().toString());
                    model.setCowweight(snapshot.child("cowweight").getValue().toString());
                    model.setAgeWithUnit(snapshot.child("ageWithUnit").getValue().toString());
                    model.setGender(snapshot.child("gender").getValue().toString());


                    modelArrayList.add(model);
                    taskProgress();



                }
                Log.d("LIST_SIZE", "List size: " + modelArrayList.size());
                // After processing all the data, set the adapters to the RecyclerViews
                recyclerAdapter = new RecyclerAdapter(getApplicationContext(), modelArrayList);
                recyclerViewCategoryList.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors if needed
            }
        });
    }
    private  void clearAll(){
        if(modelArrayList !=null){
            modelArrayList.clear();

            if (recyclerAdapter !=null){
                recyclerAdapter.notifyDataSetChanged();
            }
        }
        modelArrayList = new ArrayList<>();
    }
    private  void taskProgress(){
        String titleYours = "Milking";
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
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
                            if(currentProgress == 25) {
                                int newProgress = currentProgress + 45; // Assuming you want to increment by 10
                                // Update the progress value only if it's not already at the desired value
                                if (newProgress == 70) { // Assuming the maximum progress value is 100
                                    snapshot.getRef().child("progressText").setValue(newProgress)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Task updated successfully
                                                    Toast.makeText(AnimalList.this, "Task progress updated successfully", Toast.LENGTH_SHORT).show();
                                                    // Navigate to AnimalFeeding activity

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to update task
                                                    Toast.makeText(AnimalList.this, "Failed to update task progress", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(AnimalList.this, "No signed in user", Toast.LENGTH_SHORT).show();
        }


    }


}