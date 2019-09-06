package com.example.fireapp;


import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DownloadingFBData implements RecyclerProductAdapter.EmailItemClicked {
    private DatabaseReference reff;
    private RecyclerView ProductsRecycler;
    private ArrayList<Product> myProducts = new ArrayList<>();
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
        reff = FirebaseDatabase.getInstance().getReference().child(child);//работаем с классом в бд, которые мы получили из параметров
        final ArrayList<Product> prlist = this.myProducts;//this - важный момент, мы используем глобальную переменную для её дальнейшего использования. Создаём промежуточную переменную
        reff.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Product product = ds.getValue(Product.class);
                    prlist.add(product);//в цыкле в промеж.переменную кидаем продукты, которые авоматически переводяться в глобальную переменную в строке 32
                }
                setRecycler(myProducts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void setRecycler(ArrayList<Product> pr) {
        if (iter == 1) {
            reff = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference().child(randForBasket);//работаем с объектами в корзине в бд
            RecyclerProductAdapter recyclerProductAdapter = new RecyclerProductAdapter(pr, this, reff, 1);
            ProductsRecycler.setAdapter(recyclerProductAdapter);
        } else if (iter == 2) {
            reff = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference().child(randForBasket);
            RecyclerProductAdapter recyclerProductAdapter = new RecyclerProductAdapter(pr, this, reff, 2);
            ProductsRecycler.setAdapter(recyclerProductAdapter);
        }
    }

    @Override
    public void itemClickedCallback(int itemPosition) {

    }

}
