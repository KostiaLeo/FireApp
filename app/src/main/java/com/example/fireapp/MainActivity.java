package com.example.fireapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import java.util.Random;

public class MainActivity extends AppCompatActivity{
    private DatabaseReference reff;
    private Button toBasket;
    RecyclerProductAdapter recyclerProductAdapter;

    private DownloadingFBData downloadingFBData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        FirebaseApp.initializeApp(this);

        Random r = new Random();

        final String nameOfBasket = "BasketProd";

        toBasket = findViewById(R.id.toBasket);
        toBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BasketActivity.class);
                //intent.putExtra("nameOfB", nameOfBasket);
                startActivity(intent);
            }
        });
        RecyclerView ProductsRecycler = findViewById(R.id.products);

        ProductsRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        downloadingFBData = new DownloadingFBData(reff, ProductsRecycler, nameOfBasket, "Product", 1);

        downloadingFBData.setRecyclerView();
    }
}