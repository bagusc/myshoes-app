package com.bagusc.myshoes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
// Import yang benar
import com.bagusc.myshoes.ProductAdapter.OnItemClickListener;


import com.squareup.picasso.Picasso;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Product[] productList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public ProductAdapter(Product[] productList) {
        this.productList = productList;
    }

    public ProductAdapter(Product[] productList, OnItemClickListener onItemClickListener) {
        this.productList = productList;
        this.onItemClickListener = onItemClickListener;
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
        holder.productBrandTextView.setText(product.getBrand());
        holder.productPriceTextView.setText(String.valueOf(product.getPrice()));
        holder.productDescTextView.setText(product.getDescription());
        holder.productStockTextView.setText(String.valueOf(product.getStock()));
        holder.productRatingTextView.setText(String.valueOf(product.getRating()));
        Picasso.get().load(product.getImage()).into(holder.productImageView);

        // Menambahkan OnClickListener pada gambar
        holder.productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Panggil listener untuk menangani klik item
                onItemClickListener.onItemClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView;
        TextView productBrandTextView;
        TextView productPriceTextView;
        TextView productDescTextView;
        TextView productStockTextView;
        TextView productRatingTextView;
        ImageView productImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productBrandTextView = itemView.findViewById(R.id.productBrandTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            productDescTextView = itemView.findViewById(R.id.productDescTextView);
            productStockTextView = itemView.findViewById(R.id.productStockTextView);
            productRatingTextView = itemView.findViewById(R.id.productRatingTextView);
            productImageView = itemView.findViewById(R.id.productImageView);

            // Menambahkan OnClickListener pada gambar
            productImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Panggil metode untuk menampilkan detail produk
                    showProductDetail(productList[getAdapterPosition()].getId(), itemView.getContext());
                }
            });
        }
    }

    // Metode untuk menampilkan detail produk
    private void showProductDetail(String productId, Context context) {
        // Implementasikan logika tampilan detail produk di sini.
        // Anda dapat menggunakan Intent untuk membuka layar detail baru atau menampilkan dialog, dll.
        // Contoh: Intent untuk membuka layar detail produk
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra("PRODUCT_ID", productId);

        context.startActivity(intent);
    }
}
