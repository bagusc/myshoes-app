package com.bagusc.myshoes;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddProduct extends AppCompatActivity {

    private EditText nameEditText, brandEditText, priceEditText, descriptionEditText, stockEditText, ratingEditText, imageEditText;
    private ImageView selectedImageView;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE_PERMISSION = 101;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        nameEditText = findViewById(R.id.nameEditText);
        brandEditText = findViewById(R.id.brandEditText);
        priceEditText = findViewById(R.id.priceEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        stockEditText = findViewById(R.id.stockEditText);
        ratingEditText = findViewById(R.id.ratingEditText);
        imageEditText = findViewById(R.id.imageEditText);
        selectedImageView = findViewById(R.id.selectedImageView);


        Button selectImageButton = findViewById(R.id.selectImageButton);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDataToApi();
            }
        });
    }

    private boolean validateInputs(String name, String brand, String price, String description, String stock, String rating, String image) {
        if (name.isEmpty() || brand.isEmpty() || price.isEmpty() || description.isEmpty() || stock.isEmpty() || rating.isEmpty() || image.isEmpty()) {
            // Tampilkan pesan kesalahan kepada pengguna
            Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void handleApiResponse(String result) {
        // Tambahkan logika penanganan hasil respon dari server di sini
        // Misalnya: Menampilkan pesan sukses atau melakukan tindakan tertentu
        Toast.makeText(AddProduct.this, "Input Success!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void handleApiError(String errorMessage) {
        // Tambahkan logika penanganan kesalahan di sini
        // Misalnya: Menampilkan pesan kesalahan kepada pengguna
        Toast.makeText(AddProduct.this, "Gagal mengirim data ke API: " + errorMessage, Toast.LENGTH_SHORT).show();
    }


    private void pickImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Jika izin belum diberikan, minta izin secara dinamis
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_PERMISSION);
        } else {
            // Jika izin sudah diberikan, lanjutkan dengan operasi pemilihan gambar
            performImagePick();
        }

    }

    private void performImagePick() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Jika izin diberikan, lanjutkan dengan operasi pemilihan gambar
                performImagePick();
            } else {
                // Jika izin ditolak, berikan informasi kepada pengguna atau lakukan tindakan lainnya
                Toast.makeText(this, "Izin akses penyimpanan ditolak.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("AddProduct", "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            Log.d("AddProduct", "onActivityResult: imageUri=" + imageUri);

            selectedImageView.setVisibility(View.VISIBLE);
            selectedImageView.setImageURI(imageUri);
            String imagePath = getImagePath(imageUri);
            imageEditText.setText(imagePath);
        }
    }
// ...

    private String getImagePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String imagePath = cursor.getString(column_index);
            cursor.close();
            return imagePath;
        }
        return null;
    }

    private void postDataToApi() {
        // Validasi input sebelum mengirim ke API
        String name = nameEditText.getText().toString();
        String brand = brandEditText.getText().toString();
        String price = priceEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String stock = stockEditText.getText().toString();
        String rating = ratingEditText.getText().toString();
        String imagePath = imageEditText.getText().toString();

        if (validateInputs(name, brand, price, description, stock, rating, imagePath)) {
            // Konversi path gambar menjadi File
            File imageFile = new File(imagePath);
            RequestBody imageRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);

            // MultipartBody.Part digunakan untuk mengirim file sebagai bagian dari permintaan multipart
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", "image.jpg", imageRequestBody);

            // Buat objek JSON
            RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody brandBody = RequestBody.create(MediaType.parse("text/plain"), brand);
            RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), price);
            RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), description);
            RequestBody stockBody = RequestBody.create(MediaType.parse("text/plain"), stock);
            RequestBody ratingBody = RequestBody.create(MediaType.parse("text/plain"), rating);

            // Kirim permintaan ke API menggunakan Retrofit
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://restful-api-myshoes.vercel.app/") // Sesuaikan dengan endpoint API yang benar
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService apiService = retrofit.create(ApiService.class);

            // Sesuaikan dengan endpoint API dan parameter yang diterima oleh API
            Call<Void> call = apiService.uploadProduct(imagePart, nameBody, brandBody, priceBody, descriptionBody, stockBody, ratingBody);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Proses hasil respon dari server
                        handleApiResponse(response.message());
                    } else {
                        // Tampilkan pesan kesalahan
                        handleApiError(response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                    // Tampilkan pesan kesalahan
                    handleApiError("Request failure: " + t.getMessage());
                }
            });
        }
    }
// ...
}
