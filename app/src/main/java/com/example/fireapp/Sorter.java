package com.example.fireapp;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

//------- здрасте, добро пожаловать в класс-сортировщик ------------------

public class Sorter implements RecyclerProductAdapter.EmailItemClicked {
    private DatabaseReference reference;
    private String sortByIt, nameOfBasket = "BasketProd";
    private int iteratorForDescAsc, minValue, maxValue;
    private ArrayList<Product> mySortedProducts = new ArrayList<>();
    private RecyclerView productsRecycler;
    private Query q;

    public Sorter(RecyclerView productsRecycler, String sortByIt) {
        this.productsRecycler = productsRecycler;
        this.sortByIt = sortByIt;
    }

// пожалуй метод, ради которого создавался класс ----------------------------------------------
// запускаем запрос, который подгружает данные из бд, -----------------------------------------
// при необходимости их сразу сортирует по показателю, выбранному юзером ----------------------

    public void sortProds() {
        reference = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference().child("Product");
        final ArrayList<Product> prlist = this.mySortedProducts;

        if (!(sortByIt.equals("none filters"))) {
            this.q = reference.orderByChild(sortByIt);
        } else {
            this.q = reference;
        }
//------------ перебираем все елементы и, если они удовлетворяют условиям фильтра, закидываем в список -----------
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Product product = ds.getValue(Product.class);
                    if (product.getPrice() <= maxValue && product.getPrice() >= minValue) {
                        prlist.add(product);
                    }
                }

                if (iteratorForDescAsc == 1) {
                    Collections.reverse(mySortedProducts);
                } else if (iteratorForDescAsc == 0) {
                    System.out.println();
                }
                setAdapter(mySortedProducts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

//------------- закидываем отсортированные данные в адаптер -> ресайклер ----------------------
    private void setAdapter(ArrayList<Product> mySortedProducts) {
        reference = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference().child(nameOfBasket);//работаем с объектами в корзине в бд
        RecyclerProductAdapter recyclerProductAdapter = new RecyclerProductAdapter(mySortedProducts, this, reference, 1);
        productsRecycler.setAdapter(recyclerProductAdapter);
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }

    public String getSortByIt() {
        return sortByIt;
    }

    public void setSortByIt(String sortByIt) {
        this.sortByIt = sortByIt;
    }

    public int getIteratorForDescAsc() {
        return iteratorForDescAsc;
    }

    public void setIteratorForDescAsc(int iteratorForDescAsc) {
        this.iteratorForDescAsc = iteratorForDescAsc;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public void itemClickedCallback(int itemPosition) {

    }
}