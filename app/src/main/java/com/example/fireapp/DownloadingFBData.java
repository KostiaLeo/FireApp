package com.example.fireapp;

import android.database.CursorJoiner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Response;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class DownloadingFBData implements RecyclerProductAdapter.EmailItemClicked, RecyclerBasketAdapter.EmailItemClicked {
    private DatabaseReference reff;
    private RecyclerView ProductsRecycler;
    ArrayList<Product> myProducts = new ArrayList<>();
    private String randForBasket, child;
private int iter;

    public DownloadingFBData(DatabaseReference reff, RecyclerView productsRecycler, String randForBasket, String child, int iter) {
        this.reff = reff;
        ProductsRecycler = productsRecycler;
        this.randForBasket = randForBasket;
        this.child = child;
        this.iter = iter;
    }

    public void setRecyclerView() {
        reff = FirebaseDatabase.getInstance().getReference().child(child);
        final ArrayList<Product> prlist = this.myProducts;
        reff.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Product product = ds.getValue(Product.class);
                    prlist.add(product);
                }
                setRecycler(myProducts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void setRecycler(ArrayList<Product> pr) {
        if(iter == 1) {
            reff = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference().child("BasketProd");
            RecyclerProductAdapter recyclerProductAdapter = new RecyclerProductAdapter(pr, this, reff, randForBasket);
            ProductsRecycler.setAdapter(recyclerProductAdapter);
        } else if(iter == 2){
            RecyclerBasketAdapter recyclerProductAdapter = new RecyclerBasketAdapter(pr, this, reff);
            ProductsRecycler.setAdapter(recyclerProductAdapter);
        }
    }

    @Override
    public void itemClickedCallback(int itemPosition) {

    }
//
//    public ArrayList<Product> f(){
//        return myProducts;
//    }
}
