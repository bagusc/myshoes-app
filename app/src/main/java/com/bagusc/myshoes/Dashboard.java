package com.bagusc.myshoes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class Dashboard extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private TextView textViewTotalData;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Button add = findViewById(R.id.add);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aksi yang akan dijalankan saat tombol diklik
                Intent intent = new Intent(Dashboard.this, AddProduct.class);
                startActivity(intent);
            }
        });

        Button all = findViewById(R.id.all);


        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aksi yang akan dijalankan saat tombol diklik
                Intent intent = new Intent(Dashboard.this, Catalog.class);
                startActivity(intent);
            }
        });


        // Mendapatkan referensi TextView dari XML
        textViewTotalData = findViewById(R.id.textViewTotalData);

        // Memulai AsyncTask untuk mengambil data dari API (Anda mungkin ingin menggunakan AsyncTask atau thread terpisah lainnya)
        new GetDataFromApi().execute();

}

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh data setiap kali kembali ke halaman
        new GetDataFromApi().execute();
    }

    private class GetDataFromApi extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                // Gantilah URL_API dengan URL yang sesuai
                String urlApi = "https://restful-api-myshoes.vercel.app/api/products/";
                URL url = new URL(urlApi);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;

                    while ((inputLine = reader.readLine()) != null) {
                        response.append(inputLine);
                    }
                    reader.close();

                    // Parsing respons JSON
                    JSONArray jsonArray = new JSONArray(response.toString());

                    // Mengembalikan panjang array
                    return jsonArray.length();
                } else {
                    Log.e("GetDataFromApi", "Gagal mengambil data. Kode respons: " + responseCode);
                    return -1; // Nilai yang menunjukkan kegagalan
                }

            } catch (Exception e) {
                Log.e("GetDataFromApi", "Error: " + e.toString());
                return -1; // Nilai yang menunjukkan kegagalan
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result != -1) {
                // Mengupdate TextView dengan panjang array
                textViewTotalData.setText("" + result);
            } else {
                // Menampilkan pesan kesalahan jika gagal
                textViewTotalData.setText("Gagal mengambil data dari API");
            }
        }
    }
}