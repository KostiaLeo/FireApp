package com.example.fireapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class BasketActivity extends AppCompatActivity implements RecyclerProductAdapter.EmailItemClicked, RecyclerBasketAdapter.EmailItemClicked {
    private Button goToMain;
    private DatabaseReference ref;
    private DownloadingFBData downloadingFBData;
    private RecyclerView basketProducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        FirebaseApp.initializeApp(this);
        goToMain = findViewById(R.id.toMain);
        basketProducts = findViewById(R.id.basketProducts);
        goToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(BasketActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });
        basketProducts.setLayoutManager(new LinearLayoutManager(BasketActivity.this));

        downloadingFBData = new DownloadingFBData(ref, basketProducts, "BasketProd", "BasketProd", 2);
        downloadingFBData.setRecyclerView();
    }

    @Override
    public void itemClickedCallback(int itemPosition) {

    }
}
