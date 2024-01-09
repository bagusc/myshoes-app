package com.bagusc.myshoes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProductActivity extends AppCompatActivity {

    private EditText editProductName, editProductBrand, editProductPrice, editProductDescription, editProductStock, editProductRating;
    private Button saveButton;

    private static final String BASE_URL = "https://restful-api-myshoes.vercel.app/api/";
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        editProductName = findViewById(R.id.editProductName);
        editProductBrand = findViewById(R.id.editProductBrand);
        editProductPrice = findViewById(R.id.editProductPrice);
        editProductDescription = findViewById(R.id.editProductDescription);
        editProductStock = findViewById(R.id.editProductStock);
        editProductRating = findViewById(R.id.editProductRating);
        saveButton = findViewById(R.id.saveButton);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Mendapatkan data produk dari intent
        Product product = getIntent().getParcelableExtra("PRODUCT");

        // Memasukkan data produk ke dalam elemen UI
        editProductName.setText(product.getName());
        editProductBrand.setText(product.getBrand());
        editProductPrice.setText(String.valueOf(product.getPrice()));
        editProductDescription.setText(product.getDescription());
        editProductStock.setText(String.valueOf(product.getStock()));
        editProductRating.setText(String.valueOf(product.getRating()));

        // Menangani klik tombol Simpan
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengambil data dari elemen UI
                String newName = editProductName.getText().toString();
                String newBrand = editProductBrand.getText().toString();
                int newPrice = Integer.parseInt(editProductPrice.getText().toString());
                String newDescription = editProductDescription.getText().toString();
                int newStock = Integer.parseInt(editProductStock.getText().toString());
                double newRating = Double.parseDouble(editProductRating.getText().toString());

                // Membuat objek Product baru dengan data yang diperbarui
                Product updatedProduct = new Product(
                        product.getId(),
                        newName,
                        newBrand,
                        newPrice,
                        newDescription,
                        newStock,
                        newRating,
                        product.getImage()
                );

                // Menjalankan metode untuk memperbarui produk
                updateProduct(updatedProduct);
            }
        });
    }

    private void updateProduct(Product updatedProduct) {
        ApiService service = retrofit.create(ApiService.class);
        Call<Product> call = service.updateProduct(updatedProduct.getId(), updatedProduct);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {

                    // Produk berhasil diperbarui, kirim kembali data yang diubah ke ProductDetailActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("EDITED_PRODUCT", updatedProduct);
                    setResult(RESULT_OK, resultIntent);
                    // Produk berhasil diperbarui, tambahkan logika sesuai kebutuhan
                    Toast.makeText(EditProductActivity.this, "Berhasil memperbarui produk", Toast.LENGTH_SHORT).show();
                    finish(); // Menutup activity setelah memperbarui produk
                } else {
                    // Tampilkan pesan kesalahan jika diperlukan
                    Toast.makeText(EditProductActivity.this, "Gagal memperbarui produk", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                // Tampilkan pesan kesalahan jika diperlukan
                Toast.makeText(EditProductActivity.this, "Kesalahan jaringan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
