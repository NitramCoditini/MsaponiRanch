package com.example.msaponiranch.ManagerStuff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.msaponiranch.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TaskAssigning extends AppCompatActivity {

    RecyclerView.Adapter recyclerAdapter;

    RecyclerView recyclerViewCategoryList3;
    ArrayList<Worker> workerList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_assigning);

        db = FirebaseFirestore.getInstance();

        // Set up recycler view adapter
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerViewCategoryList3 = findViewById(R.id.workersRecyclerView);
        recyclerViewCategoryList3.setLayoutManager(linearLayoutManager);


        workerList = new ArrayList<>();


        // Get worker details from Firestore
        fetchWorkerDetails();

    }
    private void fetchWorkerDetails() {
        db.collection("Msaponi ranch workers")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        if (querySnapshot != null) {
                            workerList.clear();
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                Worker worker = document.toObject(Worker.class);
                                String userId = document.getId(); // User ID is the document ID

                                String areaWork = document.getString("Area of work");

                                worker.setUserId(userId);
                                worker.setName(document.getString("Full name"));
                                worker.setEmail(document.getString("Email address"));
                                worker.setImageId(document.getString("Profile pic"));

                                if (areaWork.equals("Ranch Hand")){
                                    workerList.add(worker);
                                }


                            }
                            // Update recycler view adapter to reflect new data
                            // After processing all the data, set the adapters to the RecyclerViews
                            recyclerAdapter = new RecyclerAdapterWorker(getApplicationContext(), workerList);
                            recyclerViewCategoryList3.setAdapter(recyclerAdapter);
                            recyclerAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle data fetching error
                        Log.e("ManagerActivity", "Error fetching worker details", e);
                    }
                });

    }

}