package com.bagusc.myshoes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Catalog extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private static final int PRODUCT_DETAIL_REQUEST = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        recyclerView = findViewById(R.id.recyclerView);
        int numberOfColumns = 2; // Jumlah kolom pada grid
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh data setiap kali kembali ke halaman
        loadData();
    }

    private void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://restful-api-myshoes.vercel.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<Product[]> call = service.getProducts();
        call.enqueue(new Callback<Product[]>() {
            @Override
            public void onResponse(Call<Product[]> call, Response<Product[]> response) {
                if (response.isSuccessful()) {
                    Product[] productList = response.body();
                    adapter = new ProductAdapter(productList, new ProductAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Product product) {
                            displayProductDetail(product);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(Catalog.this, "Gagal mengambil data produk", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product[]> call, Throwable t) {
                Toast.makeText(Catalog.this, "Kesalahan jaringan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProductDetail(Product product) {
        Intent intent = new Intent(Catalog.this, ProductDetailActivity.class);
        intent.putExtra("PRODUCT", product);
        startActivityForResult(intent, PRODUCT_DETAIL_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PRODUCT_DETAIL_REQUEST && resultCode == RESULT_OK) {
            // Produk berhasil diubah atau ada aksi lain yang memerlukan penyegaran data
            // Lakukan penyegaran data di sini, misalnya, panggil metode loadData()
            loadData();
        }
    }
}
