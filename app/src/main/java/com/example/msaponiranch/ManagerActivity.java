package com.example.msaponiranch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class ManagerActivity extends AppCompatActivity {

    Button reset1 , back1;
    RadioButton Rb1,Rb2, Rb3,Rb4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        reset1 = findViewById(R.id.Resetman);
        back1 = findViewById(R.id.backman);
        Rb1 =  findViewById(R.id.radioButton4);
        Rb2=  findViewById(R.id.radioButton5);
        Rb3 =  findViewById(R.id.radioButton6);
        Rb4=  findViewById(R.id.radioButton7);

        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManagerActivity.this, ManagerLogin.class);
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
                    Rb4.setEnabled(true);

                }
                if(Rb2.isChecked()){
                    Rb2.setChecked(false);
                    Rb1.setEnabled(true);
                    Rb3.setEnabled(true);
                    Rb4.setEnabled(true);

                }
                if(Rb3.isChecked()){
                    Rb3.setChecked(false);
                    Rb1.setEnabled(true);
                    Rb2.setEnabled(true);
                    Rb4.setEnabled(true);

                }
                if(Rb4.isChecked()){
                    Rb4.setChecked(false);
                    Rb1.setEnabled(true);
                    Rb2.setEnabled(true);
                    Rb3.setEnabled(true);

                }


            }
        });

        Rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Rb1.isChecked()) {
                    Rb2.setEnabled(false);
                    Rb3.setEnabled(false);
                    Rb4.setEnabled(false);
                    Intent i = new Intent(ManagerActivity.this, RanchActivity.class);
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
                    Rb4.setEnabled(false);
                    Intent i = new Intent(ManagerActivity.this, RegisteringActivity.class);
                    startActivity(i);
                }
            }
        });
        Rb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Rb3.isChecked()) {
                    Rb1.setEnabled(false);
                    Rb2.setEnabled(false);
                    Rb4.setEnabled(false);
                    Intent i = new Intent(ManagerActivity.this, AccountantTransactionActivity.class);
                    startActivity(i);
                }
            }
        });
        Rb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Rb4.isChecked()) {
                    Rb1.setEnabled(false);
                    Rb2.setEnabled(false);
                    Rb3.setEnabled(false);
                    Intent i = new Intent(ManagerActivity.this, CattleAdding.class);
                    startActivity(i);
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
        Rb4.setChecked(false);


        // Enable all radio buttons
        Rb1.setEnabled(true);
        Rb2.setEnabled(true);
        Rb3.setEnabled(true);
        Rb4.setEnabled(true);
    }
}