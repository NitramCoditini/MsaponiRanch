package com.example.msaponiranch.livestockactivityclassess;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msaponiranch.CattleAdding;
import com.example.msaponiranch.CattleDetail;
import com.example.msaponiranch.Model;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AnimalBreeding extends AppCompatActivity implements RecyclerAdapterBreeding.OnCownameClickListener {

    public static final int GALLERY_REQUEST_CODE = 105;

    RecyclerView.Adapter recyclerAdapter;

    public static final String TAG = "TAG";

    DatabaseReference databaseReference;

    ArrayList<Model> modelArrayList;

    Calendar calendar;

    FirebaseFirestore fStore;

    FirebaseAuth mAuth;
    String uid;

    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseUser currentUser;



    EditText dateEditText,cownm,cowag,cowWe;

    ProgressBar pgb;

    Button recPreg,recCalv;

    ImageButton imageButton;

    ImageView datePickerIcon;

    Uri imguri;

    TextView cowP,cowP1,profdes;

    LinearLayout pregnantLayout,deliveredLayout;

    String ranchHandName;

    RecyclerView recyclerViewCategoryList;

    private Spinner nameCowSpinner,nameCowSpinner1,spinner1,spinnerBreed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_breeding);

        nameCowSpinner = findViewById(R.id.nameCow);
        cowP = findViewById(R.id.cowParent);
        cowP1 = findViewById(R.id.cowParent1);
        nameCowSpinner1 = findViewById(R.id.nameCow1);
        pregnantLayout = findViewById(R.id.linearPregnancy);
        deliveredLayout = findViewById(R.id.linearCalving);
        recPreg = findViewById(R.id.pregnancyRec);
        recCalv = findViewById(R.id.calfReg);
        dateEditText = findViewById(R.id.dateEditText);
        datePickerIcon = findViewById(R.id.datePickerIcon);
        imageButton = findViewById(R.id.cowimage);
        profdes = findViewById(R.id.cowinfo);
        cownm = findViewById(R.id.calfName);
        spinnerBreed = findViewById(R.id.calfBreed);
        cowWe = findViewById(R.id.calfWeight);
        spinner1 = findViewById(R.id.gender);
        cowag  = findViewById(R.id.calfAge);
        pgb = findViewById(R.id.cowRegProgressBar);




        calendar = Calendar.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        uid = currentUser.getUid();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();





        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCategoryList = findViewById(R.id.cattle1RecyclerView);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profdes.setVisibility(View.GONE);
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                gallery.setType("image/*");
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });



        datePickerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        // Initialize ArrayLists separately for each data source
        modelArrayList = new ArrayList<>();
        getDataFromFirebase();
        getDataFromFirebase1();
        getDataFromFirebase2();




        // Clear data for each RecyclerView separately
        clearAll();


        recPreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredDate = dateEditText.getText().toString();
                String cowNamePregnant = cowP.getText().toString();

                if (TextUtils.isEmpty(enteredDate)) {
                    dateEditText.setError("Enter the date of delivery");
                    return;
                }
                if (TextUtils.isEmpty(cowNamePregnant)) {
                    cowP.setError("First select the cow from the cattle details");
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
                                //nmRanch.setText(RanchHandnm);



                                uploadtoFirebase();


                            } else {
                                Toast.makeText(AnimalBreeding.this, "No such document", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(AnimalBreeding.this, "Error getting documents: ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        recCalv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cowname = cownm.getText().toString().trim();
                String cowage = cowag.getText().toString().trim();
                String cowweight = cowWe.getText().toString().trim();
                Drawable drawable = imageButton.getDrawable();

                if (drawable == null) {
                    Toast.makeText(AnimalBreeding.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(cowname)) {
                    cownm.setError("Calf name is required!");
                    return;
                }


                if (TextUtils.isEmpty(cowage)) {
                    cowag.setError("Calf age is required!");
                    return;
                }
                if (TextUtils.isEmpty(cowweight)) {
                    cowWe.setError("Calf weight is required!");
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
                                //nmRanch.setText(RanchHandnm);



                                uploadImageFirebase(imguri);


                            } else {
                                Toast.makeText(AnimalBreeding.this, "No such document", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(AnimalBreeding.this, "Error getting documents: ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });





            }
        });


    }

    public void getDataFromFirebase() {

        Log.d("May", "Fetching data from Firebase");
        Query query = databaseReference.child("CattleDetails");
        Log.d("May", "Query created");
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                modelArrayList.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Log.d("May", "Listener added");

                    Model model = new Model();

                    model.setImageId(snapshot.child("imageId").getValue().toString());
                    model.setCowname(snapshot.child("cowname").getValue().toString());
                    model.setCowbreed(snapshot.child("cowbreed").getValue().toString());
                    model.setCowweight(snapshot.child("cowweight").getValue().toString());
                    model.setAgeWithUnit(snapshot.child("ageWithUnit").getValue().toString());
                    model.setGender(snapshot.child("gender").getValue().toString());


                    modelArrayList.add(model);



                }
                Log.d("LIST_SIZE", "List size: " + modelArrayList.size());
                // After processing all the data, set the adapters to the RecyclerViews
                recyclerAdapter = new RecyclerAdapterBreeding(getApplicationContext(), modelArrayList, AnimalBreeding.this);
                recyclerViewCategoryList.setAdapter(recyclerAdapter);
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
    private void getDataFromFirebase1() {

        Log.d("May", "Fetching data from Firebase");
        Query query = databaseReference.child("CattleDetails");
        Log.d("May", "Query created");
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                List<String> cowNames = new ArrayList<>();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Log.d("May", "Listener added");

                    String gender = snapshot.child("gender").getValue(String.class);

                    if (gender.equals("Male")){
                        String cowName = snapshot.child("cowname").getValue(String.class); // Access specific field
                        if (cowName != null) {
                            cowNames.add(cowName);
                        }
                        populateCowNameSpinner(cowNames);

                    }




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
    private void getDataFromFirebase2() {

        Log.d("May", "Fetching data from Firebase");
        Query query = databaseReference.child("CattleDetails");
        Log.d("May", "Query created");
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                List<String> cowNames = new ArrayList<>();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Log.d("May", "Listener added");

                    String gender = snapshot.child("gender").getValue(String.class);

                    if (gender.equals("Male")){
                        String cowName = snapshot.child("cowname").getValue(String.class); // Access specific field
                        if (cowName != null) {
                            cowNames.add(cowName);
                        }
                        populateCowNameSpinner1(cowNames);

                    }




                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors if needed
            }
        });
    }
    private void populateCowNameSpinner1(List<String> cowNames) {
        if (nameCowSpinner1 != null) { // Check if spinner exists
            ArrayAdapter<String> nameCowAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, cowNames);
            nameCowAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            nameCowSpinner1.setAdapter(nameCowAdapter);
        }
    }




    @Override
    public void onCownameClick(String cowname, String pregnant) {

        cowP.setText(cowname);
        cowP1.setText(cowname);

        if (pregnant.equals("pregnant")) {
            pregnantLayout.setVisibility(View.VISIBLE);
            deliveredLayout.setVisibility(View.GONE);
            taskProgress();

        } else { // pregnant.equals("Just Delivered")
            pregnantLayout.setVisibility(View.GONE);
            deliveredLayout.setVisibility(View.VISIBLE);
            taskProgress();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK){
                imguri = data.getData();
                imageButton.setImageURI(imguri);



            }
        }


    }
    private void uploadtoFirebase() {
        String femalePregnant  = cowP.getText().toString();
        String malePartner = nameCowSpinner.getSelectedItem().toString();
        String datetoDeliver = dateEditText.getText().toString();

        DatabaseReference pregnancyMadeRef = databaseReference.child("Pregnant Cattle");

        // Generate a unique key for the feed entry using push()

        PregnancyDetail pregnancyDetail = new PregnancyDetail(ranchHandName,femalePregnant,malePartner,datetoDeliver);
        String pregEntryKey = pregnancyMadeRef.push().getKey();

        pregnancyMadeRef.child(pregEntryKey).setValue(pregnancyDetail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(AnimalBreeding.this, femalePregnant + " current set as pregnant", Toast.LENGTH_SHORT).show();

                        // Clear displayed information after successful save
                        cowP.setText("");
                        dateEditText.setText("");
                        taskProgress1();




                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AnimalBreeding.this, "Failed to add cattle pregnancy details, please try again.", Toast.LENGTH_SHORT).show();
                    }
                });


    }
    private void uploadImageFirebase(Uri contentUri) {
        StorageReference image = storageReference.child(" image cows/" + System.currentTimeMillis() + "." + getFileExt(contentUri));
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(AnimalBreeding.this, "Image is uploaded", Toast.LENGTH_SHORT).show();
                        String femaleParent  = cowP1.getText().toString();
                        String maleParent = nameCowSpinner1.getSelectedItem().toString();
                        String calfName = cownm.getText().toString().trim();
                        String calfBreed = spinnerBreed.getSelectedItem().toString().trim();
                        String calfAge = cowag.getText().toString().trim() + "days";
                        String calfWeight = cowWe.getText().toString().trim() + " kg";
                        String calfGender = spinner1.getSelectedItem().toString().trim();

                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Calf Details");



                        CalfDetail calfDetail = new CalfDetail(uri.toString(),ranchHandName,femaleParent,maleParent,calfName,calfGender,calfBreed,calfAge,calfWeight);
                        String imageId = databaseReference1.push().getKey();
                        databaseReference1.child(imageId).setValue(calfDetail);
                        Toast.makeText(AnimalBreeding.this, "Calf added", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onSuccess: Uploaded Image url is: " + uri.toString());


                        imageButton.setImageURI(null);
                        profdes.setVisibility(View.VISIBLE);
                        cownm.setText("");
                        cowP1.setText("");
                        cowag.setText("");
                        cowWe.setText("");
                        pgb.setVisibility(View.GONE);
                        taskProgress1();
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pgb.setVisibility(View.GONE);
                Toast.makeText(AnimalBreeding.this, "Upload Failed", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private String getFileExt(Uri contentUri) {
        //allow all support types of gallery
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(c.getType(contentUri));
    }
    private  void taskProgress(){
        String titleYours = "Breeding";
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            Query query = databaseReference.child("Assigned Tasks").orderByChild("workerUserId").equalTo(uid);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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
                                                    Toast.makeText(AnimalBreeding.this, "Task progress updated successfully", Toast.LENGTH_SHORT).show();
                                                    // Navigate to AnimalFeeding activity

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to update task
                                                    Toast.makeText(AnimalBreeding.this, "Failed to update task progress", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(AnimalBreeding.this, "No signed in user", Toast.LENGTH_SHORT).show();
        }


    }
    private  void taskProgress1(){
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
                            // Retrieve the current progress value
                            int currentProgress = snapshot.child("progressText").getValue(Integer.class);
                            // Increment the progress value
                            if(currentProgress == 40) {
                                int newProgress = currentProgress + 60; // Assuming you want to increment by 10
                                // Update the progress value only if it's not already at the desired value
                                if (newProgress == 100) { // Assuming the maximum progress value is 100
                                    snapshot.getRef().child("progressText").setValue(newProgress)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Task updated successfully
                                                    Toast.makeText(AnimalBreeding.this, "Task progress updated successfully", Toast.LENGTH_SHORT).show();
                                                    // Navigate to AnimalFeeding activity

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to update task
                                                    Toast.makeText(AnimalBreeding.this, "Failed to update task progress", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(AnimalBreeding.this, "No signed in user", Toast.LENGTH_SHORT).show();
        }


    }
}