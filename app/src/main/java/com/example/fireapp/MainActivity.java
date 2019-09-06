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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private String nameOfBasket = "BasketProd", sortByIt = "none filters";
    private RecyclerView ProductsRecycler;
    private Spinner spinner;
    private RecyclerProductAdapter recyclerProductAdapter;
    private EditText maxV, minV;
    private int max, min;
    private Sorter sorter;
    private CheckBox desc_asc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        maxV = findViewById(R.id.maxV);
        minV = findViewById(R.id.minV);

//----------- типа онКликЛистенеры и прочяя штука с нажималками --------------------------
        desc_asc = findViewById(R.id.desc_asc);
        desc_asc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                giveValueForSorting();
            }
        });

        goFByValue = findViewById(R.id.giveByValue);
        goFByValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giveValueForSorting();
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

//-------------- ну всё, приехали, начинаются адаптеры, ресайклеры и прочие страшные вещи -----------------
        ProductsRecycler = findViewById(R.id.products);
        ProductsRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        downloadingFBData = new DownloadingFBData(reff, ProductsRecycler, nameOfBasket, "Product", 1);
        downloadingFBData.setRecyclerView();
        //инициализируем объект, работающий с загрузкой данных из бд, и вызываем генерацию рес.вью + лайаут менеджер для ресайклера

        recyclerProductAdapter = new RecyclerProductAdapter(mySortedProducts, this, reff, 1);

//--------------- ъуъ, типа спинер, типа фильтрация пошла,определяем дропдаун штуку для выора фильтра -----
        String[] filterBy = {"none filter", "name", "desc", "price"};
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filterBy);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortByIt = String.valueOf(spinner.getSelectedItem());
                    giveValueForSorting();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    @Override
    public void itemClickedCallback(int itemPosition) {
    }
//--------------- метод, анализирующий данные для фильтрации из UI и передающий их в класс-сортировщик
    private void giveValueForSorting(){
        sorter = new Sorter(ProductsRecycler, sortByIt);
        if(maxV.getText().toString().length() == 0){
            max = Integer.MAX_VALUE;
        }else {
            max = Integer.parseInt(maxV.getText().toString());
        }
        System.out.println(maxV.getText().toString().length());
        System.out.println(max);

        if(minV.getText().toString().length() == 0){
            min = 0;
        }else {
            min = Integer.parseInt(minV.getText().toString());
        }
        //sorter.setMinValue(min);
        System.out.println(maxV.getText().toString().length());
        System.out.println(max);

        sorter.setSortByIt(sortByIt);

        if(desc_asc.isChecked()) {
            sorter.setIteratorForDescAsc(1);
        }else {
            sorter.setIteratorForDescAsc(0);
        }

        sorter.setMaxValue(max);
        sorter.setMinValue(min);

        sorter.sortProds();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerProductAdapter.clear();
    }
}