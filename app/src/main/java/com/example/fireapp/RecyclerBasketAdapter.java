package com.example.fireapp;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecyclerBasketAdapter extends RecyclerView.Adapter<RecyclerBasketAdapter.ViewHolder>  {
    private List<Product> ProductList;
    private EmailItemClicked callback;
    private ArrayList<Product> basket = new ArrayList<>();
    private DatabaseReference ref;
    private int maxId;
    //---------------------------------------------------------------------------------------

    public RecyclerBasketAdapter(ArrayList<Product> productList, EmailItemClicked callback, DatabaseReference ref) {
        ProductList = productList;
        this.callback = callback;
        this.ref = ref;
    }

    @NonNull
    @Override
    public RecyclerBasketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

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

//------------------------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(RecyclerBasketAdapter.ViewHolder holder, final int position) {
        final Product product = ProductList.get(position);
        holder.nameTv.setText(product.getName());
        holder.titleTv.setText(product.getDesc());
        holder.textTv.setText(product.getPrice());
        holder.timeTv.setText("00:00");
        holder.add.setText("DELETE");
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                removeAt(ProductList, position, product);
            }
        });
    }

    private void removeAt(List<Product> productList, int position, Product production) {
        productList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productList.size());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists())) {
                    maxId = (int) dataSnapshot.getChildrenCount();
                } else {
                    //Toast.makeText(AddActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        ref.child(String.valueOf(maxId++)).removeValue();
    }

    interface EmailItemClicked {
        void itemClickedCallback(int itemPosition);
    }

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
}
