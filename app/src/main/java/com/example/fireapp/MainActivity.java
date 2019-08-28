package com.example.fireapp;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {
    private DatabaseReference reff;
    private ListView userList;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        FirebaseApp.initializeApp(this);
        reff = FirebaseDatabase.getInstance().getReference().child("User");
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> myUsers = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    myUsers.add(user);
                }
                ArrayAdapter<User> adapter = new UserAdapter(MainActivity.this, myUsers);
                userList = findViewById(R.id.mainList);
                userList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        add = findViewById(R.id.goToAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }
}


//        reff = FirebaseDatabase.getInstance().getReference().child("User").child("0");
//        reff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String nick = dataSnapshot.child("nick").getValue().toString();
//                String mail = dataSnapshot.child("mail").getValue().toString();
//                String pwd = dataSnapshot.child("pwd").getValue().toString();
//                System.out.println("Key: " + dataSnapshot.getKey() + "\n"
//                        + "nick: " + nick + "\n"
//                        + "mail: " + mail + "\n"
//                        + "pwd: " + pwd);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }

//        reff = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference().child("User");
//        reff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()) {
//                    maxId = (int) dataSnapshot.getChildrenCount();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        nick_inp = findViewById(R.id.nick);
//        mail_inp = findViewById(R.id.mail);
//        pwd_inp = findViewById(R.id.pwd);
//        ins_btn = findViewById(R.id.btn_ins);
//
//        ins_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                user = new User(nick_inp.getText().toString(), mail_inp.getText().toString(), pwd_inp.getText().toString());
//                //reff.push().setValue(user);//random configuration for username in db
//                reff.child(String.valueOf(maxId++)).setValue(user);
//            }
//        });
