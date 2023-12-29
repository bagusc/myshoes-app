package com.bagusc.myshoes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Product[] productList;


    public ProductAdapter(Product[] productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList[position];

        holder.productNameTextView.setText(product.getName());
//        holder.productBrandTextView.setText(product.getBrand());
        holder.productPriceTextView.setText(String.valueOf(product.getPrice()));
        holder.productDescTextView.setText(product.getDescription());
        holder.productStockTextView.setText(String.valueOf(product.getStock()));
        holder.productRatingTextView.setText(String.valueOf(product.getRating()));
        Picasso.get().load(product.getImage()).into(holder.productImageView);


    }

    @Override
    public int getItemCount() {
        return productList.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productNameTextView ;
        TextView productBrandTextView;
        TextView productPriceTextView;
        TextView productDescTextView ;
        TextView productStockTextView ;
        TextView productRatingTextView ;
        ImageView productImageView ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productNameTextView = itemView.findViewById(R.id.productNameTextView);
//            productBrandTextView = itemView.findViewById(R.id.productBrandTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            productDescTextView = itemView.findViewById(R.id.productDescTextView);
            productStockTextView = itemView.findViewById(R.id.productStockTextView);
            productRatingTextView = itemView.findViewById(R.id.productRatingTextView);
            productImageView = itemView.findViewById(R.id.productImageView);

        }
    }
}