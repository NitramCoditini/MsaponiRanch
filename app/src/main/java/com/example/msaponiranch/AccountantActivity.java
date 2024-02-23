package com.example.msaponiranch;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msaponiranch.AccountantStuff.MakePurcharses;
import com.example.msaponiranch.AccountantStuff.SellItems;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class AccountantActivity extends AppCompatActivity {

    TextView tvName,tvPhone;
    FirebaseFirestore fStore;
    FirebaseStorage storage;

    FirebaseAuth firebaseAuth;
    StorageReference storageReference;

    RadioButton Rb1,Rb2,Rb3;
    Button reset1 , back1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountant);

        ImageView image = findViewById(R.id.accountantImage);
        tvName = findViewById(R.id.accountantName);
        tvPhone = findViewById(R.id.accountantPhone);
        reset1 = findViewById(R.id.accReset1);
        back1 = findViewById(R.id.accbackBtn1);
        Rb1 =  findViewById(R.id.accradioButton1);
        Rb2=  findViewById(R.id.accradioButton2);
        Rb3=  findViewById(R.id.accradioButton3);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fStore.collection("Msaponi ranch workers").document(firebaseAuth.getCurrentUser().getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Get the data from the document
                Map<String, Object> data = documentSnapshot.getData();
                String name = data.get("Full name").toString();
                String phone = data.get("Phone number").toString();
                String imageString = data.get("Profile pic").toString();

                Picasso.get().load(imageString).into(image);



                tvName.setText(name);

                tvPhone.setText(phone);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: ");
            }
        });

        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AccountantActivity.this, SelectClass.class);
                startActivity(i);
            }
        });

        reset1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Rb1.isChecked()){
                    Rb1.setChecked(false);
                    Rb2.setEnabled(true);
                    Rb3.setEnabled(true);
                }
                if(Rb2.isChecked()){
                    Rb2.setChecked(false);
                    Rb1.setEnabled(true);
                    Rb3.setEnabled(true);
                }
                if(Rb3.isChecked()){
                    Rb3.setChecked(false);
                    Rb2.setEnabled(true);
                    Rb1.setEnabled(true);
                }


            }
        });

        Rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Rb1.isChecked()) {
                    Rb2.setEnabled(false);
                    Rb3.setEnabled(false);
                    Intent i = new Intent(AccountantActivity.this, MakePurcharses.class);
                    startActivity(i);
                }

            }
        });
        Rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Rb2.isChecked()) {
                    Rb1.setEnabled(false);
                    Rb3.setEnabled(false);
                    Intent i = new Intent(AccountantActivity.this, SellItems.class);
                    startActivity(i);
                }
            }
        });

        Rb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Rb3.isChecked()) {
                    Rb2.setEnabled(false);
                    Rb1.setEnabled(false);
                    Toast.makeText(AccountantActivity.this, "Currently Unavailable", Toast.LENGTH_SHORT).show();
                }

            }


        });



    }
    @Override
    protected void onRestart() {
        super.onRestart();
        // Uncheck all radio buttons
        Rb1.setChecked(false);
        Rb2.setChecked(false);
        Rb3.setChecked(false);
        // Enable all radio buttons
        Rb1.setEnabled(true);
        Rb2.setEnabled(true);
        Rb3.setEnabled(true);
    }
}