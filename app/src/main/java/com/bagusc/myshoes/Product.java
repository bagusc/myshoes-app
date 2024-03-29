package com.bagusc.myshoes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Product implements Parcelable {
    @SerializedName("_id")
    private String id;
    private String name;
    private String brand;
    private int price;
    private String description;
    private int stock;
    private double rating;
    private String image;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("updatedAt")
    private String updatedAt;

    // Constructor
    public Product(String id, String name, String brand, int price, String description, int stock, double rating, String image ) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.rating = rating;
        this.image = image;
    }

    // Getter dan Setter untuk id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter dan Setter untuk name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter dan Setter untuk brand
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    // Getter dan Setter untuk price
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    // Getter dan Setter untuk description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter dan Setter untuk stock
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    // Getter dan Setter untuk rating
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    // Getter dan Setter untuk image
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Implementasi Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(brand);
        dest.writeInt(price);
        dest.writeString(description);
        dest.writeInt(stock);
        dest.writeDouble(rating);
        dest.writeString(image);
        // Tuliskan atribut-atribut lainnya ke dalam parcel
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    private Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        brand = in.readString();
        price = in.readInt();
        description = in.readString();
        stock = in.readInt();
        rating = in.readDouble();
        image = in.readString();
        // Baca atribut-atribut lainnya dari parcel
    }
}