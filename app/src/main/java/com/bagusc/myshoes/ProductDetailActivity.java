package com.bagusc.myshoes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProductDetailActivity extends AppCompatActivity {
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Inisialisasi objek retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("https://restful-api-myshoes.vercel.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Product product = getIntent().getParcelableExtra("PRODUCT");

        TextView productNameTextView = findViewById(R.id.productNameTextView);
        productNameTextView.setText("Nama Produk: " + product.getName());

        TextView productBrandTextView = findViewById(R.id.productBrandTextView);
        productBrandTextView.setText("Merek: " + product.getBrand());

        TextView productPriceTextView = findViewById(R.id.productPriceTextView);
        productPriceTextView.setText("Harga: " + product.getPrice());

        TextView productDescTextView = findViewById(R.id.productDescTextView);
        productDescTextView.setText("Deskripsi: " + product.getDescription());

        TextView productStockTextView = findViewById(R.id.productStockTextView);
        productStockTextView.setText("Stok: " + product.getStock());

        TextView productRatingTextView = findViewById(R.id.productRatingTextView);
        productRatingTextView.setText("Rating: " + product.getRating());

        ImageView productImageView = findViewById(R.id.productImageView);
        Picasso.get().load(product.getImage()).into(productImageView);

        // Tombol Hapus
        Button deleteButton = findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementasikan logika penghapusan produk di sini
                // Misalnya, panggil metode untuk menghapus produk dari database atau sumber data lainnya
                deleteProduct(product);
            }
        });
    }


    private void deleteProduct(Product product) {
        ApiService service = retrofit.create(ApiService.class);
        Call<Void> call = service.deleteProduct(product.getId());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Produk berhasil dihapus, tambahkan logika sesuai kebutuhan
                    setResult(RESULT_OK);
                    Toast.makeText(ProductDetailActivity.this, "Berhasil menghapus produk", Toast.LENGTH_SHORT).show();
                    finish(); // Menutup activity setelah menghapus produk
                } else {
                    // Tampilkan pesan kesalahan jika diperlukan
                    Toast.makeText(ProductDetailActivity.this, "Gagal menghapus produk", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Tampilkan pesan kesalahan jika diperlukan
                Toast.makeText(ProductDetailActivity.this, "Kesalahan jaringan", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
