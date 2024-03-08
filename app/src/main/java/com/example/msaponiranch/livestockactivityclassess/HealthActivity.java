package com.example.msaponiranch.livestockactivityclassess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.msaponiranch.ManagerStuff.AdministrationProcess;
import android.Manifest;
import com.example.msaponiranch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HealthActivity extends AppCompatActivity {

    SearchView searchView;

    private static final int PHONE_CALL_REQUEST = 0;

    ArrayList<AdministrationProcess> processess;

    RecyclerView.Adapter recyclerAdapter;

    Calendar calendar;


    ImageView datePickerIcon;

    DatabaseReference databaseReference;

    EditText temp,ape,gen,dateEditText;

    CardView cardView;

    FirebaseFirestore fStore;

    FirebaseAuth mAuth;
    String uid;

    FirebaseUser currentUser;

    RecyclerView recyclerView;

    TextView tvN,tvH,messageView,tvS, messageView1,nmRanch,call;

    FloatingActionButton fabPhone;

    RadioButton Rb1,Rb2;

    Button recObservation,admDose;

    TextInputLayout txLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        searchView = findViewById(R.id.cow_search_view);
        cardView = findViewById(R.id.catlleHealthDetailCard);
        tvN = findViewById(R.id.admCowName);
        Rb1 = findViewById(R.id.healthyStatus);
        Rb2 = findViewById(R.id.sickStatus);
        tvH = findViewById(R.id.nameHealthy);
        recObservation = findViewById(R.id.healthRecording);
        temp = findViewById(R.id.cowTemp);
        ape = findViewById(R.id.cowAppe);
        gen = findViewById(R.id.cowGen);
        messageView = findViewById(R.id.messageText);
        messageView1 = findViewById(R.id.messageText1);
        dateEditText = findViewById(R.id.dateEditText);
        datePickerIcon = findViewById(R.id.datePickerIcon);
        txLayout = findViewById(R.id.InputTextDate);
        tvS = findViewById(R.id.nameSearching);
        admDose = findViewById(R.id.administerDose);
        nmRanch = findViewById(R.id.nameRancher);
        fabPhone = findViewById(R.id.fab_phone);
        call = findViewById(R.id.nameCall);

        calendar = Calendar.getInstance();
        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        fStore = FirebaseFirestore.getInstance();
        uid = currentUser.getUid();


        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView = findViewById(R.id.recyclerViewAdminProcesses);
        recyclerView.setLayoutManager(linearLayoutManager);




        databaseReference = FirebaseDatabase.getInstance().getReference();

        datePickerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        admDose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredDate = dateEditText.getText().toString();
                if (TextUtils.isEmpty(enteredDate)) {
                    dateEditText.setError("Administered date is required!");
                    return;
                }

                // Assuming cowId is the unique identifier for the cow
                String cowId = tvN.getText().toString(); // Assuming tvN holds the cow's ID

                // Assuming processName is the name of the process you're updating
                String processName = messageView1.getText().toString(); // Replace this with the actual process name

                // Reference to the Firebase Realtime Database
                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Health Record").child(cowId).child("administration").child(processName);

                // Create a map to hold the updated fields
                Map<String, Object> updates = new HashMap<>();
                updates.put("date administered", enteredDate);

                // Update the cow's details in the database
                databaseReference2.updateChildren(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Handle successful update
                                Toast.makeText(getApplicationContext(), "Dose administered and Date updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failed update
                                Toast.makeText(getApplicationContext(), "Failed to update date", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Initialize ArrayLists separately for each data source
        processess = new ArrayList<>();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // User submitted the search query
                String searchQuery = query.trim();

                // Ensure search query is not empty
                if (searchQuery.isEmpty()) {
                    // Show a custom message or toast indicating an empty search
                    Toast.makeText(HealthActivity.this, "Please enter a search query", Toast.LENGTH_SHORT).show();
                    return true;
                }
                Query databaseQuery = databaseReference.child("Health Record").orderByChild("name").equalTo(searchQuery);


                // Option 3: Search for cow IDs matching the query exactly


                searchCowDetails(databaseQuery);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return true;
            }
        });

        Rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Rb1.isChecked()) {
                    Rb2.setChecked(false);
                    tvH.setVisibility(View.VISIBLE);
                    recObservation.setVisibility(View.VISIBLE);
                    recObservation.setEnabled(false);
                    temp.setVisibility(View.GONE);
                    ape.setVisibility(View.GONE);
                    gen.setVisibility(View.GONE);

                }


            }
        });
        Rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Rb2.isChecked()) {
                    Rb1.setChecked(false);
                    tvH.setVisibility(View.GONE);
                    recObservation.setVisibility(View.VISIBLE);
                    recObservation.setEnabled(true);
                    temp.setVisibility(View.VISIBLE);
                    ape.setVisibility(View.VISIBLE);
                    gen.setVisibility(View.VISIBLE);
                    call.setVisibility(View.VISIBLE);
                    fabPhone.setVisibility(View.VISIBLE);


                }


            }
        });

        recObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temperature = temp.getText().toString();
                String appetite = ape.getText().toString();
                String appearance = gen.getText().toString();

                if (TextUtils.isEmpty(temperature)) {
                    temp.setError("Indicate existence of Fever or not");
                    return;
                }
                if (TextUtils.isEmpty(appetite)) {
                    ape.setError("Fill in the eating habit");
                    return;
                }
                if (TextUtils.isEmpty(appearance)) {
                    ape.setError("Indicate any abnormalities seen");
                    return;
                }
                DocumentReference userRef = fStore.collection("Msaponi ranch workers").document(uid);
                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String RanchHandnm = document.getString("Full name");
                                nmRanch.setText(RanchHandnm);



                                uploadObservationFirebase();


                            } else {
                                Toast.makeText(HealthActivity.this, "No such document", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(HealthActivity.this, "Error getting documents: ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        fabPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the device has the necessary permission
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // Request the permission
                    ActivityCompat.requestPermissions(HealthActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_REQUEST);
                } else {
                    // Permission is already granted, dial the number
                    dialNumber("0794843866"); // Replace "0794843866" with the actual number you want to dial
                }
            }
        });


    }
    private void dialNumber(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }


    private void searchCowDetails(Query query) {


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    processess.clear();
                    messageView.setText(""); // Clear the TextView message
                    messageView.setVisibility(View.GONE);
                    messageView1.setText(""); // Clear the TextView message
                    messageView1.setVisibility(View.GONE);
                    txLayout.setVisibility(View.GONE);
                    admDose.setVisibility(View.GONE);
                    Rb1.setEnabled(true);
                    Rb2.setEnabled(true);
                    tvS.setVisibility(View.GONE);

                    DataSnapshot childSnapshot = snapshot.getChildren().iterator().next();

                    // Process retrieved cow details here
                    String cowId = childSnapshot.child("name").getValue(String.class); // This should be "Cow 001" or the matching cow ID
                    Log.d("CowDetails", "Cow ID: " + cowId); // Debugging line

                    // Directly set the cowId to the TextView
                    tvN.setText(cowId);

                    // Access the "administration" child node directly from the snapshot
                    DataSnapshot adminSnapshot = childSnapshot.child("administration");
                    for (DataSnapshot adminProcess : adminSnapshot.getChildren()) {

                        AdministrationProcess administrationProcess = new AdministrationProcess();


                        String processName = adminProcess.getKey();
                        String dateAdministered = adminProcess.child("date administered").getValue().toString();
                        String dosageInstruction = adminProcess.child("dosage instruction").getValue().toString();
                        String dosageAmount = adminProcess.child("dosage amount").getValue().toString();
                        administrationProcess.setProcessName(processName);
                        administrationProcess.setDateAdministered(dateAdministered);
                        administrationProcess.setDosageInstruction(dosageInstruction);
                        administrationProcess.setDosageAmount(dosageAmount);


                        processess.add(administrationProcess);


                        if (processName.equals("Vaccination - Lumpivax") || processName.equals("Vaccination - Riftvax")) {
                            // Parse the dateAdministered string to a Date object
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()); // Adjust the pattern to match your date format
                            Date adminDate = null;
                            try {
                                adminDate = sdf.parse(dateAdministered);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            // Calculate the next administration date (add 1 year)
                            long nextAdminMillis = adminDate.getTime() + TimeUnit.DAYS.toMillis(365);
                            Date nextAdminDate = new Date(nextAdminMillis);

                            // Get the current date
                            Date currentDate = new Date();

                            // Check if it's time to administer the vaccine
                            if (currentDate.after(nextAdminDate) || currentDate.equals(nextAdminDate)) {



                                // It's time to administer the vaccine
                                if (processName.equals("Vaccination - Lumpivax")) {
                                    tvS.setVisibility(View.VISIBLE);
                                    tvS.setText("Administer dosage first");
                                    Rb1.setEnabled(false);
                                    Rb2.setEnabled(false);
                                    messageView.setVisibility(View.VISIBLE);
                                    messageView1.setVisibility(View.VISIBLE);
                                    admDose.setVisibility(View.VISIBLE);
                                    messageView.setText("Please vaccinate " + cowId + " with");
                                    messageView1.setText(processName);
                                    txLayout.setVisibility(View.VISIBLE);

                                } else if (processName.equals("Vaccination - Riftvax")) {
                                    tvS.setVisibility(View.VISIBLE);
                                    tvS.setText("Administer dosage first");
                                    Rb1.setEnabled(false);
                                    Rb2.setEnabled(false);
                                    messageView.setVisibility(View.VISIBLE);
                                    admDose.setVisibility(View.VISIBLE);
                                    messageView.setText("Please vaccinate " + cowId + " with\n" + processName);
                                    txLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        } else if (processName.equals("Vaccination - Contavax")) {

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()); // Adjust the pattern to match your date format
                            Date adminDate = null;
                            try {
                                adminDate = sdf.parse(dateAdministered);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            // Calculate the next administration date (add 6 months)
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(adminDate);
                            calendar.add(Calendar.MONTH, 6); // Add 6 months
                            Date nextAdminDate = calendar.getTime();

                            // Get the current date
                            Date currentDate = new Date();

                            // Check if it's time to administer the vaccine
                            if (currentDate.after(nextAdminDate) || currentDate.equals(nextAdminDate)) {
                                // It's time to administer the vaccine
                                // Your logic here
                                tvS.setVisibility(View.VISIBLE);
                                tvS.setText("Administer dosage first");
                                Rb1.setEnabled(false);
                                Rb2.setEnabled(false);
                                messageView.setVisibility(View.VISIBLE);
                                admDose.setVisibility(View.VISIBLE);
                                messageView.setText("Please vaccinate " + cowId + " with\n" + processName);
                                txLayout.setVisibility(View.VISIBLE);
                            }


                        } else if (processName.equals("Spraying - Triatix")) {

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()); // Adjust the pattern to match your date format
                            Date adminDate = null;
                            try {
                                adminDate = sdf.parse(dateAdministered);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            // Calculate the next administration date (add 1 year)
                            long nextAdminMillis = adminDate.getTime() + TimeUnit.DAYS.toMillis(21);
                            Date nextAdminDate = new Date(nextAdminMillis);

                            // Get the current date
                            Date currentDate = new Date();

                            // Check if it's time to administer the vaccine
                            if (currentDate.after(nextAdminDate) || currentDate.equals(nextAdminDate)) {
                                // It's time to administer the vaccine
                                tvS.setVisibility(View.VISIBLE);
                                tvS.setText("Administer dosage first");
                                Rb1.setEnabled(false);
                                Rb2.setEnabled(false);
                                messageView.setVisibility(View.VISIBLE);
                                admDose.setVisibility(View.VISIBLE);
                                messageView.setText("Please vaccinate " + cowId + " with\n" + processName);
                                txLayout.setVisibility(View.VISIBLE);
                            }


                        } else if (processName.equals("Deworming - Neflux")) {

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()); // Adjust the pattern to match your date format
                            Date adminDate = null;
                            try {
                                adminDate = sdf.parse(dateAdministered);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            // Calculate the next administration date (add 6 months)
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(adminDate);
                            calendar.add(Calendar.MONTH, 3); // Add 6 months
                            Date nextAdminDate = calendar.getTime();

                            // Get the current date
                            Date currentDate = new Date();
                            // Check if it's time to administer the vaccine
                            if (currentDate.after(nextAdminDate) || currentDate.equals(nextAdminDate)) {
                                // It's time to administer the vaccine
                                tvS.setVisibility(View.VISIBLE);
                                tvS.setText("Administer dosage first");
                                Rb1.setEnabled(false);
                                Rb2.setEnabled(false);
                                messageView.setVisibility(View.VISIBLE);
                                admDose.setVisibility(View.VISIBLE);
                                messageView.setText("Please vaccinate " + cowId + " with\n" + processName);
                                txLayout.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                    recyclerAdapter = new AdministrationProcessAdapter(getApplicationContext(), processess);
                    recyclerView.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(HealthActivity.this, "No cow found with the provided ID", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Log.e("CowDetails", "Error querying cow details", error.toException());
            }
        });
    }
    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();

        // Add one day to the current date to start from tomorrow
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        // Get the year, month, and day from the adjusted calendar
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Set the selected date
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("en", "KE"));
                        String selectedDate = sdf.format(calendar.getTime());
                        dateEditText.setText(selectedDate);
                    }
                }, year, month, day);

        // Set the minimum date to tomorrow's date
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }
    private void uploadObservationFirebase() {
        String ranchHandName = nmRanch.getText().toString();
        String cowname1  = tvN.getText().toString();
        String cowtemperature = temp.getText().toString();
        String cowappetite = ape.getText().toString();
        String cowappearance = gen.getText().toString();

        DatabaseReference conditionMadeRef = databaseReference.child("Cattle Current Condition");

        // Generate a unique key for the feed entry using push()

        CurrentConditionDetail currentConditionDetail = new CurrentConditionDetail(ranchHandName,cowname1,cowtemperature,cowappetite,cowappearance);
        String feedEntryKey = conditionMadeRef.push().getKey();

        conditionMadeRef.child(feedEntryKey).setValue(currentConditionDetail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(HealthActivity.this, cowname1 + " current condition sent to the Manager", Toast.LENGTH_SHORT).show();

                        // Clear displayed information after successful save
                        temp.setText("");
                        ape.setText("");
                        gen.setText("");
                        Rb2.setChecked(false);



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HealthActivity.this, "Failed to save cattle current condtion, please try again.", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PHONE_CALL_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, dial the number
                dialNumber("0794843866");
            } else {
                // Permission was denied, handle appropriately
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}