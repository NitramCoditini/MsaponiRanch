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


}