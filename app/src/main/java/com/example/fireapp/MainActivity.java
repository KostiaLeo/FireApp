package com.example.fireapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements RecyclerProductAdapter.EmailItemClicked {
    private DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        FirebaseApp.initializeApp(this);
        reff = FirebaseDatabase.getInstance().getReference().child("Product");
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Product> myProducts = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Product Product = ds.getValue(Product.class);
                    myProducts.add(Product);
                }
                give(myProducts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void itemClickedCallback(int itemPosition) {

    }

    private void give(List<Product> myProducts) {
        RecyclerView ProductsRecycler = findViewById(R.id.products);
        ProductsRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        RecyclerProductAdapter recyclerProductAdapter = new RecyclerProductAdapter(myProducts, this);
        ProductsRecycler.setAdapter(recyclerProductAdapter);
    }
}