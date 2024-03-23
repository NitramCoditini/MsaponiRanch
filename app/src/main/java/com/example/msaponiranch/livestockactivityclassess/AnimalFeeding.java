package com.example.msaponiranch.livestockactivityclassess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.msaponiranch.CattleDetail;
import com.example.msaponiranch.InventoryStuff.RanchHandInventoryAdding;
import com.example.msaponiranch.ManagerStuff.FeedRecDetail;
import com.example.msaponiranch.R;
import com.example.msaponiranch.RanchHandActivity;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AnimalFeeding extends AppCompatActivity {

    SearchView searchView;
    ImageView imageView;
    DatabaseReference databaseReference;

    RadioButton rb1;

    Button feeding;

    FirebaseFirestore fStore;

    FirebaseAuth mAuth;
    String uid;

    FirebaseUser currentUser;

    CardView cardView, recommendationCardView;
    TextView tvN,tvW,tvA,tvB,tvG,feed1, instru, feed2, feed3,feed4,feed5,nm;
    LinearLayout line1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_feeding);

        searchView = findViewById(R.id.cow_search_view);
        cardView = findViewById(R.id.catlleDetailCard);
        imageView = findViewById(R.id.feedCattlePoster);
        tvN = findViewById(R.id.feedCowName);
        tvB = findViewById(R.id.feedCowBreed);
        tvG = findViewById(R.id.feedCowGender);
        tvW = findViewById(R.id.feedCowWeight);
        tvA = findViewById(R.id.feedCowAge);
        feeding = findViewById(R.id.feedRec);
        feed1 = findViewById(R.id.feed1Details);
        feed2 = findViewById(R.id.feed2Details);
        feed3 = findViewById(R.id.feed3Details);
        feed4 = findViewById(R.id.feed4Details);
        feed5 = findViewById(R.id.feed5Details);
        nm = findViewById(R.id.nameWorker);



        recommendationCardView = findViewById(R.id.cardFeed);
        rb1 = findViewById(R.id.invradioButton1);
        instru = findViewById(R.id.nameSubtract);
        line1 = findViewById(R.id.linearcard);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        fStore = FirebaseFirestore.getInstance();
        uid = currentUser.getUid();



        databaseReference = FirebaseDatabase.getInstance().getReference();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // User submitted the search query
                String searchQuery = query.trim();

                // Ensure search query is not empty
                if (searchQuery.isEmpty()) {
                    // Show a custom message or toast indicating an empty search
                    Toast.makeText(AnimalFeeding.this, "Please enter a search query", Toast.LENGTH_SHORT).show();

                    return true;
                }
                retrivedString(new OnDescriptionProcessedListener() {
                    @Override
                    public void onDescriptionProcessed(String processedDescription) {

                        if (searchQuery.equals(processedDescription)) {
                            // The search query matches the processed description, perform your action here
                            // For example, navigate to another activity or update the UI
                            taskProgress();

                        }
                    }
                });
                Query databaseQuery = databaseReference.child("CattleDetails").orderByChild("cowname").equalTo(searchQuery);


                // Option 3: Search for cow IDs matching the query exactly


                searchCowDetails(databaseQuery);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return true;
            }
        });

        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent i = new Intent(AnimalFeeding.this, RanchHandInventoryAdding.class);
                    startActivity(i);


            }
        });



        feeding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference userRef = fStore.collection("Msaponi ranch workers").document(uid);
                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String RanchHandnm = document.getString("Full name");
                                nm.setText(RanchHandnm);


                                performFeedLogic();
                                taskProgress1();


                            } else {
                                Toast.makeText(AnimalFeeding.this, "No such document", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(AnimalFeeding.this, "Error getting documents: ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });
    }

    private void searchCowDetails(Query query) {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Process retrieved cow details here
                    DataSnapshot childSnapshot = snapshot.getChildren().iterator().next();
                    // Extract cow details from each child node
                    String cowImage = childSnapshot.child("imageId").getValue(String.class);
                    String cowName = childSnapshot.child("cowname").getValue(String.class);
                    String breed = childSnapshot.child("cowbreed").getValue(String.class);
                    String weight = childSnapshot.child("cowweight").getValue(String.class);
                    String age = childSnapshot.child("ageWithUnit").getValue(String.class);
                    String gender = childSnapshot.child("gender").getValue(String.class);
                    // ... access other cow details

                    // Update UI to display retrieved details for each cow
                    String weightValueString = weight.replaceAll("[^0-9]", "");
                    int cowWeight = 0;
                    try {
                        cowWeight = Integer.parseInt(weightValueString);
                        tvW.setText(String.valueOf(cowWeight)); // Update UI with integer weight
                    } catch (NumberFormatException e) {
                        // Handle the exception gracefully
                        // e.g., display an error message or set a default weight value
                        Log.e("CattleFeeding", "Error parsing weight: " + e.getMessage());
                    }
                    tvN.setText(cowName);
                    tvB.setText(breed);
                    tvA.setText(age);
                    tvG.setText(gender);
                    Glide.with(AnimalFeeding.this)
                            .load(cowImage) // Use the extracted cowImage variable
                            .into(imageView);




                } else {
                    Toast.makeText(AnimalFeeding.this, "Ensure you have written the correct name tag", Toast.LENGTH_SHORT).show();
                    // Handle case where no cow found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }
    private void performFeedLogic() {
        if (tvW.getText().toString().equals("Cow weight")) {
            // Handle the case where no cow details are retrieved
            Toast.makeText(AnimalFeeding.this, "Please search for a cow first", Toast.LENGTH_SHORT).show();
            return;
        }
        // Extract retrieved details from TextViews (assuming they were updated correctly)
        String cowWeightString = tvW.getText().toString();
        int cowWeight = Integer.parseInt(cowWeightString);
        String cowBreed = tvB.getText().toString();
        String ageString = tvA.getText().toString(); // Assuming age is retrieved from TextView

        // Identify unit and extract numerical value
        int ageValue;
        String unit;
        if (ageString.contains("years")) {
            unit = "years";
            ageValue = Integer.parseInt(ageString.replaceAll("[^0-9]", ""));
            // Convert years to months
            ageValue *= 12;
        } else if (ageString.contains("months")) {
            unit = "months";
            ageValue = Integer.parseInt(ageString.replaceAll("[^0-9]", ""));
        } else {
            // Handle cases where the unit is not recognized or missing (optional: display an error message)
            Log.e("CattleFeeding", "Invalid age format: " + ageString);
            return; // Or handle the invalid format appropriately
        }

        // Store the converted age in months
        int convertedAgeInMonths = ageValue;
        String cowGender = tvG.getText().toString();


        DatabaseReference feedDetailsRef = databaseReference.child("Inventory Details");

        // Add a ValueEventListener to retrieve the data
        feedDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Iterate through each child node (representing individual feed entries)
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String feedName = childSnapshot.child("feedName").getValue(String.class);
                        String feedWeightString = childSnapshot.child("feedWeight").getValue(String.class);
                        Log.d("CattleFeeding", "\t- Name: " + feedName + ", Weight: " + feedWeightString);
                        // Extract weight value (handle potential null values)
                        String feedValueString = feedWeightString.replaceAll("[^0-9.]", ""); // Allow both digits and decimal points
                        double feedWeight = 0;
                        try {
                            feedWeight = Double.parseDouble(feedValueString);

                        } catch (NumberFormatException e) {
                            // Handle the exception gracefully
                            // e.g., display an error message or set a default weight value
                            Log.e("CattleFeeding", "Error parsing feed weight: " + e.getMessage());
                        }
                        Map<String, Double> recommendedFeed = new HashMap<>();

                        if (cowWeight >= 1000 && cowBreed.equals("Friesian") && convertedAgeInMonths >= 24 && cowGender.equals("Female")) {

                            if (feedName.equals("Maize germ") || feedName.equals("Wheat pollard")|| feedName.equals("Napier grass")|| feedName.equals("Wheat straw")|| feedName.equals("Maclik super")) {
                                recommendedFeed.put("Maize germ", 2.0);
                                recommendedFeed.put("Wheat pollard", 3.0);
                                recommendedFeed.put("Napier grass", 6.0);
                                recommendedFeed.put("Wheat straw", 4.0);
                                recommendedFeed.put("Maclik super", 0.02);

                                // Initialize both TextViews to an empty string or a placeholder
                                feed1.setText("");
                                feed2.setText("");
                                feed3.setText("");
                                feed4.setText("");
                                feed5.setText("");

                                // Update each TextView with the recommended feed quantity
                                for (Map.Entry<String, Double> entry : recommendedFeed.entrySet()) {
                                    String feedName1 = entry.getKey();
                                    double recommendedQuantity = entry.getValue();

                                    if ("Maize germ".equals(feedName1)) {
                                        feed1.setText(feedName1 + ": " + recommendedQuantity + " kg");
                                    } else if ("Wheat pollard".equals(feedName1)) {
                                        feed2.setText(feedName1 + ": " + recommendedQuantity + " kg");
                                    }
                                    else if ("Napier grass".equals(feedName1)) {
                                        feed3.setText(feedName1 + ": " + recommendedQuantity + " kg");
                                    }
                                    else if ("Wheat straw".equals(feedName1)) {
                                        feed4.setText(feedName1 + ": " + recommendedQuantity + " kg");
                                    }
                                    else if ("Maclik super".equals(feedName1)) {
                                        feed5.setText(feedName1 + ": " + recommendedQuantity + " kg");
                                    }

                                }



                                instru.setVisibility(View.VISIBLE);



                            }else{

                            }


                        }else if(cowGender.equals("Female")) {

                            if (feedName.equals("Maize germ") || feedName.equals("Wheat pollard") || feedName.equals("Napier grass") || feedName.equals("Wheat straw") || feedName.equals("Maclik super")) {
                                recommendedFeed.put("Maize germ", 2.5);
                                recommendedFeed.put("Wheat pollard", 3.5);
                                recommendedFeed.put("Napier grass", 7.0);
                                recommendedFeed.put("Wheat straw", 4.5);
                                recommendedFeed.put("Maclik super", 0.03);

                                // Initialize both TextViews to an empty string or a placeholder
                                feed1.setText("");
                                feed2.setText("");
                                feed3.setText("");
                                feed4.setText("");
                                feed5.setText("");

                                // Update each TextView with the recommended feed quantity
                                for (Map.Entry<String, Double> entry : recommendedFeed.entrySet()) {
                                    String feedName1 = entry.getKey();
                                    double recommendedQuantity = entry.getValue();

                                    if ("Maize germ".equals(feedName1)) {
                                        feed1.setText(feedName1 + ": " + recommendedQuantity + " kg");
                                    } else if ("Wheat pollard".equals(feedName1)) {
                                        feed2.setText(feedName1 + ": " + recommendedQuantity + " kg");
                                    } else if ("Napier grass".equals(feedName1)) {
                                        feed3.setText(feedName1 + ": " + recommendedQuantity + " kg");
                                    } else if ("Wheat straw".equals(feedName1)) {
                                        feed4.setText(feedName1 + ": " + recommendedQuantity + " kg");
                                    } else if ("Maclik super".equals(feedName1)) {
                                        feed5.setText(feedName1 + ": " + recommendedQuantity + " kg");
                                    }

                                }


                            }
                        }else if(cowGender.equals("Male")) {

                            if (feedName.equals("Maize stover") || feedName.equals("Wheat bran") || feedName.equals("Lucerne") || feedName.equals("Cotton seedcake") || feedName.equals("Soya meal")) {
                                recommendedFeed.put("Maize stover", 1.0);
                                recommendedFeed.put("Wheat bran", 3.0);
                                recommendedFeed.put("Lucerne", 7.0);
                                recommendedFeed.put("Cotton seedcake", 4.0);
                                recommendedFeed.put("Soya meal", 2.5);

                                // Initialize both TextViews to an empty string or a placeholder
                                feed1.setText("");
                                feed2.setText("");
                                feed3.setText("");
                                feed4.setText("");
                                feed5.setText("");

                                // Update each TextView with the recommended feed quantity
                                for (Map.Entry<String, Double> entry : recommendedFeed.entrySet()) {
                                    String feedName1 = entry.getKey();
                                    double recommendedQuantity = entry.getValue();

                                    if ("Maize stover".equals(feedName1)) {
                                        feed1.setText(feedName1 + ": " + recommendedQuantity + " kg");
                                    } else if ("Wheat bran".equals(feedName1)) {
                                        feed2.setText(feedName1 + ": " + recommendedQuantity + " kg");
                                    } else if ("Lucerne".equals(feedName1)) {
                                        feed3.setText(feedName1 + ": " + recommendedQuantity + " kg");
                                    } else if ("Cotton seedcake".equals(feedName1)) {
                                        feed4.setText(feedName1 + ": " + recommendedQuantity + " kg");
                                    } else if ("Soya meal".equals(feedName1)) {
                                        feed5.setText(feedName1 + ": " + recommendedQuantity + " kg");
                                    }

                                }


                            }
                        }
                        recommendationCardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                instru.setVisibility(View.GONE);
                                String cowString = feed1.getText().toString();
                                if (TextUtils.isEmpty(cowString)) {
                                    Toast.makeText(AnimalFeeding.this, "Please Get Feed Recommendation first", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(AnimalFeeding.this);
                                builder.setMessage("Are you sure you want to select the feed details recommended ")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // Assuming the TextView displays "Maize germ: 2 kg\nWheat pollard: 3 kg"
                                                // Process the text from the first TextView

                                                processTextView(feed1.getText().toString());

                                                // Process the text from the second TextView
                                                processTextView(feed2.getText().toString());
                                                processTextView(feed3.getText().toString());
                                                processTextView(feed4.getText().toString());
                                                processTextView(feed5.getText().toString());
                                                Toast.makeText(AnimalFeeding.this, "Feed details saved successfully and sent to the manager", Toast.LENGTH_SHORT).show();


                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // User cancelled, do nothing
                                                instru.setVisibility(View.VISIBLE);
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();





                            }
                        });




                            // Store feed name and weight (in a Map or list)
                        // ... (logic to store or use feedName and feedWeight)
                    }
                } else {
                    // Handle case where no feed details are found (optional: display a message)
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors during data retrieval (optional)
            }
        });

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        // Uncheck all radio buttons
        rb1.setChecked(false);
        // Enable all radio buttons
        rb1.setEnabled(true);
    }
    private void processTextView(String text) {
        String cowName = tvN.getText().toString(); // Extract cow name
        String[] parts = text.split("\n"); // Split by newline characters
        long currentTime = System.currentTimeMillis();



        String feedNameOf = null;
        double quantity = 0;
        for (String part : parts) {
            if (part.contains(":")) {
                String[] feedParts = part.split(":");
                feedNameOf = feedParts[0].trim();
                String quantityString = feedParts[1].trim();

                // Extract actual quantity from the string (assuming format "X kg")
                quantity = Double.parseDouble(quantityString.split(" ")[0]);

                // Log the selected feed name for verification (optional)
                Log.d("SelectedFeed", "Selected feed: " + feedNameOf + ", Quantity: " + quantity + " kg");

                // Update the quantity in the Firebase Realtime Database
                // User confirmed, proceed with updating the feed quantity

                updateFeedQuantity(feedNameOf, quantity);
                addFeedInformationToDatabase(feedNameOf, quantity, cowName, currentTime);

            }
        }



    }

    private void updateFeedQuantity(String feedNameOf, double quantityUsed) {
        DatabaseReference inventoryDetailsRef = databaseReference.child("Inventory Details");

        inventoryDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot feedSnapshot : snapshot.getChildren()) {
                    String currentFeedName = feedSnapshot.child("feedName").getValue(String.class);
                    if (currentFeedName != null && currentFeedName.equals(feedNameOf)) {
                        String currentWeightString = feedSnapshot.child("feedWeight").getValue(String.class);
                        if (currentWeightString != null) {
                            // Check if the current weight is not 0kg and quantityUsed is greater than 0
                            if (!currentWeightString.equals("0kg") && quantityUsed > 0) {
                                float currentWeight = Float.parseFloat(currentWeightString.replaceAll("[^0-9.]", "")); // Remove non-numeric characters
                                float updatedWeight = currentWeight - (float) quantityUsed;
                                if (updatedWeight == 0) {
                                    // Show a Toast message indicating the feed has 0kg
                                    Toast.makeText(getApplicationContext(), feedNameOf + " is now 0kg. Please contact accountant to add feed", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Check if the updated weight is 0kg
                                    String updatedWeightString = String.format("%.2fkg", updatedWeight);

                                    // Update the value in the database
                                    feedSnapshot.getRef().child("feedWeight").setValue(updatedWeightString);
                                    // Initialize both TextViews to an empty string or a placeholder

                                    taskProgress2();

                                }
                            } else {
                                // Handle the case where the feed weight is 0kg or quantityUsed is 0
                                Toast.makeText(getApplicationContext(), "Cannot update feed quantity for " + feedNameOf + ". Feed weight is 0kg or quantity used is 0.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FeedUpdate", "Failed to update feed quantity: " + error.getMessage());
            }
        });
    }

    public interface OnDescriptionProcessedListener {
        void onDescriptionProcessed(String processedDescription);
    }
    private void retrivedString(OnDescriptionProcessedListener listener) {
        String titleYours = "Feeding";
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("Assigned Tasks").orderByChild("workerUserId").equalTo(uid);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                        if (titleYours.equals(taskSnapshot.child("title").getValue(String.class))) {
                            String name = taskSnapshot.child("cowName").getValue(String.class);
                            if (name != null) {
                                String processedDescription = name;

                                listener.onDescriptionProcessed(processedDescription);
                            }
                        }
                    }
                } else {
                    Toast.makeText(AnimalFeeding.this, "No tasks found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
    private  void taskProgress(){
        String titleYours = "Feeding";
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
                            if(currentProgress == 10) {
                                int newProgress = currentProgress + 30; // Assuming you want to increment by 10
                                // Update the progress value only if it's not already at the desired value
                                if (newProgress == 40) { // Assuming the maximum progress value is 100
                                    snapshot.getRef().child("progressText").setValue(newProgress)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Task updated successfully
                                                    Toast.makeText(AnimalFeeding.this, "Task progress updated successfully", Toast.LENGTH_SHORT).show();
                                                    // Navigate to AnimalFeeding activity

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to update task
                                                    Toast.makeText(AnimalFeeding.this, "Failed to update task progress", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(AnimalFeeding.this, "No signed in user", Toast.LENGTH_SHORT).show();
        }


    }
    private  void taskProgress1(){
        String titleYours = "Feeding";
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
                            if(currentProgress == 40) {
                                int newProgress = currentProgress + 30; // Assuming you want to increment by 10
                                // Update the progress value only if it's not already at the desired value
                                if (newProgress == 70) { // Assuming the maximum progress value is 100
                                    snapshot.getRef().child("progressText").setValue(newProgress)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Task updated successfully
                                                    Toast.makeText(AnimalFeeding.this, "Task progress updated successfully", Toast.LENGTH_SHORT).show();
                                                    // Navigate to AnimalFeeding activity

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to update task
                                                    Toast.makeText(AnimalFeeding.this, "Failed to update task progress", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(AnimalFeeding.this, "No signed in user", Toast.LENGTH_SHORT).show();
        }


    }
    private  void taskProgress2(){
        String titleYours = "Feeding";
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
                                                    Toast.makeText(AnimalFeeding.this, "Task progress updated successfully", Toast.LENGTH_SHORT).show();
                                                    // Navigate to AnimalFeeding activity


                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to update task
                                                    Toast.makeText(AnimalFeeding.this, "Failed to update task progress", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(AnimalFeeding.this, "No signed in user", Toast.LENGTH_SHORT).show();
        }


    }
    private void addFeedInformationToDatabase(String feedNameOf, double quantity, String cowName, long currentTime) {
        String ranchHandName = nm.getText().toString();
        DatabaseReference feedMadeRef = databaseReference.child("feedMade");
        FeedRecDetail feedRecDetail = new FeedRecDetail(ranchHandName, cowName, feedNameOf, quantity, currentTime);
        String feedEntryKey = feedMadeRef.push().getKey();

        feedMadeRef.child(feedEntryKey).setValue(feedRecDetail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // Clear displayed information after successful save
                        feed1.setText("");
                        feed2.setText("");
                        feed3.setText("");
                        feed4.setText("");
                        feed5.setText("");
                        imageView.setImageURI(null);

                        tvN.setText("Cow Name");
                        tvW.setText("Cow weight");
                        tvA.setText("Cow Age");
                        tvG.setText("Cow gender");
                        tvB.setText("Cow breed");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FeedMade", "Error saving feed details: " + e.getMessage());
                        Toast.makeText(AnimalFeeding.this, "Failed to save feed details, please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }





}