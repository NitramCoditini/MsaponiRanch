package com.example.msaponiranch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msaponiranch.ManagerStuff.Model2;
import com.example.msaponiranch.ManagerStuff.RecyclerAdapter2;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AccountantTransactionActivity extends AppCompatActivity {

    RecyclerView.Adapter recyclerAdapter;

    RecyclerView recyclerViewCategoryList2;

    ArrayList<Model2> modelArrayList;

    Button update, tim;

    EditText addBud;

    TextView initial,remaining,disabledText;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountant_transaction);


        update = findViewById(R.id.addBudget);
        addBud = findViewById(R.id.budgetName);
        initial = findViewById(R.id.budgetTitle);
        disabledText = findViewById(R.id.errorMes);
        remaining = findViewById(R.id.remainingTitle);
        tim = findViewById(R.id.checkTimeDelay);

        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCategoryList2 = findViewById(R.id.purchaseRecyclerView);
        recyclerViewCategoryList2.setLayoutManager(linearLayoutManager);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference("Budget Details");
        databaseReference.child("amount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    float remainingBudget = snapshot.getValue(Float.class);
                    String formattedBudget = String.format("Ksh %.2f", remainingBudget);
                    remaining.setText(formattedBudget);  // Update TextView with retrieved value
                } else {
                    // Handle case where data doesn't exist
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database errors
            }
        });

        modelArrayList = new ArrayList<>();


        getDataFromFirebase();


        // Clear data for each RecyclerView separately
        clearAll();

        tim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDelay();
            }
        });



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enterBudget = addBud.getText().toString().trim();


                if (TextUtils.isEmpty(enterBudget)) {
                    addBud.setError("Please Enter the budget!");
                    return;
                }

                float budgetAmount = Float.parseFloat(enterBudget);
                if (budgetAmount <= 0) {
                    addBud.setError("Budget should be more than 0");
                    return;
                }

                initial.setText("Ksh " + enterBudget);


                uploadToFirebase(budgetAmount);



            }


        });

    }
    private void getDataFromFirebase() {

        Log.d("May", "Fetching data from Firebase");
        Query query = databaseReference.child("Purchase History");
        Log.d("May", "Query created");
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                modelArrayList.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Log.d("May", "Listener added");

                    Model2 model = new Model2();

                    model.setDate(snapshot.child("date").getValue().toString());
                    model.setFeedName(snapshot.child("feedName").getValue().toString());
                    model.setFeedWeight(snapshot.child("feedWeight").getValue().toString());
                    model.setTotalPriceStr(snapshot.child("totalPriceStr").getValue().toString());



                    modelArrayList.add(model);



                }
                Log.d("LIST_SIZE", "List size: " + modelArrayList.size());
                // After processing all the data, set the adapters to the RecyclerViews
                recyclerAdapter = new RecyclerAdapter2(getApplicationContext(), modelArrayList);
                recyclerViewCategoryList2.setAdapter(recyclerAdapter);
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

    private void timeDelay() {
        databaseReference.child("lastUpdated");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long lastUpdatedTimestamp = snapshot.getValue(Long.class);
                    // Proceed to calculate and handle button state
                    Date lastUpdatedDate = new Date(lastUpdatedTimestamp);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(lastUpdatedDate);
                    calendar.add(Calendar.DAY_OF_YEAR, 20);  // Add 20 days
                    Date endDate = calendar.getTime();

                    // Format the date for display
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = sdf.format(endDate);
                    disabledText.setText("End Date: " + formattedDate);


                } else {
                    // Handle case where data doesn't exist
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database errors
            }
        });

    }

    private void uploadToFirebase(float budgetAmount) {


        databaseReference.child("amount").setValue(budgetAmount).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                databaseReference.child("lastUpdated").setValue(ServerValue.TIMESTAMP);

                Toast.makeText(AccountantTransactionActivity.this, "Budget updated", Toast.LENGTH_SHORT).show();
                addBud.setText("");
                update.setEnabled(false);

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AccountantTransactionActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


}