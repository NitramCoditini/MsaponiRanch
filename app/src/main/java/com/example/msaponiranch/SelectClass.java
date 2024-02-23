package com.example.msaponiranch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class SelectClass extends AppCompatActivity {

    Button reset1 , back1;
    RadioButton Rb1,Rb2,Rb3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_class);

        reset1 = findViewById(R.id.Reset1);
        back1 = findViewById(R.id.backBtn1);
        Rb1 =  findViewById(R.id.radioButton1);
        Rb2=  findViewById(R.id.radioButton2);
        Rb3=  findViewById(R.id.radioButton3);

        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectClass.this, MainActivity.class);
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
                    Intent i = new Intent(SelectClass.this, RanchHandLogin.class);
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
                    Intent i = new Intent(SelectClass.this, AccountantLogin.class);
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
                    Intent i = new Intent(SelectClass.this, ManagerLogin.class);
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
        // Enable all radio buttons
        Rb1.setEnabled(true);
        Rb2.setEnabled(true);
        Rb3.setEnabled(true);
    }
}