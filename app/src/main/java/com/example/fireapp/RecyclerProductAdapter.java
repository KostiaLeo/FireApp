package com.example.fireapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RecyclerProductAdapter extends RecyclerView.Adapter<RecyclerProductAdapter.ViewHolder> {
    private List<Product> ProductList;
    private EmailItemClicked callback;
    private DatabaseReference reff;
    private int maxId, iteratorforadapter;


    //---------------------------------------------------------------------------------------

    public RecyclerProductAdapter(List<Product> productList, EmailItemClicked callback, DatabaseReference reff, int iteratorforadapter) {
        this.ProductList = productList;
        this.callback = callback;
        this.reff = reff;
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

//------------------------------------------------------------------------------------------------------------

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

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists())) {
                    maxId = (int) dataSnapshot.getChildrenCount();//цыкл создан для вычисления ID продукта для дальнейшей его идентификации и использования
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        reff.child(String.valueOf(maxId++)).setValue(product);//по ID задаём продукт в корзину
    }

    private void removeAt(List<Product> productList, int position) {
        productList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productList.size()); //предыдущие 3 строки отвечают за удаление продукта именно с UI (из рес.вью) + анимацию удаления
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists())) {
                    maxId = (int) dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        reff.child(String.valueOf(maxId++)).removeValue();//а теперь удаление из бд, то есть окончательное удаление из корзины
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


    public void clear() {
        int size = ProductList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                ProductList.remove(0);
            }
            System.out.println(ProductList.size());
            notifyItemRangeRemoved(0, size);

        }
    }
}
