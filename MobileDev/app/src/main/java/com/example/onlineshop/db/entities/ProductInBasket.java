package com.example.onlineshop.db.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.example.onlineshop.db.entities.Product;

@Entity(foreignKeys = @ForeignKey(entity = Product.class,
        parentColumns = "id",
        childColumns = "id",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE))

public class ProductInBasket {
    @PrimaryKey
    public int id;

    @ColumnInfo
    public int amount;

    // TODO: is it necessary?
    public ProductInBasket() {}

    public ProductInBasket(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }
}
