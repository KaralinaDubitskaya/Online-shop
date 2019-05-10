package com.example.onlineshop.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.sqlite.SQLiteConstraintException;

import com.example.onlineshop.db.entities.Product;
import com.example.onlineshop.db.entities.ProductInBasket;

import java.util.List;

@Dao
public interface ProductInBasketDao {

    @Query("SELECT * FROM productinbasket WHERE id=:id")
    ProductInBasket getById(int id);

    @Query("SELECT * FROM product JOIN productinbasket ON productinbasket.id = product.id")
    List<Product> getAll();

    @Query("SELECT * FROM productinbasket")
    List<ProductInBasket> getAllProductsInBasket();

    @Delete
    void delete(ProductInBasket productInBasket);

    @Query("DELETE FROM productinbasket")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(ProductInBasket productInBasket);

    @Update(onConflict = OnConflictStrategy.FAIL)
    void update(ProductInBasket productInBasket);

    // The data may or may not exist in the database.
    default void upsert(ProductInBasket productInBasket) {
        try {
            insert(productInBasket);
        } catch (SQLiteConstraintException exception) {
            update(productInBasket);
        }
    }
}
