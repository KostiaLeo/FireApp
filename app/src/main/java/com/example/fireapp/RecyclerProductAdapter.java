package com.example.fireapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerProductAdapter extends RecyclerView.Adapter<RecyclerProductAdapter.ViewHolder>  {
    private List<Product> ProductList;
    private EmailItemClicked callback;
    private ArrayList<Product> basket = new ArrayList<>();
    //---------------------------------------------------------------------------------------


    public RecyclerProductAdapter(List<Product> productList, EmailItemClicked callback) {
        ProductList = productList;
        this.callback = callback;
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

//------------------------------------------------------------------------------------------------------------

    @Override
    public void onBindViewHolder(RecyclerProductAdapter.ViewHolder holder, final int position) {
        final Product product = ProductList.get(position);
        holder.nameTv.setText(product.getName());
        holder.titleTv.setText(product.getDesc());
        holder.textTv.setText(product.getPrice());
        holder.timeTv.setText("00:00");
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basket.add(product);
                System.out.println(basket.size());
            }
        });
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

    public ArrayList<Product> getBasket() {
        return basket;
    }
}
