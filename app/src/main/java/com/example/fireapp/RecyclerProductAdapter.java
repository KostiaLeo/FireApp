package com.example.fireapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class RecyclerProductAdapter extends RecyclerView.Adapter<RecyclerProductAdapter.ViewHolder> {
    private List<Product> ProductList;
    private EmailItemClicked callback;
   // private DatabaseReference reff;
    private int maxId = 0, iteratorforadapter;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    //---------------------------------------------------------------------------------------
//------------Стрёмная херь, которая чё-то там, куда-то там адаптирует ----------------------
//----лаадно, это класс, отвечающий за работу над елементами ресайклера ---------------------

    public RecyclerProductAdapter(List<Product> productList, EmailItemClicked callback, int iteratorforadapter) {//, DatabaseReference reff
        this.ProductList = productList;
        this.callback = callback;
     //   this.reff = reff;
        this.iteratorforadapter = iteratorforadapter;
    }

    @NonNull
    @Override
    public RecyclerProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION && callback != null) {
                    callback.itemClickedCallback(adapterPosition);
                }
            }
        });
        return holder;
    }

//-------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        if (ProductList == null) {
            return 0;
        }
        return ProductList.size();
    }

//----------метод для заполнения каждого елемента списка -----------------------------------------------------
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Product product = ProductList.get(position);
        holder.nameTv.setText(product.getName());
        holder.titleTv.setText(product.getDesc());
        holder.textTv.setText(String.valueOf(product.getPrice()));
        holder.timeTv.setText("00:00");
        if (iteratorforadapter == 1) {
            holder.add.setText("+add");
        } else if (iteratorforadapter == 2) {
            holder.add.setText("-delete");
        }

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iteratorforadapter == 1) {
                    addToBasket(product);
                } else if (iteratorforadapter == 2) {
                    removeAt(ProductList, position);
                }//если задействовано главное меню, то задаём кнопке добавление при нажатии, если же корзина, то удаляем + редактируем надпись на кнопке
            }
        });
    }

    private void addToBasket(Product product) {
//        firestore.collection("Basket").document().addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                if (documentSnapshot.exists()) maxId = documentSnapshot.getData().size();
//                System.out.println(documentSnapshot.getData().size());
//            }
//        });
        CollectionReference reference = firestore.collection("Basket");
        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                System.out.println(queryDocumentSnapshots.size());
                maxId = queryDocumentSnapshots.size();
            }
        });

        firestore.collection("Basket").document(String.valueOf(maxId++)).set(product);
        }
//        reff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if ((dataSnapshot.exists())) {
//                    maxId = (int) dataSnapshot.getChildrenCount();//цыкл создан для вычисления ID продукта для дальнейшей его идентификации и использования
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {}
//        });
//        reff.child(String.valueOf(maxId++)).setValue(product);//по ID задаём продукт в корзину

    private void removeAt(List<Product> productList, int position) {
        productList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productList.size()); //предыдущие 3 строки отвечают за удаление продукта именно с UI (из рес.вью) + анимацию удаления
        CollectionReference reference = firestore.collection("Basket");
        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                maxId = queryDocumentSnapshots.size();
            }
        });
        firestore.collection("Basket").document(String.valueOf(maxId++)).delete();
//        reff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if ((dataSnapshot.exists())) {
//                    maxId = (int) dataSnapshot.getChildrenCount();
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {}
//        });
       // reff.child(String.valueOf(maxId++)).removeValue();//а теперь удаление из бд, то есть окончательное удаление из корзины
    }

    interface EmailItemClicked {
        void itemClickedCallback(int itemPosition);
    }

//--------- метод, соединяющий UI c елементами ресайклера --------------------
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv, titleTv, textTv, timeTv;
        Button add;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.name);
            titleTv = itemView.findViewById(R.id.title);
            textTv = itemView.findViewById(R.id.text);
            timeTv = itemView.findViewById(R.id.time);
            add = itemView.findViewById(R.id.add);
        }
    }


    public void clear() {
     //   DatabaseReference refff = FirebaseDatabase.getInstance(FirebaseApp.getInstance()).getReference().child("BasketProd");
     //   refff.removeValue();
    }
}
