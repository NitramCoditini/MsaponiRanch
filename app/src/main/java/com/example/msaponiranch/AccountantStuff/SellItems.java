package com.example.msaponiranch.AccountantStuff;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msaponiranch.R;
import com.example.msaponiranch.livestockactivityclassess.MilkingDetail;
import com.example.msaponiranch.livestockactivityclassess.MilkingRecord;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SellItems extends AppCompatActivity {

    TextView budget,totalMilkSizeTextView,tvAMilk;
    EditText itemNa,itemWe, dateEditText,itemTo;

    ImageView datePickerIcon;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;


    Calendar calendar;

    Button purchase;

    ProgressBar pgb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_items);

        itemWe = findViewById(R.id.stItemWeight);
        itemNa = findViewById(R.id.stItemName);
        itemTo = findViewById(R.id.stItemTotal);
        purchase = findViewById(R.id.makePur);
        tvAMilk = findViewById(R.id.totalMilkSizeAfter);
        budget = findViewById(R.id.currentBudget);
        dateEditText = findViewById(R.id.dateEditText);
        datePickerIcon = findViewById(R.id.datePickerIcon);
        pgb = findViewById(R.id.itemProgressBar);

        calendar = Calendar.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference();

        datePickerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        databaseReference.child("Budget Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Float currentBudget = snapshot.child("amount").getValue(Float.class);

                    budget.setText("Ksh " + currentBudget);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database errors
            }
        });
        DatabaseReference milkRecordRef = FirebaseDatabase.getInstance().getReference("Milking Record");

        milkRecordRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalMilkSize = 0; // Changed from long to int

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String milkSize = ds.child("milkSize").getValue(String.class);
                    if (milkSize != null) {
                        // Assuming milkSize is in the format "XL" where X is the number of liters
                        // Extract the numeric part and convert it to int
                        int milkSizeInLiters = Integer.parseInt(milkSize.substring(0, milkSize.length() - 1));
                        totalMilkSize += milkSizeInLiters;
                    }
                }

                // Display the total milk size in your UI
                // For example, if you have a TextView to display the total
                totalMilkSizeTextView = findViewById(R.id.totalMilkSizeTextView);
                totalMilkSizeTextView.setText(String.valueOf(totalMilkSize) + "L");

                // Create and update the TotalMilkInventory field in your database
                DatabaseReference totalMilkInventoryRef = FirebaseDatabase.getInstance().getReference("Milking Record/TotalMilkInventory");
                totalMilkInventoryRef.setValue(totalMilkSize);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
        // Reference to the TotalMilkSizeAfterSelling in your Firebase Realtime Database
        DatabaseReference totalMilkSizeAfterSellingRef = FirebaseDatabase.getInstance().getReference("Milking Record/TotalMilkSizeAfterSelling");

        // Set up a ValueEventListener to listen for changes to TotalMilkSizeAfterSelling
        ValueEventListener totalMilkSizeListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if TotalMilkSizeAfterSelling exists
                if (snapshot.exists()) {
                    // Retrieve the new total milk size after selling
                    int totalNewMilkSize = snapshot.getValue(Integer.class);

                    // Update the TextView on the UI thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (tvAMilk != null) {
                                tvAMilk.setText(totalNewMilkSize + "L");
                            } else {
                                Log.e("TextView Update", "tvAMilk is null");
                            }
                        }
                    });
                } else {
                    // Handle the case where TotalMilkSizeAfterSelling does not exist
                    // This could be a place to initialize the field or handle the absence of data
                    Log.d("TotalMilkSizeAfterSelling", "Field does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Log.e("Firebase Read Error", "Failed to read total milk size after selling", error.toException());
            }
        };
        // Attach the ValueEventListener to the reference
        totalMilkSizeAfterSellingRef.addValueEventListener(totalMilkSizeListener);

        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String itemNm = itemNa.getText().toString().trim();
                String totalPriceStr = itemTo.getText().toString().trim();
                String date = dateEditText.getText().toString().trim();
                String curBudget = budget.getText().toString().trim();
                String weightStr = itemWe.getText().toString().trim();

                if (TextUtils.isEmpty(weightStr)) {
                    itemWe.setError("Quantity is required!");
                    return;
                }
                if (TextUtils.isEmpty(itemNm)) {
                    itemNa.setError("Item name is required!");
                    return;
                }

                if (TextUtils.isEmpty(date)) {
                    dateEditText.setError("Date is required!");
                    return;
                }

                if (TextUtils.isEmpty(totalPriceStr)) {
                    itemTo.setError("Total amount is required!");
                    return;
                }



                float totalPrice = Float.parseFloat(totalPriceStr);



                // Parse total price to float (handle potential errors)
                if (totalPrice <= 0) {
                    // totalPrice is less than or equal to 0
                    String message = "Invalid total price: must be positive";

                    Toast.makeText(SellItems.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                String budgetValueStr = curBudget.replace("Ksh ", ""); // Removes "Ksh " from the string
                float budgetValue = Float.parseFloat(budgetValueStr);

                final float newBudget = budgetValue + totalPrice;


                String afterSelling = tvAMilk.getText().toString().trim();
                // Check if the TextView is empty
                if (afterSelling.isEmpty()) {
                    // If empty, continue to perform the showReceiptDialog operation
                    pgb.setVisibility(View.VISIBLE);
                    showReceiptDialog(itemNm, weightStr, totalPriceStr, newBudget, date);
                } else if (afterSelling.equals("0L")) {
                    // If the TextView contains "0L", notify the user to ask the ranch hand to milk some cows
                    Toast.makeText(SellItems.this, "Please ask the ranch hand to milk some cows.", Toast.LENGTH_LONG).show();
                } else {
                    // If the TextView contains a value other than "0L", check if the quantity is greater than the available milk size
                    int quantityMilkInt = Integer.parseInt(weightStr.replace("L", ""));
                    int availableMilkSize = Integer.parseInt(afterSelling.replace("L", ""));

                    if (quantityMilkInt > availableMilkSize) {
                        // If the quantity is greater than the available milk size, notify the user
                        Toast.makeText(SellItems.this, "Quantity cannot be more than the available milk size.", Toast.LENGTH_LONG).show();
                    } else {
                        // If the quantity is less than or equal to the available milk size, proceed with the operation
                        // Your existing code to handle the operation
                        pgb.setVisibility(View.VISIBLE);
                        showReceiptDialog(itemNm, weightStr, totalPriceStr, newBudget, date);
                    }
                }











            }

        });




    }
    private void showReceiptDialog(String itemName, String quantity, String amount, Float newBudget, String date) {
        // Inflate the receipt layout
        LayoutInflater inflater = getLayoutInflater();
        View receiptView = inflater.inflate(R.layout.receiptlayout, null);

        // Populate the receipt layout with data
        TextView soldItemName = receiptView.findViewById(R.id.soldItemName);
        TextView soldItemQuantity = receiptView.findViewById(R.id.soldItemQuantity);
        TextView soldItemAmount = receiptView.findViewById(R.id.soldItemAmount);
        TextView receiptTotal = receiptView.findViewById(R.id.receiptTotal);
        TextView soldDate = receiptView.findViewById(R.id.soldDate);

        soldItemName.setText(itemName);
        soldItemQuantity.setText(quantity);
        soldItemAmount.setText(amount);
        receiptTotal.setText("Ksh " + amount); // Assuming amount is the total
        soldDate.setText(date);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(receiptView);

        // Set the "Cancel" button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Handle the Cancel button click
                pgb.setVisibility(GONE);
                dialog.dismiss();
            }
        });

        // Set the "Save" button
        builder.setPositiveButton("Save And Print", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Handle the Save button click
                // Implement your save logic here
                String quantityWithout = quantity.replace("L", "");
                int quantityMilkInt = Integer.parseInt(quantityWithout);
                Log.d("Quantity", "onClick: " + quantityMilkInt);


                // Reference to the Milking Record in your Firebase Realtime Database
                DatabaseReference milkRecordRef = FirebaseDatabase.getInstance().getReference("Milking Record");

                // Retrieve all cow milk records
                milkRecordRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int totalNewMilkSize = 0;
                        int cowsCount = 0; // Initialize cowsCount to 0

                        // Iterate over each child in the snapshot
                        for (DataSnapshot cowSnapshot : snapshot.getChildren()) {
                            // Check if the current child is not the TotalMilkInventory field and not the newly created TotalMilkSizeAfterSelling field
                            if (!cowSnapshot.getKey().equals("TotalMilkInventory") && !cowSnapshot.getKey().equals("TotalMilkSizeAfterSelling")) {
                                cowsCount++; // Increment cowsCount only if it's not the TotalMilkInventory or TotalMilkSizeAfterSelling
                            }
                        }
                        int milkPerCow = quantityMilkInt / cowsCount;

                        for (DataSnapshot cowSnapshot : snapshot.getChildren()) {
                            // Skip the TotalMilkInventory field and the newly created TotalMilkSizeAfterSelling field
                            if (cowSnapshot.getKey().equals("TotalMilkInventory") || cowSnapshot.getKey().equals("TotalMilkSizeAfterSelling")) {
                                continue;
                            }

                            // Use milkSizeAfterSelling if it exists, otherwise use milkSize
                            String milkSize = cowSnapshot.child("milkSizeAfterSelling").exists() ? cowSnapshot.child("milkSizeAfterSelling").getValue(String.class) : cowSnapshot.child("milkSize").getValue(String.class);
                            if (milkSize != null) {
                                // Extract the numeric part and convert it to int
                                int milkSizeInLiters = Integer.parseInt(milkSize.substring(0, milkSize.length() - 1));

                                // Calculate new milk size after selling
                                int newMilkSizeAfterSelling = milkSizeInLiters - milkPerCow;

                                // Update the cow's record with the new milk size after selling
                                cowSnapshot.getRef().child("milkSizeAfterSelling").setValue(newMilkSizeAfterSelling + "L");

                                // Add the new milk size to the total
                                totalNewMilkSize += newMilkSizeAfterSelling;
                            }
                        }

                        // Update the TotalMilkInventory with the new total milk size
                        DatabaseReference totalMilkInventoryRef = FirebaseDatabase.getInstance().getReference("Milking Record/TotalMilkSizeAfterSelling");
                        totalMilkInventoryRef.setValue(totalNewMilkSize, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error != null) {
                                    Toast.makeText(SellItems.this, "Failed to update total milk size", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SellItems.this, "Total milk size updated successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });

                // save to database
                String sdItemName  = itemNa.getText().toString();
                String sdItemQuantity = itemWe.getText().toString();
                String sdItemDate = dateEditText.getText().toString();
                String sdItemTotal = "Ksh " + itemTo.getText().toString();
                DatabaseReference sellingMadeRef = databaseReference.child("Sold Items Record");
                SoldItemDetail soldItemDetail = new SoldItemDetail(sdItemName,sdItemQuantity,sdItemDate,sdItemTotal);
                String sellingEntryKey = sellingMadeRef.push().getKey();
                sellingMadeRef.child(sellingEntryKey).setValue(soldItemDetail)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(SellItems.this, "Sold item details sent to the manager", Toast.LENGTH_SHORT).show();


                                PrintReceipt(newBudget,receiptView);




                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SellItems.this, "Failed to add sold items record, please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });


        // Show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void PrintReceipt(Float newBudget, View receiptView) {
        databaseReference.child("Budget Details").child("amount").setValue(newBudget, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    // Handle error (e.g., network issue, permission denied)
                    Toast.makeText(SellItems.this, "Budget update failed!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update UI with new budget and display success message

                budget.setText("Ksh " + newBudget);

                Bitmap bitmap = getBitmapFromView(receiptView);

                // Create a PDF from the bitmap

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    createPdfFromBitmap(bitmap, "receipt.pdf");

                } else {
                    Toast.makeText(SellItems.this, "PDF creation is not supported on this device.", Toast.LENGTH_SHORT).show();
                }





                // Clear or reset UI elements as needed
            }
        });

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
    private Bitmap getBitmapFromView(View view) {
        //conerting the layout to a bitmap then the bit map to be used for pdf
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    private void createPdfFromBitmap(Bitmap bitmap, String fileName) {
        if (bitmap == null) {
            Log.e("PDF Creation", "Bitmap is null");
            return;
        }

        try {
            // Create a new PdfDocument
            PdfDocument pdfDocument = new PdfDocument();

            // Create a new page description
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();

            // Start a page
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            // Draw the bitmap onto the page
            Canvas canvas = page.getCanvas();
            canvas.drawBitmap(bitmap, 0, 0, null);

            // Finish the page
            pdfDocument.finishPage(page);

            // Prepare to write the PDF document to a file
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
            if (uri != null) {
                try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                    pdfDocument.writeTo(outputStream);
                }
            }

            // Close the document
            pdfDocument.close();

            // Optionally, notify the user that the PDF has been saved
            Toast.makeText(this, "PDF saved to " + uri.getPath(), Toast.LENGTH_LONG).show();
            itemNa.setText("");
            itemWe.setText("");
            itemTo.setText("");
            dateEditText.setText("");
            pgb.setVisibility(GONE);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating PDF", Toast.LENGTH_SHORT).show();
        }
    }

}