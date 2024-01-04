package com.bagusc.myshoes;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    @GET("products")
    Call<Product[]> getProducts();  // Mengubah dari List<Product> menjadi Product[]

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") String productId);

    @DELETE("products/{productId}")
    Call<Void> deleteProduct(@Path("productId") String productId);

    @PUT("products/{id}")
    Call<Product> updateProduct(@Path("id") String productId, @Body Product updatedProduct);



    @Multipart
    @POST("api/products")  // Sesuaikan dengan endpoint API yang benar
    Call<Void> uploadProduct(
            @Part MultipartBody.Part image,
            @Part("name") RequestBody name,
            @Part("brand") RequestBody brand,
            @Part("price") RequestBody price,
            @Part("description") RequestBody description,
            @Part("stock") RequestBody stock,
            @Part("rating") RequestBody rating
    );}
