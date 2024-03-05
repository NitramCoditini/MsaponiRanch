package com.example.msaponiranch.ManagerStuff;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.msaponiranch.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CattleHealthRecord extends AppCompatActivity {

    private Spinner eventTypeSpinner;
    private Spinner specificItemSpinner;

    Calendar calendar;

    EditText dateEditText;

    ImageView datePickerIcon;

    private Spinner nameCowSpinner;
    private HashMap<String, List<String>> eventItemsMap;

    private HashMap<String, String> dosageAmountMap;

    private HashMap<String, String> dosageMap;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Button healthDone;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cattle_health_record);

        eventTypeSpinner = findViewById(R.id.event_type_spinner);
        specificItemSpinner = findViewById(R.id.specific_item_spinner);
        nameCowSpinner = findViewById(R.id.nameCow);
        dateEditText = findViewById(R.id.dateEditText);
        datePickerIcon = findViewById(R.id.datePickerIcon);
        healthDone = findViewById(R.id.healthRec);

        calendar = Calendar.getInstance();


        databaseReference = firebaseDatabase.getInstance().getReference();

        eventItemsMap = new HashMap<>();
        eventItemsMap.put("Vaccination", Arrays.asList("Lumpivax", "Riftvax", "Contavax"));
        eventItemsMap.put("Deworming", Arrays.asList("Neflux"));
        eventItemsMap.put("Spraying", Arrays.asList("Triatix"));

        datePickerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Set adapter for the first (event type) spinner
        ArrayAdapter<String> eventTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, eventItemsMap.keySet().toArray(new String[0]));
        eventTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventTypeSpinner.setAdapter(eventTypeAdapter);

        // Implement onItemSelectedListener for the event type spinner
        eventTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedEventType = (String) parent.getItemAtPosition(position);

                // Clear the selection and any existing items from the second spinner
                specificItemSpinner.setAdapter(null);

                // Check if selected event type has corresponding items in the map
                if (eventItemsMap.containsKey(selectedEventType)) {
                    List<String> specificItems = eventItemsMap.get(selectedEventType);

                    // Create and set adapter for the second spinner with specific items
                    ArrayAdapter<String> specificItemAdapter = new ArrayAdapter<>(
                            view.getContext(), android.R.layout.simple_spinner_item, specificItems);
                    specificItemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    specificItemSpinner.setAdapter(specificItemAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle if nothing is selected in the first spinner (optional)
            }
        });

        getDataFromFirebase();

        dosageMap = new HashMap<>();
        dosageMap.put("Lumpivax", "After Every 1 year");
        dosageMap.put("Riftvax", "After Every 1 year");
        dosageMap.put("Contavax", "After Every 6 months");
        dosageMap.put("Neflux", "After Every 3 months"); // Example for dewormer
        dosageMap.put("Triatix", "After Every 3 weeks"); // Example for spraying

        dosageAmountMap = new HashMap<>();
        dosageAmountMap.put("Lumpivax", "2ml");
        dosageAmountMap.put("Riftvax", "2ml");
        dosageAmountMap.put("Contavax", "0.5ml");
        dosageAmountMap.put("Neflux", "20ml");
        dosageAmountMap.put("Triatix", "10ml");

        healthDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadtoFirebase();
            }
        });
    }

    private void getDataFromFirebase() {

        Log.d("May", "Fetching data from Firebase");
        Query query = databaseReference.child("CattleDetails");
        Log.d("May", "Query created");
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                List<String> cowNames = new ArrayList<>();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Log.d("May", "Listener added");

                    String cowName = snapshot.child("cowname").getValue(String.class); // Access specific field
                    if (cowName != null) {
                        cowNames.add(cowName);
                    }
                    populateCowNameSpinner(cowNames);



                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors if needed
            }
        });
    }
    private void populateCowNameSpinner(List<String> cowNames) {
        if (nameCowSpinner != null) { // Check if spinner exists
            ArrayAdapter<String> nameCowAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, cowNames);
            nameCowAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            nameCowSpinner.setAdapter(nameCowAdapter);
        }
    }
    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("en", "KE"));
                        String selectedDate = sdf.format(calendar.getTime());
                        dateEditText.setText(selectedDate);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }
    private String getDosageInstruction(String selectedSpecificItem) {
        if (dosageMap.containsKey(selectedSpecificItem)) {
            return dosageMap.get(selectedSpecificItem);
        } else {
            return "Dosage information unavailable"; // Or a more appropriate message
        }
    }
    private String getDosageAmount(String selectedSpecificItem) {
        if (dosageAmountMap.containsKey(selectedSpecificItem)) {
            return dosageAmountMap.get(selectedSpecificItem);
        } else {
            return "Dosage amount unavailable"; // Or a more appropriate message
        }
    }
    private void uploadtoFirebase() {
        String selectedCowName = nameCowSpinner.getSelectedItem().toString();
        String selectedEventType = eventTypeSpinner.getSelectedItem().toString();
        String selectedSpecificItem = specificItemSpinner.getSelectedItem().toString();
        String dosageInstruction = getDosageInstruction(selectedSpecificItem); // Local function usage
        String dosageAmount = getDosageAmount(selectedSpecificItem); // Local function usage

        // Combine vaccination and specific item
        String combinedVaccination = selectedEventType + " - " + selectedSpecificItem;

        String enteredDate = dateEditText.getText().toString();

        // Input validation (optional)
        if (TextUtils.isEmpty(selectedCowName) || TextUtils.isEmpty(combinedVaccination) || TextUtils.isEmpty(enteredDate)) {
            Toast.makeText(CattleHealthRecord.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference databaseReference1 = firebaseDatabase.getInstance().getReference("Health Record");

        // Create a HashMap to store health record data
        HashMap<String, String> healthRecordMap = new HashMap<>();
        healthRecordMap.put("cow name", selectedCowName);
        healthRecordMap.put("administration process", combinedVaccination);
        healthRecordMap.put("date administered", enteredDate);
        healthRecordMap.put("dosage instruction", dosageInstruction);
        healthRecordMap.put("dosage amount", dosageAmount);

        // Push data to the Realtime Database
        databaseReference1.push().setValue(healthRecordMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    /* **Handle database write error** */

                    Log.w("failure", "Failed to save health record to database", error.toException()); // Use a meaningful TAG for logging
                    Toast.makeText(CattleHealthRecord.this, "An error occurred while saving health record. Please try again.", Toast.LENGTH_LONG).show();
                } else {
                    /* **Handle successful database write** */
                    Log.d("failure", "Health record saved successfully to database: " + ref.getKey()); // Log the generated record ID
                    Toast.makeText(CattleHealthRecord.this, "Health record saved successfully!", Toast.LENGTH_SHORT).show();

                    // Optionally clear form fields for a better user experience
                    dateEditText.setText("");
                    nameCowSpinner.setSelection(0);
                    specificItemSpinner.setSelection(0);
                    eventTypeSpinner.setSelection(0);
                }
            }
        });




    }


}