package com.example.msaponiranch.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.msaponiranch.MainActivity;
import com.example.msaponiranch.R;
import com.example.msaponiranch.SelectClass;
import com.example.msaponiranch.databinding.FragmentGalleryBinding;
import com.example.msaponiranch.livestockactivityclassess.AnimalBreeding;
import com.example.msaponiranch.livestockactivityclassess.AnimalFeeding;
import com.example.msaponiranch.livestockactivityclassess.AnimalList;
import com.example.msaponiranch.livestockactivityclassess.HealthActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class GalleryFragment extends Fragment {

    CardView cd1,cd2,cd3,cd4;

    DatabaseReference databaseReference;

    FirebaseDatabase firebaseDatabase;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        cd1 = root.findViewById(R.id.cattleCard);
        cd2 = root.findViewById(R.id.healthCard);
        cd3 = root.findViewById(R.id.feedingCard);
        cd4 = root.findViewById(R.id.breedingCard);

        databaseReference = firebaseDatabase.getInstance().getReference();

        cd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskProgress3();
            }
        });
        cd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskProgress1();

            }
        });
        cd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskProgress();

            }
        });

        cd4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskProgress2();

            }
        });


        return root;
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
                            taskFound = true;
                            // Retrieve the current progress value
                            int currentProgress = snapshot.child("progressText").getValue(Integer.class);

                            if(currentProgress == 0) {
                                // Increment the progress value
                                int newProgress = currentProgress + 10; // Assuming you want to increment by 10
                                // Update the progress value only if it's not already at the desired value
                                if (newProgress == 10) { // Assuming the maximum progress value is 100
                                    snapshot.getRef().child("progressText").setValue(newProgress)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Task updated successfully
                                                    Toast.makeText(getActivity(), "Task progress updated successfully", Toast.LENGTH_SHORT).show();
                                                    // Navigate to AnimalFeeding activity
                                                    Intent i = new Intent(getActivity(), AnimalFeeding.class);
                                                    startActivity(i);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to update task
                                                    Toast.makeText(getActivity(), "Failed to update task progress", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }else {
                                // Progress value is already at the maximum, navigate directly to AnimalFeeding activity
                                Intent i = new Intent(getActivity(), AnimalFeeding.class);
                                startActivity(i);
                            }
                        }
                    }
                    // If no task with title "Feeding" was found, show a message or navigate to a different activity
                    if (!taskFound) {
                        Toast.makeText(getActivity(), "Access denied. Please check your assigned task for today", Toast.LENGTH_SHORT).show();
                        // Optionally, navigate to a different activity or page
                        // Intent i = new Intent(getActivity(), SomeOtherActivity.class);
                        // startActivity(i);
                    }
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
    private  void taskProgress1(){
        String titleYours = "Health";
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
                            taskFound = true;
                            // Retrieve the current progress value
                            int currentProgress = snapshot.child("progressText").getValue(Integer.class);

                            if(currentProgress == 0) {
                                // Increment the progress value
                                int newProgress = currentProgress + 10; // Assuming you want to increment by 10
                                // Update the progress value only if it's not already at the desired value
                                if (newProgress == 10) { // Assuming the maximum progress value is 100
                                    snapshot.getRef().child("progressText").setValue(newProgress)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Task updated successfully
                                                    Toast.makeText(getActivity(), "Task progress updated successfully", Toast.LENGTH_SHORT).show();
                                                    // Navigate to AnimalFeeding activity
                                                    Intent i = new Intent(getActivity(), HealthActivity.class);
                                                    startActivity(i);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to update task
                                                    Toast.makeText(getActivity(), "Failed to update task progress", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }else {
                                // Progress value is already at the maximum, navigate directly to AnimalFeeding activity
                                Intent i = new Intent(getActivity(), HealthActivity.class);
                                startActivity(i);
                            }
                        }
                    }
                    // If no task with title "Feeding" was found, show a message or navigate to a different activity
                    if (!taskFound) {
                        Toast.makeText(getActivity(), "Access denied. Please check your assigned task for today", Toast.LENGTH_SHORT).show();
                        // Optionally, navigate to a different activity or page
                        // Intent i = new Intent(getActivity(), SomeOtherActivity.class);
                        // startActivity(i);
                    }
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
    private  void taskProgress2(){
        String titleYours = "Breeding";
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
                            taskFound = true;
                            // Retrieve the current progress value
                            int currentProgress = snapshot.child("progressText").getValue(Integer.class);

                            if(currentProgress == 0) {
                                // Increment the progress value
                                int newProgress = currentProgress + 10; // Assuming you want to increment by 10
                                // Update the progress value only if it's not already at the desired value
                                if (newProgress == 10) { // Assuming the maximum progress value is 100
                                    snapshot.getRef().child("progressText").setValue(newProgress)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Task updated successfully
                                                    Toast.makeText(getActivity(), "Task progress updated successfully", Toast.LENGTH_SHORT).show();
                                                    // Navigate to AnimalFeeding activity
                                                    Intent i = new Intent(getActivity(), AnimalBreeding.class);
                                                    startActivity(i);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to update task
                                                    Toast.makeText(getActivity(), "Failed to update task progress", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }else {
                                // Progress value is already at the maximum, navigate directly to AnimalFeeding activity
                                Intent i = new Intent(getActivity(), AnimalBreeding.class);
                                startActivity(i);
                            }
                        }
                    }
                    // If no task with title "Feeding" was found, show a message or navigate to a different activity
                    if (!taskFound) {
                        Toast.makeText(getActivity(), "Access denied. Please check your assigned task for today", Toast.LENGTH_SHORT).show();
                        // Optionally, navigate to a different activity or page
                        // Intent i = new Intent(getActivity(), SomeOtherActivity.class);
                        // startActivity(i);
                    }
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
    private  void taskProgress3(){
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
                            taskFound = true;
                            // Retrieve the current progress value
                            int currentProgress = snapshot.child("progressText").getValue(Integer.class);

                            if(currentProgress == 0) {
                                // Increment the progress value
                                int newProgress = currentProgress + 25; // Assuming you want to increment by 10
                                // Update the progress value only if it's not already at the desired value
                                if (newProgress == 25) { // Assuming the maximum progress value is 100
                                    snapshot.getRef().child("progressText").setValue(newProgress)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Task updated successfully
                                                    Toast.makeText(getActivity(), "Task progress updated successfully", Toast.LENGTH_SHORT).show();
                                                    // Navigate to AnimalFeeding activity
                                                    Intent i = new Intent(getActivity(), AnimalList.class);
                                                    startActivity(i);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to update task
                                                    Toast.makeText(getActivity(), "Failed to update task progress", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }else {
                                // Progress value is already at the maximum, navigate directly to AnimalFeeding activity
                                Intent i = new Intent(getActivity(), AnimalList.class);
                                startActivity(i);
                            }
                        }
                    }
                    // If no task with title "Feeding" was found, show a message or navigate to a different activity
                    if (!taskFound) {
                        Toast.makeText(getActivity(), "Access denied. Please check your assigned task for today", Toast.LENGTH_SHORT).show();
                        // Optionally, navigate to a different activity or page
                        // Intent i = new Intent(getActivity(), SomeOtherActivity.class);
                        // startActivity(i);
                    }
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