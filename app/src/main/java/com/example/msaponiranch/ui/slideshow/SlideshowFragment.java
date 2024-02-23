package com.example.msaponiranch.ui.slideshow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msaponiranch.InventoryStuff.Model3;
import com.example.msaponiranch.InventoryStuff.RecyclerAdapter3;
import com.example.msaponiranch.ManagerStuff.Model2;
import com.example.msaponiranch.ManagerStuff.RecyclerAdapter2;
import com.example.msaponiranch.R;
import com.example.msaponiranch.databinding.FragmentSlideshowBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    RecyclerView.Adapter recyclerAdapter;

    RecyclerView recyclerViewCategoryList3;

    ArrayList<Model3> modelArrayList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerViewCategoryList3 = root.findViewById(R.id.inventoryRecyclerView);
        recyclerViewCategoryList3.setLayoutManager(linearLayoutManager);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference();

        modelArrayList = new ArrayList<>();


        getDataFromFirebase();


        // Clear data for each RecyclerView separately
        clearAll();



        return root;
    }
    private void getDataFromFirebase() {

        Log.d("May", "Fetching data from Firebase");
        Query query = databaseReference.child("Inventory Details");
        Log.d("May", "Query created");
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                modelArrayList.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Log.d("May", "Listener added");

                    Model3 model = new Model3();

                    model.setDate(snapshot.child("date").getValue().toString());
                    model.setFeedName(snapshot.child("feedName").getValue().toString());
                    model.setFeedWeight(snapshot.child("feedWeight").getValue().toString());
                    model.setImageId(snapshot.child("imageId").getValue().toString());



                    modelArrayList.add(model);



                }
                Log.d("LIST_SIZE", "List size: " + modelArrayList.size());
                // After processing all the data, set the adapters to the RecyclerViews
                recyclerAdapter = new RecyclerAdapter3(getActivity(), modelArrayList);
                recyclerViewCategoryList3.setAdapter(recyclerAdapter);
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

}