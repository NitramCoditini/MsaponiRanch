package com.example.msaponiranch.livestockactivityclassess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msaponiranch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MilkingRecord extends AppCompatActivity {

    TextView tvN,tvB;
    EditText edW;
    Button mButton;

    FirebaseFirestore fStore;

    FirebaseUser currentUser;

    FirebaseAuth mAuth;
    String uid,ranchHandName;

    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milking_record);


        tvN = findViewById(R.id.cowMilkName);
        tvB = findViewById(R.id.cowMilkBreed);
        edW = findViewById(R.id.milkWeight);
        mButton = findViewById(R.id.milkReg);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        uid = currentUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Retrieve the position from the Intent
        Intent intent = getIntent();

        // Retrieve the cow's name and breed from the Intent
        String cowMName = intent.getStringExtra("cow_name_key");
        String cowMBreed = intent.getStringExtra("cow_breed_key");

        tvN.setText(cowMName);
        tvB.setText(cowMBreed);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cowname = tvN.getText().toString().trim();
                String cowbreed = tvB.getText().toString().trim();
                String cowweight = edW.getText().toString().trim();

                if (TextUtils.isEmpty(cowname)) {
                    tvN.setError("Cow name is required!");
                    return;
                }


                if (TextUtils.isEmpty(cowbreed)) {
                    tvB.setError("Cow breed is required!");
                    return;
                }
                if (TextUtils.isEmpty(cowweight)) {
                    edW.setError("Milk size is required!");
                    return;
                }
                DocumentReference userRef = fStore.collection("Msaponi ranch workers").document(uid);
                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                ranchHandName = document.getString("Full name");




                                uploadToFirebase();


                            } else {
                                Toast.makeText(MilkingRecord.this, "No such document", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(MilkingRecord.this, "Error getting documents: ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });




            }
        });
    }
    private void uploadToFirebase() {
        String cowMiName  = tvN.getText().toString();
        String cowMiBreed = tvB.getText().toString();
        String milkSize = edW.getText().toString() + "L";
        long currentTime = System.currentTimeMillis();

        DatabaseReference milkingMadeRef = databaseReference.child("Milking Record");

        // Generate a unique key for the feed entry using push()

        MilkingDetail milkingDetail = new MilkingDetail(ranchHandName,cowMiName,cowMiBreed,milkSize,currentTime);
        String milkEntryKey = milkingMadeRef.push().getKey();

        milkingMadeRef.child(milkEntryKey).setValue(milkingDetail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(MilkingRecord.this, cowMiName + " milk record saved", Toast.LENGTH_SHORT).show();

                        // Clear displayed information after successful save
                        tvN.setText("");
                        tvB.setText("");
                        edW.setText("");
                        taskProgress();




                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MilkingRecord.this, "Failed to add cow milking record, please try again.", Toast.LENGTH_SHORT).show();
                    }
                });


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
                            if(currentProgress == 70) {
                                int newProgress = currentProgress + 30; // Assuming you want to increment by 10
                                // Update the progress value only if it's not already at the desired value
                                if (newProgress == 100) { // Assuming the maximum progress value is 100
                                    snapshot.getRef().child("progressText").setValue(newProgress)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Task updated successfully
                                                    Toast.makeText(MilkingRecord.this, "Task progress updated successfully", Toast.LENGTH_SHORT).show();
                                                    // Navigate to AnimalFeeding activity

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to update task
                                                    Toast.makeText(MilkingRecord.this, "Failed to update task progress", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(MilkingRecord.this, "No signed in user", Toast.LENGTH_SHORT).show();
        }


    }
}