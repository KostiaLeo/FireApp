package com.example.fireapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddActivity extends AppCompatActivity {
    private EditText nick_inp, mail_inp, pwd_inp;
    private Button ins_btn, goToShow;
    private DatabaseReference reff;
    private Product product;
    private int maxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        reff = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference().child("Product");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists())) {
                    maxId = (int) dataSnapshot.getChildrenCount();
                } else {
                    Toast.makeText(AddActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        nick_inp = findViewById(R.id.name);
        mail_inp = findViewById(R.id.desc);
        pwd_inp = findViewById(R.id.price);
        ins_btn = findViewById(R.id.btn_ins);

        ins_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product = new Product();
                product.setName(nick_inp.getText().toString());
                product.setDesc(mail_inp.getText().toString());
                product.setPrice(pwd_inp.getText().toString());
                reff.child(String.valueOf(maxId++)).setValue(product);
            }
        });
        goToShow = findViewById(R.id.goToShow);
        goToShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
