package com.example.onlineshop.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.onlineshop.db.entities.Product;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM product WHERE id=:id")
    Product getById(int id);

    @Query("SELECT * FROM product")
    List<Product> getAll();

    @Update
    void update(Product product);

    @Insert
    void insertAll(Product... products);

    @Delete
    void delete(Product product);
}
