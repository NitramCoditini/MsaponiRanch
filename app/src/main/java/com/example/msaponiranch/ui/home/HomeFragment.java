package com.example.msaponiranch.ui.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msaponiranch.ManagerStuff.Model4;
import com.example.msaponiranch.ManagerStuff.RecyclerAdapter4;
import com.example.msaponiranch.ManagerStuff.TaskAssigning;
import com.example.msaponiranch.ManagerStuff.TaskDetail;
import com.example.msaponiranch.R;
import com.example.msaponiranch.RanchActivity;
import com.example.msaponiranch.databinding.FragmentHomeBinding;
import com.example.msaponiranch.ranchhandtasks.RecyclerAdapterTasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class HomeFragment extends Fragment {

    RecyclerView.Adapter recyclerAdapter;
    Calendar calendar;
    ImageView imageView;

    RecyclerView recyclerViewCategoryList3;

    ArrayList<TaskDetail> taskList;

    FirebaseDatabase firebaseDatabase;

    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    TextView done;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        imageView = root.findViewById(R.id.dateImage);
        done = root.findViewById(R.id.taskDate);


        calendar = Calendar.getInstance();


        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerViewCategoryList3 = root.findViewById(R.id.tasksRecyclerView);
        recyclerViewCategoryList3.setLayoutManager(linearLayoutManager);


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });



        taskList = new ArrayList<>();


        return root;
    }
    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("en", "KE"));
                        String selectedDate = sdf.format(calendar.getTime());
                        done.setText(selectedDate);
                        getDataFromFirebase();

                    }
                }, year, month, day);

        datePickerDialog.getDatePicker();
        datePickerDialog.show();
    }
    private void getDataFromFirebase() {
        Log.d("May", "Fetching data from Firebase");
        String date = done.getText().toString().trim();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            Query query = databaseReference.child("Assigned Tasks").orderByChild("workerUserId").equalTo(uid);
            Log.d("May", "Query created");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    taskList.clear();
                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                        Log.d("May", "Listener added");
                        TaskDetail model = new TaskDetail();
                        String cowName = snapshot.child("cowName").getValue(String.class);
                        String taskTitle = snapshot.child("title").getValue(String.class);
                        String taskDescription = snapshot.child("description").getValue(String.class);
                        String taskStart = snapshot.child("startTime").getValue(String.class);
                        String datePut = snapshot.child("enteredDate").getValue(String.class);
                        String taskEnd = snapshot.child("endTime").getValue(String.class);
                        int progressRating = snapshot.child("progressText").getValue(Integer.class);

                        if(datePut.equals(date)){
                            // The dates match
                            model.setTitle(taskTitle);
                            model.setDescription(taskDescription);
                            model.setCowName(cowName);
                            model.setEnteredDate(datePut);
                            model.setStartTime(taskStart);
                            model.setEndTime(taskEnd);
                            model.setProgressText(progressRating);
                            taskList.add(model);
                        }
                    }
                    // After processing all the data, set the adapters to the RecyclerViews
                    recyclerAdapter = new RecyclerAdapterTasks(getActivity(), taskList);
                    recyclerViewCategoryList3.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle errors if needed
                }
            });
        } else {
            Toast.makeText(getActivity(), "No signed in user", Toast.LENGTH_SHORT).show();
        }
    }

}