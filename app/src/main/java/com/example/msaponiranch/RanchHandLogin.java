package com.example.msaponiranch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RanchHandLogin extends AppCompatActivity {

    TextView Resend, forget;
    EditText edEmail, edPassword;
    Button log;
    FirebaseAuth firebaseAuth;
    ProgressBar pgb1;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranch_hand_login);


        edEmail = findViewById(R.id.ranchLoginEmail);
        Resend = findViewById(R.id.ranchResend);
        forget = findViewById(R.id.ranchForgot);
        edPassword = findViewById(R.id.ranchLoginPassword);
        log = findViewById(R.id.ranchLoginButton);
        pgb1 = findViewById(R.id.ranchProgressBar);


        firebaseAuth = FirebaseAuth.getInstance();

        Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser fuser = firebaseAuth.getCurrentUser();
                fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(RanchHandLogin.this, "Verification code sent to " + fuser.getEmail(), Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RanchHandLogin.this, "Unable to send verification email", Toast.LENGTH_SHORT).show();


                    }
                });

            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordresetDialog = new AlertDialog.Builder(v.getContext());
                passwordresetDialog.setTitle("Reset Password");
                passwordresetDialog.setMessage("Enter your email to receive reset link");
                passwordresetDialog.setView(resetMail);

                passwordresetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail  = resetMail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(RanchHandLogin.this, "Reset link has been sent", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RanchHandLogin.this, "Error reset link is not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordresetDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordresetDialog.create().show();

            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = edEmail.getText().toString().trim();
                String mPassword = edPassword.getText().toString();

                if (TextUtils.isEmpty(mPassword)) {
                    edPassword.setError("Registered Password is required!");
                    return;
                }


                if (TextUtils.isEmpty(mEmail)) {
                    edEmail.setError("Register email is required!");
                    return;
                }
                pgb1.setVisibility(View.VISIBLE);


                firebaseAuth.signInWithEmailAndPassword(mEmail, mPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        if(firebaseAuth.getCurrentUser().isEmailVerified()){
                            if (!flag) {
                                flag = true;
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference docRef = db.collection("Msaponi ranch workers").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                String role = document.getString("Area of work");
                                                if ("Ranch Hand".equals(role)) {
                                                    Toast.makeText(RanchHandLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(RanchHandLogin.this, RanchHandActivity.class));
                                                    finish();
                                                } else if ("Accountant".equals(role)) {
                                                    Toast.makeText(RanchHandLogin.this, "An accountant cannot login here", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(RanchHandLogin.this, "No such document", Toast.LENGTH_SHORT).show();

                                            }
                                        } else {
                                            Toast.makeText(RanchHandLogin.this, "get failed with ", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                                pgb1.setVisibility(View.GONE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        flag = false;
                                    }
                                },500);

                            }


                        }else{
                            pgb1.setVisibility(View.GONE);
                            Toast.makeText(RanchHandLogin.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                            Toast.makeText(RanchHandLogin.this, "Verification code can be in spam if not use resend code", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pgb1.setVisibility(View.GONE);
                        Toast.makeText(RanchHandLogin.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        Toast.makeText(RanchHandLogin.this, "Ensure email and password are correct", Toast.LENGTH_SHORT).show();
                        Toast.makeText(RanchHandLogin.this, "Ensure you are connected to the internet", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}