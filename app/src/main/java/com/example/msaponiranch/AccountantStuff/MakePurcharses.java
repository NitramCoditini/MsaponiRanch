package com.example.msaponiranch.AccountantStuff;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msaponiranch.CattleAdding;
import com.example.msaponiranch.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MakePurcharses extends AppCompatActivity {


    public static final String TAG = "imageInfo";
    public static final int GALLERY_REQUEST_CODE = 105;

    Spinner spinner1;

    Button total, purchase;
    EditText itemWe, dateEditText;
    TextView profdes, tot, budget,err;
    ImageView datePickerIcon;
    Uri imguri;

    ProgressBar pgb;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    Calendar calendar;
    Float initialBudget = 0.0f;
    ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_purcharses);

        imageButton = findViewById(R.id.itemimage);
        spinner1 = findViewById(R.id.itemfeed);
        total = findViewById(R.id.calTot);
        itemWe = findViewById(R.id.itemWeight);
        tot = findViewById(R.id.priceTotal);
        profdes = findViewById(R.id.iteminfo);
        purchase = findViewById(R.id.makePur);
        budget = findViewById(R.id.currentBudget);
        dateEditText = findViewById(R.id.dateEditText);
        datePickerIcon = findViewById(R.id.datePickerIcon);
        pgb = findViewById(R.id.itemProgressBar);
        err = findViewById(R.id.errorMessage);

        calendar = Calendar.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Feed_available, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner1.setAdapter(adapter);

        datePickerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profdes.setVisibility(View.GONE);
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                gallery.setType("image/*");
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });
        databaseReference.child("Budget Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Float currentBudget = snapshot.child("amount").getValue(Float.class);
                    if (currentBudget != null) {
                        budget.setText("Ksh " + currentBudget);
                        initialBudget = currentBudget; // Store for later calculations
                    } else {
                        // Handle missing budget data or errors
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database errors
            }
        });

        Map<String, Float> itemPrices = new HashMap<>();
        itemPrices.put("Maize germ", 35.0f); // Assuming 10 per kg for Maize germ
        itemPrices.put("Wheat pollard", 25.0f);
        itemPrices.put("Wheat bran", 17.0f);
        itemPrices.put("Soya meal", 28.0f);
        itemPrices.put("Di-calcium Phosphate", 123.0f);
        itemPrices.put("Molasses", 58.0f);
        itemPrices.put("Cotton seedcake", 73.0f);
        itemPrices.put("Fish meal", 73.0f);
        itemPrices.put("Maclik super", 230.0f);
        itemPrices.put("Triatix Spray", 1500.0f);

        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedItem = spinner1.getSelectedItem().toString();
                float pricePerUnit = itemPrices.get(selectedItem);

                String weightStr = itemWe.getText().toString().trim();

                if (TextUtils.isEmpty(weightStr)) {
                    itemWe.setError("size is required!");
                    return;
                }


                try {
                    float weight = Float.parseFloat(weightStr);

                    // Calculate total price
                    float totalPrice = pricePerUnit * weight;
                    tot.setText(String.format("Ksh %.2f", totalPrice));
                    purchase.setEnabled(true);
                    err.setVisibility(View.GONE);





                    // Display total price in a TextView or other UI element
                    // ... (e.g., totalPriceTextView.setText(String.format("Ksh %.2f", totalPrice));
                } catch (NumberFormatException e) {
                    purchase.setEnabled(false);
                    // Handle error: Show message if weight is not a valid number
                    // ...
                }


            }
        });
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String totalPriceStr = tot.getText().toString().trim();
                String date = dateEditText.getText().toString().trim();
                Drawable drawable = imageButton.getDrawable();

                if (TextUtils.isEmpty(date)) {
                    dateEditText.setError("Date is required!");
                    return;
                }



                if (drawable == null) {
                    Toast.makeText(MakePurcharses.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    return;
                }


                // Parse total price to float (handle potential errors)
                float totalPrice = 0.0f;
                try {
                    totalPrice = Float.parseFloat(totalPriceStr.substring(4)); // Assuming "Ksh " prefix
                } catch (NumberFormatException e) {
                    Toast.makeText(MakePurcharses.this, "Invalid total price format!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check if total price is valid and within budget
                if (totalPrice <= 0 || totalPrice > initialBudget) {
                    String message = "Invalid total price: ";
                    if (totalPrice <= 0) {
                        message += "must be positive";
                    } else {
                        message += "exceeds budget (Ksh " + initialBudget + ")";
                    }
                    Toast.makeText(MakePurcharses.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }

                final float newBudget = initialBudget - totalPrice;
                databaseReference.child("Budget Details").child("amount").setValue(newBudget, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error != null) {
                            // Handle error (e.g., network issue, permission denied)
                            Toast.makeText(MakePurcharses.this, "Budget update failed!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Update UI with new budget and display success message
                        initialBudget = newBudget;
                        budget.setText("Ksh " + initialBudget);


                        // Clear or reset UI elements as needed
                    }
                });
                pgb.setVisibility(View.VISIBLE);
                uploadImageFirebase(imguri);


            }

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            // Process image selection result (e.g., set image URI to button)
            imguri = data.getData();
            if (imguri != null) {
                Log.d(TAG, "Image URI retrieved: " + imguri.toString());
                imageButton.setImageURI(imguri);
            } else {
                Log.e(TAG, "Error retrieving image URI from gallery result");
            }
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

    private void uploadImageFirebase(Uri contentUri) {
        StorageReference image = storageReference.child(" image feed/" + System.currentTimeMillis() + "." + getFileExt(contentUri));
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(MakePurcharses.this, "Image is uploaded", Toast.LENGTH_SHORT).show();
                        String feedWeight = itemWe.getText().toString().trim() + "kg";
                        String feedName = spinner1.getSelectedItem().toString().trim();
                        String date = dateEditText.getText().toString().trim();
                        String totalPriceStr = tot.getText().toString().trim();


                        InventoryDetails inventoryDetails = new InventoryDetails(uri.toString(), feedName, feedWeight, date);
                        String key = databaseReference.push().getKey();
                        databaseReference.child("Inventory Details").child(key).setValue(inventoryDetails);


                        Toast.makeText(MakePurcharses.this, "Purchase successful!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(MakePurcharses.this, "Purchase added to Inventory", Toast.LENGTH_SHORT).show();


                        PurchaseDetails purchaseDetails = new PurchaseDetails(totalPriceStr, feedName, feedWeight, date);
                        String key2 = databaseReference.push().getKey();
                        databaseReference.child("Budget Details").child("Purchase History").child(key2).setValue(purchaseDetails);
                        Toast.makeText(MakePurcharses.this, "Purchase Details sent to the manager", Toast.LENGTH_SHORT).show();
                        itemWe.setText("");
                        dateEditText.setText("");
                        tot.setText("****");
                        imageButton.setImageURI(null);
                        profdes.setVisibility(View.VISIBLE);
                        pgb.setVisibility(View.GONE);
                        purchase.setEnabled(false);
                        err.setVisibility(View.VISIBLE);


                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pgb.setVisibility(View.GONE);
                Toast.makeText(MakePurcharses.this, "Upload Failed", Toast.LENGTH_SHORT).show();

            }
        });


    }
    private String getFileExt(Uri contentUri) {
        //allow all support types of gallery
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(c.getType(contentUri));
    }
}