package com.example.onlineshop.db.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Product {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo
    public String title;

    @ColumnInfo
    public String producer;

    @ColumnInfo
    public double price;

    @ColumnInfo
    public int amount;

    @ColumnInfo
    public String image_uri;

    @ColumnInfo
    public String description;
}
