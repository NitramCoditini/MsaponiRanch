package com.example.msaponiranch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msaponiranch.InventoryStuff.Model3;
import com.example.msaponiranch.InventoryStuff.RecyclerAdapter3;
import com.example.msaponiranch.ManagerStuff.CattleCondition;
import com.example.msaponiranch.ManagerStuff.Model4;
import com.example.msaponiranch.ManagerStuff.RecyclerAdapter4;
import com.example.msaponiranch.ManagerStuff.TaskAssigning;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RanchActivity extends AppCompatActivity {

    RecyclerView.Adapter recyclerAdapter;

    Calendar calendar;
    ImageView imageView;

    RecyclerView recyclerViewCategoryList3;

    ArrayList<Model4> modelArrayList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    TextView fed;

    RadioButton Rb1,Rb2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranch);

        imageView = findViewById(R.id.dateImage);
        fed = findViewById(R.id.fedDate);
        Rb1 = findViewById(R.id.taskradioButton1);
        Rb2 = findViewById(R.id.conditionradioButton1);

        calendar = Calendar.getInstance();


        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerViewCategoryList3 = findViewById(R.id.feedRecyclerView);
        recyclerViewCategoryList3.setLayoutManager(linearLayoutManager);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        Rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Rb1.isChecked()) {
                    Rb2.setEnabled(false);
                    Intent i = new Intent(RanchActivity.this, TaskAssigning.class);
                    startActivity(i);
                }

            }
        });

        Rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Rb2.isChecked()) {
                    Rb1.setEnabled(false);
                    Intent i = new Intent(RanchActivity.this, CattleCondition.class);
                    startActivity(i);
                }

            }
        });

        modelArrayList = new ArrayList<>();





        // Clear data for each RecyclerView separately
        clearAll();

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
                        fed.setText(selectedDate);
                        getDataFromFirebase();
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void getDataFromFirebase() {

        Log.d("May", "Fetching data from Firebase");
        String date = fed.getText().toString().trim();
        Query query = databaseReference.child("feedMade");
        Log.d("May", "Query created");
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                modelArrayList.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Log.d("May", "Listener added");

                    Model4 model = new Model4();

                    String cowName = snapshot.child("cowName").getValue(String.class);
                    String ranchHandName = snapshot.child("ranchHandName").getValue(String.class);
                    String feedNameOf = snapshot.child("feedNameOf").getValue(String.class);
                    Double quantity = snapshot.child("quantity").getValue(Double.class);
                    Long currentTime = snapshot.child("currentTime").getValue(Long.class);

                    Date date2 = new Date(currentTime);

                    // Format the Date object to a string in "dd/MM/yyyy" format
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String formattedDate = sdf.format(date2);
                    Log.d("DateConversion", "Formatted itemDateString: " + formattedDate);

                    if (formattedDate.equals(date)) {
                        // The dates match
                        // Proceed with your logic
                        model.setRanchHandName(ranchHandName);
                        model.setCowName(cowName);
                        model.setFeedNameOf(feedNameOf);
                        model.setQuantity(quantity);
                        model.setCurrentTime(currentTime);

                        modelArrayList.add(model);
                    } else {
                        // The dates do not match
                        // Proceed accordingly
                        Toast.makeText(RanchActivity.this, "Currently no feed done during this date", Toast.LENGTH_SHORT).show();
                    }






                }
                Log.d("LIST_SIZE", "List size: " + modelArrayList.size());
                // After processing all the data, set the adapters to the RecyclerViews
                recyclerAdapter = new RecyclerAdapter4(getApplicationContext(), modelArrayList);
                recyclerViewCategoryList3.setAdapter(recyclerAdapter);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        // Uncheck all radio buttons
        Rb1.setChecked(false);
        Rb2.setChecked(false);
        // Enable all radio buttons
        Rb1.setEnabled(true);
        Rb2.setEnabled(true);
    }
}