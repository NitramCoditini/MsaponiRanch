package com.example.msaponiranch.ManagerStuff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.msaponiranch.R;
import com.example.msaponiranch.RanchActivity;
import com.example.msaponiranch.livestockactivityclassess.MilkingDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CattleCondition extends AppCompatActivity {

    RecyclerView.Adapter recyclerAdapter,recyclerAdapter1,recyclerAdapterProducts;
    RecyclerView recyclerViewCategoryList3,recyclerViewCategoryList4,recyclerViewCategoryListProducts;

    ArrayList<ModelCondition> modelArrayList;
    ArrayList<ModelCalf> modelArrayList1;

    ArrayList<MilkingDetail> modelArrayListProducts;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cattle_condition);

        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCategoryList3 = findViewById(R.id.recyclerViewCondition);
        recyclerViewCategoryList3.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager1= new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCategoryList4 = findViewById(R.id.recyclerViewCalf);
        recyclerViewCategoryList4.setLayoutManager(linearLayoutManager1);

        LinearLayoutManager linearLayoutManager2= new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCategoryListProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewCategoryListProducts.setLayoutManager(linearLayoutManager2);

        databaseReference = firebaseDatabase.getInstance().getReference();

        modelArrayList = new ArrayList<>();
        modelArrayList1 = new ArrayList<>();
        modelArrayListProducts = new ArrayList<>();


        getDataFromFirebase();
        getDataFromFirebase1();
        getDataFromFirebase2();




    }
    private void getDataFromFirebase() {

        Log.d("May", "Fetching data from Firebase");
        Query query = databaseReference.child("Cattle Current Condition");
        Log.d("May", "Query created");
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                modelArrayList.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Log.d("May", "Listener added");

                    ModelCondition model1 = new ModelCondition();

                    model1.setCowname1(snapshot.child("cowname1").getValue(String.class));
                    model1.setRanchHandName(snapshot.child("ranchHandName").getValue(String.class));
                    model1.setCowappetite(snapshot.child("cowappetite").getValue(String.class));
                    model1.setCowtemperature(snapshot.child("cowtemperature").getValue(String.class));
                    model1.setCowappearance(snapshot.child("cowappearance").getValue(String.class));


                    modelArrayList.add(model1);


                }
                Log.d("LIST_SIZE", "List size: " + modelArrayList.size());
                // After processing all the data, set the adapters to the RecyclerViews
                recyclerAdapter = new RecyclerAdapterCondition(getApplicationContext(), modelArrayList);
                recyclerViewCategoryList3.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors if needed
            }
        });
    }

    private void getDataFromFirebase1() {

        Log.d("May", "Fetching data from Firebase");
        Query query = databaseReference.child("Calf Details");
        Log.d("May", "Query created");
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                modelArrayList1.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Log.d("May", "Listener added");

                    ModelCalf model = new ModelCalf();

                    model.setImageId(snapshot.child("imageId").getValue().toString());
                    model.setRanchHandName(snapshot.child("ranchHandName").getValue().toString());
                    model.setCalfName(snapshot.child("calfName").getValue().toString());
                    model.setCalfBreed(snapshot.child("calfBreed").getValue().toString());
                    model.setCalfWeight(snapshot.child("calfWeight").getValue().toString());
                    model.setCalfAge(snapshot.child("calfAge").getValue().toString());
                    model.setCalfGender(snapshot.child("calfGender").getValue().toString());
                    model.setFemaleParent(snapshot.child("femaleParent").getValue().toString());
                    model.setMaleParent(snapshot.child("maleParent").getValue().toString());



                    modelArrayList1.add(model);


                }
                // After processing all the data, set the adapters to the RecyclerViews
                recyclerAdapter1 = new RecyclerAdapterCalf(getApplicationContext(), modelArrayList1);
                recyclerViewCategoryList4.setAdapter(recyclerAdapter1);
                recyclerAdapter1.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors if needed
            }
        });
    }
    private void getDataFromFirebase2() {

        Log.d("May", "Fetching data from Firebase");
        Query query = databaseReference.child("Milking Record");
        Log.d("May", "Query created");
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                modelArrayListProducts.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Log.d("May", "Listener added");

                    MilkingDetail model = new MilkingDetail();

                    model.setCowMiBreed(snapshot.child("cowMiBreed").getValue().toString());
                    model.setRanchHandName(snapshot.child("ranchHandName").getValue().toString());
                    model.setCowMiName(snapshot.child("cowMiName").getValue().toString());
                    model.setMilkSize(snapshot.child("milkSize").getValue().toString());
                    model.setCurrentTime(snapshot.child("currentTime").getValue(Long.class));



                    modelArrayListProducts.add(model);


                }
                // After processing all the data, set the adapters to the RecyclerViews
                recyclerAdapterProducts = new RecyclerAdapterMilking(getApplicationContext(), modelArrayListProducts);
                recyclerViewCategoryListProducts.setAdapter(recyclerAdapterProducts);
                recyclerAdapterProducts.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors if needed
            }
        });
    }

}