package com.bagusc.myshoes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

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

//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
//                    adapter = new ProductAdapter(productList);  // Menggunakan konstruktor yang menerima array
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
        // Implementasikan logika tampilan detail produk di sini.
        // Anda dapat menggunakan Intent untuk membuka layar detail baru atau menampilkan dialog, dll.
        // Contoh: Intent untuk membuka layar detail produk
        Intent intent = new Intent(Catalog.this, ProductDetailActivity.class);
//        intent.putExtra("PRODUCT_ID", product.getId());
        intent.putExtra("PRODUCT", product);
//        startActivity(intent);
        startActivityForResult(intent, PRODUCT_DETAIL_REQUEST);

    }
    // Metode ini dipanggil setelah ProductDetailActivity selesai
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PRODUCT_DETAIL_REQUEST && resultCode == RESULT_OK) {
            // Produk berhasil dihapus atau ada aksi lain yang memerlukan penyegaran data
            // Lakukan penyegaran data di sini, misalnya, panggil metode loadData()
            loadData();
        }
    }
}