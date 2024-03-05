package com.example.msaponiranch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msaponiranch.ManagerStuff.CattleHealthRecord;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CattleAdding extends AppCompatActivity {

    public static final String TAG = "TAG";
    public static final int GALLERY_REQUEST_CODE = 105;

    Spinner spinner1,spinner2;
    ImageButton imageButton;
    TextView profdes, assign;
    ProgressBar pgb;
    Button update;
    Uri imguri;
    EditText cownm,cowbr,cowag,cowWe;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cattle_adding);

        imageButton = findViewById(R.id.cowimage);
        cownm = findViewById(R.id.cowName);
        cowbr = findViewById(R.id.cowBreed);
        cowag  = findViewById(R.id.cowAge);
        spinner2 = findViewById(R.id.ageunit);
        cowWe = findViewById(R.id.cowWeight);
        assign = findViewById(R.id.Cowhealthsche);
        pgb = findViewById(R.id.cowRegProgressBar);
        update = findViewById(R.id.cowReg);
        profdes = findViewById(R.id.cowinfo);
        spinner1 = findViewById(R.id.gender);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Gender_available, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner1.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.age_available, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner2.setAdapter(adapter1);
        
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference("CattleDetails");

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profdes.setVisibility(View.GONE);
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                gallery.setType("image/*");
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CattleAdding.this, CattleHealthRecord.class);
                startActivity(i);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cowname = cownm.getText().toString().trim();
                String cowbreed = cowbr.getText().toString().trim();
                String cowage = cowag.getText().toString().trim();
                String cowweight = cowWe.getText().toString().trim();
                Drawable drawable = imageButton.getDrawable();




                if (drawable == null) {
                    Toast.makeText(CattleAdding.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(cowname)) {
                    cownm.setError("Cattle name is required!");
                    return;
                }

                if (TextUtils.isEmpty(cowbreed)) {
                    cowbr.setError("Cattle breed is required!");
                    return;
                }
                if (TextUtils.isEmpty(cowage)) {
                    cowag.setError("Cattle age is required!");
                    return;
                }
                if (TextUtils.isEmpty(cowweight)) {
                    cowbr.setError("Cattle weight is required!");
                    return;
                }

                uploadImageFirebase(imguri);





            }
        });

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

    private void uploadImageFirebase(Uri contentUri) {
        StorageReference image = storageReference.child(" image cows/" + System.currentTimeMillis() + "." + getFileExt(contentUri));
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(CattleAdding.this, "Image is uploaded", Toast.LENGTH_SHORT).show();
                        String cowname = cownm.getText().toString().trim();
                        String cowbreed = cowbr.getText().toString().trim();
                        String cowage = cowag.getText().toString().trim();
                        String age = spinner2.getSelectedItem().toString().trim();
                        String ageWithUnit = cowage + " " + age;
                        String cowweight = cowWe.getText().toString().trim() + " kg";
                        String gender = spinner1.getSelectedItem().toString().trim();



                        CattleDetail cattleDetail = new CattleDetail(uri.toString(),cowname,gender,cowbreed,ageWithUnit,cowweight);
                        String imageId = databaseReference.push().getKey();
                        databaseReference.child(imageId).setValue(cattleDetail);
                        Toast.makeText(CattleAdding.this, "Data inserted", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onSuccess: Uploaded Image url is: " + uri.toString());
                        cownm.setText("");
                        imageButton.setImageURI(null);
                        profdes.setVisibility(View.VISIBLE);
                        cowbr.setText("");
                        cowag.setText("");
                        cowWe.setText("");
                        pgb.setVisibility(View.GONE);
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pgb.setVisibility(View.GONE);
                Toast.makeText(CattleAdding.this, "Upload Failed", Toast.LENGTH_SHORT).show();

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