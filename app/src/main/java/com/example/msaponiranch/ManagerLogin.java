package com.example.msaponiranch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ManagerLogin extends AppCompatActivity {

    EditText mEmail, mPassword;
    Button mlog;
    FirebaseAuth firebaseAuth;
    ProgressBar pgb2;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login);


        mEmail = findViewById(R.id.managerLoginEmail);
        mPassword = findViewById(R.id.managerLoginPassword);
        mlog = findViewById(R.id.managerLoginButton);
        pgb2 = findViewById(R.id.managerProgressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();


        mlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memail = mEmail.getText().toString().trim();
                String mpassword = mPassword.getText().toString();

                if (TextUtils.isEmpty(mpassword)) {
                    mPassword.setError("Registered Password is required!");
                    return;
                }


                if (TextUtils.isEmpty(memail)) {
                    mEmail.setError("Register email is required!");
                    return;
                }
                pgb2.setVisibility(View.VISIBLE);


                firebaseAuth.signInWithEmailAndPassword(memail, mpassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (mpassword.equals("Password123?")){

                            AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(ManagerLogin.this);
                            confirmationDialog.setTitle("Reset Password?");
                            confirmationDialog.setMessage("Are you sure you want to reset your password?");
                            confirmationDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    firebaseAuth.sendPasswordResetEmail(memail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ManagerLogin.this, "Reset link has been sent", Toast.LENGTH_SHORT).show();
                                            pgb2.setVisibility(View.GONE);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ManagerLogin.this, "Error reset link is not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            });
                            confirmationDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // User chose not to reset the password, handle accordingly
                                }
                            });
                            confirmationDialog.show();



                        }else{
                            if (memail.equals("martin.muiga47@gmail.com")) {

                                Toast.makeText(ManagerLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ManagerLogin.this, ManagerActivity.class));
                                pgb2.setVisibility(View.GONE);
                                finish();


                            } else {

                                Toast.makeText(ManagerLogin.this, "Invalid manager email!", Toast.LENGTH_SHORT).show();
                                pgb2.setVisibility(View.GONE);
                            }
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pgb2.setVisibility(View.GONE);
                        Toast.makeText(ManagerLogin.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ManagerLogin.this, "Ensure email and password are correct", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ManagerLogin.this, "Ensure you are connected to the internet", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}