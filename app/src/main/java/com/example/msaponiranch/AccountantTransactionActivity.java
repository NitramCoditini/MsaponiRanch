package com.example.msaponiranch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
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

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AccountantTransactionActivity extends AppCompatActivity {

    RecyclerView.Adapter recyclerAdapter;

    RecyclerView recyclerViewCategoryList2;

    ArrayList<Model2> modelArrayList;

    Button update, tim;

    EditText addBud;

    ImageView dateImage;

    TextView initial,remaining,disabledText;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    String selectedDate;

    String formattedMonth;

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
        dateImage = findViewById(R.id.dateImage);
        dateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMonthYearPicker();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
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

                // Create an AlertDialog to confirm the budget amount
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountantTransactionActivity.this);
                builder.setTitle("Confirm Budget")
                        .setMessage("Are you sure this is the budget you want to set?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Proceed with setting the initial text and uploading to Firebase
                                initial.setText("Ksh " + enterBudget);
                                uploadToFirebase(budgetAmount);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Do nothing or handle the 'No' case as needed
                            }
                        });
                builder.create().show();
            }
        });
    }

    public void showMonthYearPicker() {
        final Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH); // 0-based indexing for month

        final String[] months = getResources().getStringArray(R.array.months_array); // Replace with your months array resource

        AlertDialog.Builder builder = new AlertDialog.Builder(AccountantTransactionActivity.this);


        View dialogView = getLayoutInflater().inflate(R.layout.month_year_picker_dialog, null);

        // Initialize views from custom layout (replace with your view IDs)
        ImageView rightImageView = dialogView.findViewById(R.id.rightImage);
        ImageView leftImageView = dialogView.findViewById(R.id.leftImage);
        TextView yearTextView = dialogView.findViewById(R.id.year_text_view);
        NumberPicker monthPicker = dialogView.findViewById(R.id.month_picker);


        // Set year text
        yearTextView.setText(String.valueOf(currentYear));

        // Set up month picker
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(months.length);
        monthPicker.setDisplayedValues(months);
        monthPicker.setValue(currentMonth + 1); // Adjust for 0-based indexing

        // Increase year button click listener
        rightImageView.setOnClickListener(v -> {
            int year = Integer.parseInt(yearTextView.getText().toString());
            yearTextView.setText(String.valueOf(year + 1));
        });

        // Decrease year button click listener
        leftImageView.setOnClickListener(v -> {
            int year = Integer.parseInt(yearTextView.getText().toString());
            if (year > 1900) { // Adjust minimum year as needed
                yearTextView.setText(String.valueOf(year - 1));
            }
        });
        builder.setTitle("Select Month & Year")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int selectedMonth = monthPicker.getValue() - 1; // Adjust for 0-based indexing
                        int selectedYear = Integer.parseInt(yearTextView.getText().toString());

                        // Format selected month as two-digit string
                        formattedMonth = String.format("%02d", selectedMonth + 1); // Add 1 for actual month value

                        // Format full date (month/year)
                        selectedDate = formattedMonth + "/" + selectedYear;
                        Log.d("DateConversion", "Formatted DateString: " + selectedDate);
                        getDataFromFirebase(selectedDate);

                        // Handle selected month and full date (e.g., update UI, perform actions)
                        // You can use "selectedMonth" and "selectedDate" for further processing


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do nothing or handle the 'No' case as needed
                    }
                });


        builder.setView(dialogView);
        builder.create().show();
    }


    private void getDataFromFirebase( String MonthYear) {
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

                    String dateFire = snapshot.child("date").getValue(String.class);
                    model.setDate(dateFire);
                    model.setFeedName(snapshot.child("feedName").getValue().toString());
                    model.setFeedWeight(snapshot.child("feedWeight").getValue().toString());
                    model.setTotalPriceStr(snapshot.child("totalPriceStr").getValue().toString());

                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/yyyy");

                    YearMonth yearMonth = YearMonth.parse(dateFire, inputFormatter);

                    // Format the YearMonth object to the desired string format
                    String formattedDateFire = yearMonth.format(outputFormatter);
                    Log.d("DateConversion", "Formatted: " + formattedDateFire);// Output: 03/2024
                    Log.d("DateConversion", "Formatted new: " + MonthYear);

                    // Ensure selectedDate is correctly formatted and matches formattedDateFire
                    if (formattedDateFire.equals(MonthYear)) {


                        modelArrayList.add(model);
                    }else{
                        Toast.makeText(AccountantTransactionActivity.this, "Selected Month and year have no purchase history", Toast.LENGTH_SHORT).show();
                    }
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
                Log.e("FirebaseError", "Error fetching data: ", error.toException());
            }
        });
    }


    private void timeDelay() {
        databaseReference.child("lastUpdated").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String lastUpdatedDate = snapshot.getValue(String.class);
                    // Proceed to calculate and handle button state

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date lastDate = null;
                    try {
                        lastDate = sdf.parse(lastUpdatedDate);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }


                    // Calculate the next add budget button (add 1 month)
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(lastDate);
                    calendar.add(Calendar.MONTH, 1);
                    Date nextAdminDate = calendar.getTime();

                    // Format the Date object to a string in "dd/MM/yyyy" format
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String endDate = sdf1.format(nextAdminDate);
                    Log.d("DateConversion", "Formatted itemDateString: " + endDate);

                    // get todays date
                    // Format the Date object to a string in "dd/MM/yyyy" format
                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String todayDate = sdf2.format(new Date());
                    Log.d("DateConversion", "Formatted today: " + todayDate);


                    disabledText.setText("End Date: " + endDate + "Add new budget");

                    if (todayDate.equals(endDate)){
                        update.setEnabled(true);
                        tim.setEnabled(false);
                    }else{
                        Toast.makeText(AccountantTransactionActivity.this, "Check the end date to know when you can add new budget", Toast.LENGTH_SHORT).show();


                    }





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
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String todayDate = sdf1.format(new Date());
                Log.d("DateConversion", "Formatted today: " + todayDate);
                databaseReference.child("lastUpdated").setValue(todayDate);

                Toast.makeText(AccountantTransactionActivity.this, "Budget updated", Toast.LENGTH_SHORT).show();
                addBud.setText("");
                update.setEnabled(false);
                disabledText.setText("Click time delay first");



            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AccountantTransactionActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


}