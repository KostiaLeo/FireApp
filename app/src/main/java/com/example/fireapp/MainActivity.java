package com.example.fireapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements RecyclerProductAdapter.EmailItemClicked {
    private DatabaseReference reff;
    private Button toBasket, goFByValue;
    private DownloadingFBData downloadingFBData;
    private ArrayList<Product> mySortedProducts = new ArrayList<>();
    private String nameOfBasket = "BasketProd";
    private RecyclerView ProductsRecycler;
    private Spinner spinner;
    private RecyclerProductAdapter recyclerProductAdapter;
    private EditText maxV, minV;
    int max, min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        maxV = findViewById(R.id.maxV);
        minV = findViewById(R.id.minV);

        goFByValue = findViewById(R.id.giveByValue);
        goFByValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                max = Integer.parseInt(maxV.getText().toString());
                min = Integer.parseInt(minV.getText().toString());
                sorting("none filter", 0, min, max);
            }
        });
        toBasket = findViewById(R.id.toBasket);
        toBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BasketActivity.class);
                startActivity(intent);
            }
        });
        ProductsRecycler = findViewById(R.id.products);

        ProductsRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        downloadingFBData = new DownloadingFBData(reff, ProductsRecycler, nameOfBasket, "Product", 1);
        downloadingFBData.setRecyclerView();
        //инициализируем объект, работающий с загрузкой данных из бд, и вызываем генерацию рес.вью + лайаут менеджер для ресайклера


//--------------------------------------------------------------------------------------------------------------------------------------------

//------------------------------------------------------ EXPERIMENTAL PART -------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------------------------------------------------

        recyclerProductAdapter = new RecyclerProductAdapter(mySortedProducts, this, reff, nameOfBasket, 1);

        String[] filterBy = {"none filter", "name", "desc", "price"};
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filterBy);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String filterator = String.valueOf(spinner.getSelectedItem());
                System.out.println("FILTERATOR -> " + filterator);
                //sorting("price", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
                //RecyclerProductAdapter adapterForDelete = new RecyclerProductAdapter();
                if (filterator.equals("none filter")) {
                    //System.out.println();
                    // recreate();
                } else {
                    recyclerProductAdapter.clear();
                    sorting(filterator, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
                }
                //sorting(filterator, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //sorting("price", 1, 90, 30);
//        reff = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference().child("Product");
//        reff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if ((dataSnapshot.exists())) {
//                    maxId = (int) dataSnapshot.getChildrenCount();//цыкл создан для вычисления ID продукта для дальнейшей его идентификации и использования
//                    //System.out.println(dataSnapshot.getChildrenCount());
//                } else {
//                    //Toast.makeText(AddActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//        for (int i = 8; i < 15; i++) {
//            Product product = new Product();
//            product.setName("burger" + i);
//            product.setDesc("desc" + i);
//            product.setPrice((int) (30 + Math.round(Math.random())));
//            reff.child(String.valueOf(i++)).setValue(product);
//        }
//        //reff.child(String.valueOf(maxId++)).setValue(product);
//
//
////        System.out.println("LOOK AT HERE->>>>>>> ");
//        Query q = reff.orderByChild("name");
//        final ArrayList<String> names = new ArrayList<>();
//        final ArrayList<String> prices = new ArrayList<>();
//        q.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    String name = data.child("name").getValue().toString();
//                    String price = data.child("price").getValue().toString();
//                    names.add(name);
//                    prices.add(price);
//                }
//                //Collections.reverse(prices);
//                //Collections.reverse(names);
//                for (int i = 0; i < prices.size(); i++) {
//                    System.out.println(prices.get(i) + " <-> " + names.get(i));
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        ref1 = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference().child("Product").child("burger");
//        ref1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                System.out.println("dataSnapshot.getValue() = " + dataSnapshot.getValue());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    private void sorting(String sortByIt, final int iteratorForDescAsc, final int minValue, final int maxValue) {
        reff = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference().child("Product");

        Query q = reff.orderByChild(sortByIt);
        final ArrayList<Product> prlist = this.mySortedProducts;

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
                } else {
                    System.out.println();
                }
                //ProductsRecycler.draw();
                setAdapter(mySortedProducts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setAdapter(ArrayList<Product> mySortedProducts) {
        reff = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference().child("BasketProd");//работаем с объектами в корзине в бд
        RecyclerProductAdapter recyclerProductAdapter = new RecyclerProductAdapter(mySortedProducts, this, reff, nameOfBasket, 1);
        ProductsRecycler.setAdapter(recyclerProductAdapter);
    }

    @Override
    public void itemClickedCallback(int itemPosition) {
    }
}