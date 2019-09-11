package com.example.fireapp;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DownloadingFBData implements RecyclerProductAdapter.EmailItemClicked {
    //private DatabaseReference reff;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private RecyclerView ProductsRecycler;
    private ArrayList<Product> myProducts = new ArrayList<>(), prodsss = new ArrayList<>();
    private String randForBasket, child;
    private int iter;

    public DownloadingFBData(RecyclerView productsRecycler, String randForBasket, String child, int iter) {//DatabaseReference reff,
        ProductsRecycler = productsRecycler;
        this.randForBasket = randForBasket;
        this.child = child;
        this.iter = iter;
    }

    public void setRecyclerView() {
        final ArrayList<Product> prods = this.prodsss;
        firestore.collection("Products").document("Deserts").collection("Ice-creams").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Product product = new Product();
                        product.setName(String.valueOf(document.get("name")));
                        product.setDesc(String.valueOf(document.get("desc")));
                        product.setPrice(Integer.parseInt(String.valueOf(document.get("price"))));
                        product.setCollection(String.valueOf(document.get("collection")));
                        product.setCategory(String.valueOf(document.get("category")));
                        prods.add(product);
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
                System.out.println("Hey nameeee -> " + prodsss.get(prodsss.size() - 1).getName());
                setRecycler(prodsss);
            }
        });
//        reff = FirebaseDatabase.getInstance().getReference().child(child);//работаем с классом в бд, которые мы получили из параметров
//        final ArrayList<Product> prlist = this.myProducts;//this - важный момент, мы используем глобальную переменную для её дальнейшего использования. Создаём промежуточную переменную
//        reff.addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    Product product = ds.getValue(Product.class);
//                    prlist.add(product);//в цыкле в промеж.переменную кидаем продукты, которые авоматически переводяться в глобальную переменную в строке 32
//                }
//                setRecycler(myProducts);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });

    }

    private void setRecycler(ArrayList<Product> pr) {
        if (iter == 1) {
            //reff = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference().child(randForBasket);//работаем с объектами в корзине в бд
            RecyclerProductAdapter recyclerProductAdapter = new RecyclerProductAdapter(pr, this, 1);//, reff
            ProductsRecycler.setAdapter(recyclerProductAdapter);
        } else if (iter == 2) {
            //reff = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference().child(randForBasket);
            RecyclerProductAdapter recyclerProductAdapter = new RecyclerProductAdapter(pr, this, 2);//, reff
            ProductsRecycler.setAdapter(recyclerProductAdapter);
        }
    }

    @Override
    public void itemClickedCallback(int itemPosition) {

    }

}
