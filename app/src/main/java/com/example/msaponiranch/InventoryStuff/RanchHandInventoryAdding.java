package com.example.msaponiranch.InventoryStuff;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.msaponiranch.AccountantStuff.InventoryDetails;
import com.example.msaponiranch.AccountantStuff.MakePurcharses;
import com.example.msaponiranch.AccountantStuff.PurchaseDetails;
import com.example.msaponiranch.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RanchHandInventoryAdding extends AppCompatActivity {

    public static final int GALLERY_REQUEST_CODE = 105;
    public static final String TAG = "imageInfo";

    Spinner spinner1;

    Button adding;
    EditText itemWe, dateEditText;
    TextView profdes;
    ImageView datePickerIcon;
    Uri imguri;
    ProgressBar pgb;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    Calendar calendar;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranch_hand_inventory_adding);

        imageButton = findViewById(R.id.fieldimage);
        spinner1 = findViewById(R.id.fieldfeed);
        adding = findViewById(R.id.fieldTot);
        itemWe = findViewById(R.id.fieldItemWeight);
        profdes = findViewById(R.id.fieldInfo);
        dateEditText = findViewById(R.id.dateEditText2);
        datePickerIcon = findViewById(R.id.datePickerIcon2);
        pgb = findViewById(R.id.itemProgressBar);


        calendar = Calendar.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Field_Feed_available, android.R.layout.simple_spinner_item);
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

        adding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = dateEditText.getText().toString().trim();
                Drawable drawable = imageButton.getDrawable();
                String weightStr = itemWe.getText().toString().trim();

                if (TextUtils.isEmpty(weightStr)) {
                    itemWe.setError("size is required!");
                    return;
                }

                if (TextUtils.isEmpty(date)) {
                    dateEditText.setError("Date is required!");
                    return;
                }



                if (drawable == null) {
                    Toast.makeText(RanchHandInventoryAdding.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    return;
                }
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
        StorageReference image = storageReference.child(" image inventory/" + System.currentTimeMillis() + "." + getFileExt(contentUri));
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(RanchHandInventoryAdding.this, "Image is uploaded", Toast.LENGTH_SHORT).show();
                        String feedWeight = itemWe.getText().toString().trim() + "kg";
                        String feedName = spinner1.getSelectedItem().toString().trim();
                        String date = dateEditText.getText().toString().trim();



                        InventoryDetails inventoryDetails = new InventoryDetails(uri.toString(), feedName, feedWeight, date);
                        String key = databaseReference.push().getKey();
                        databaseReference.child("Inventory Details").child(key).setValue(inventoryDetails);



                        Toast.makeText(RanchHandInventoryAdding.this, "Feed added to Inventory", Toast.LENGTH_SHORT).show();



                        itemWe.setText("");
                        dateEditText.setText("");
                        imageButton.setImageURI(null);
                        profdes.setVisibility(View.VISIBLE);
                        pgb.setVisibility(View.GONE);



                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pgb.setVisibility(View.GONE);
                Toast.makeText(RanchHandInventoryAdding.this, "Upload Failed", Toast.LENGTH_SHORT).show();

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