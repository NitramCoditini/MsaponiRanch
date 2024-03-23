package com.example.msaponiranch.ManagerStuff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.msaponiranch.R;
import com.example.msaponiranch.livestockactivityclassess.MilkingDetail;
import com.example.msaponiranch.livestockactivityclassess.MilkingRecord;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AssignmentTask extends AppCompatActivity {

    String workerName;
    String workerUserId;

    private Spinner nameCowSpinner;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ProgressBar progressWorker;



    Calendar calendar;



    TextView nm, pgText;
    EditText titl,dateEditText,stTime,enTime, desc;

    Button assign;

    ImageView datePickerIcon,stTimePick, enTimePick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_task);

        progressWorker = findViewById(R.id.progressbar);
        nm = findViewById(R.id.assName);
        nameCowSpinner = findViewById(R.id.nameCow);
        titl = findViewById(R.id.titleEd);
        dateEditText = findViewById(R.id.dateEd);
        datePickerIcon = findViewById(R.id.datePickerIcon);
        pgText = findViewById(R.id.progressText);
        stTime = findViewById(R.id.startTime);
        stTimePick = findViewById(R.id.timePickerIcon);
        enTimePick = findViewById(R.id.timePickerIconEnd);
        enTime = findViewById(R.id.endTime);
        desc = findViewById(R.id.taskDesc);
        assign = findViewById(R.id.buttonget);

        calendar = Calendar.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference();

        // Retrieve the position from the Intent
        Intent intent = getIntent();

        // Retrieve the cow's name and breed from the Intent
        workerName = intent.getStringExtra("worker_name_key");
        workerUserId = intent.getStringExtra("worker_userId_key");

        nm.setText(workerName);

        datePickerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        enTimePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AssignmentTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        calendar.set(Calendar.MINUTE, selectedMinute);

                        // Format the time using SimpleDateFormat
                        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
                        String selectedTime = sdf.format(calendar.getTime());

                        // Set the formatted time to the EditText
                        enTime.setText(selectedTime);
                    }
                }, hour, minute, false); // false for 12-hour format
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();

            }
        });


        stTimePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AssignmentTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        calendar.set(Calendar.MINUTE, selectedMinute);

                        // Format the time using SimpleDateFormat
                        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
                        String selectedTime = sdf.format(calendar.getTime());

                        // Set the formatted time to the EditText
                        stTime.setText(selectedTime);
                    }
                }, hour, minute, false); // false for 12-hour format
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();

            }
        });
        getDataFromFirebase();

        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titl.getText().toString();
                String enteredDate = dateEditText.getText().toString();
                String startTime = stTime.getText().toString();
                String endTime = enTime.getText().toString();
                String description = desc.getText().toString();



                if (TextUtils.isEmpty(title)) {
                    titl.setError("Title is required!");
                    return;
                }

                if (TextUtils.isEmpty(enteredDate)) {
                    dateEditText.setError("Date is required!");
                    return;
                }
                if (TextUtils.isEmpty(startTime)) {
                    stTime.setError("Start time is required!");
                    return;
                }
                if (TextUtils.isEmpty(endTime)) {
                    enTime.setError("End time is required!");
                    return;
                }
                if (TextUtils.isEmpty(description)) {
                    desc.setError("Description is required!");
                    return;
                }



                uploadToFirebase();

            }
        });
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

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    private void uploadToFirebase() {
        String title = titl.getText().toString();
        String enteredDate = dateEditText.getText().toString();
        String startTime = stTime.getText().toString();
        String endTime = enTime.getText().toString();
        String description = desc.getText().toString();
        String ranchHand = nm.getText().toString();
        String cowName = nameCowSpinner.getSelectedItem().toString();
        String progressTextString = pgText.getText().toString();
        // Remove the "%" sign from the string
        progressTextString = progressTextString.replace("%", "");
        // Parse the string to an integer
        int progressText = Integer.parseInt(progressTextString);


        DatabaseReference taskMadeRef = databaseReference.child("Assigned Tasks");

        // Generate a unique key for the feed entry using push()

        TaskDetail taskDetail = new TaskDetail(workerUserId,title,description,cowName,enteredDate,startTime,endTime,progressText);
        String taskEntryKey = taskMadeRef.push().getKey();

        taskMadeRef.child(taskEntryKey).setValue(taskDetail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(AssignmentTask.this, ranchHand + " has been assigned a task successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AssignmentTask.this , TaskAssigning.class);
                        startActivity(intent);
                        finish();




                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AssignmentTask.this, "Failed to assign task, please try again.", Toast.LENGTH_SHORT).show();
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

}