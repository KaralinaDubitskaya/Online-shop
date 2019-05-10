package com.example.onlineshop.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.onlineshop.db.dao.ProductDao;
import com.example.onlineshop.db.dao.ProductInBasketDao;
import com.example.onlineshop.db.entities.Product;
import com.example.onlineshop.db.entities.ProductInBasket;

@Database(entities = {Product.class, ProductInBasket.class},
        version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract ProductDao productDao();
    public abstract ProductInBasketDao productInBasketDao();

    public static AppDatabase getAppDatabase(Context context) {
        synchronized (AppDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE =
                        Room.databaseBuilder(context, AppDatabase.class, "shop-database")
                                .allowMainThreadQueries()
                                .build();
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}




