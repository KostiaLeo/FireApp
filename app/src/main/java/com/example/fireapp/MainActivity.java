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

public class MainActivity extends AppCompatActivity implements RecyclerProductAdapter.EmailItemClicked{
    private DatabaseReference reff;
    private Button addToBucket, toConsole;
    private ArrayList<Product> myProducts = new ArrayList<>();
    private ArrayList<Product> bucket = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        //Intent intent = new Intent(MainActivity.this, AddActivity.class);
        //startActivity(intent);
        FirebaseApp.initializeApp(this);
        addToBucket = findViewById(R.id.add);

        reff = FirebaseDatabase.getInstance().getReference().child("Product");
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Product product = ds.getValue(Product.class);
                    myProducts.add(product);
                }
                give(myProducts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //System.out.println("THIS IS BUCKETSSSSSSSSSSSSSSSSSSSS" + addToBucket);
    }

    private void addProductToBucket() {

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