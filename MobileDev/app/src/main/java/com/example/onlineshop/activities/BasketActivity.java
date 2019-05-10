package com.example.onlineshop.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.onlineshop.R;
import com.example.onlineshop.adapters.ProductsAdapter;
import com.example.onlineshop.db.AppDatabase;
import com.example.onlineshop.db.entities.Product;
import com.example.onlineshop.util.SwipeToDeleteCallback;

import java.util.List;
import java.util.stream.Collectors;

public class BasketActivity extends AppCompatActivity {

    public static final int UPDATE_ITEMS = 2222;

    private ProductsAdapter mProductsAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_products);

        mRecyclerView = findViewById(R.id.RecyclerViewProducts);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        List<Product> products = AppDatabase.getAppDatabase(this)
                .productInBasketDao().getAll();
        mProductsAdapter = new ProductsAdapter(products, R.layout.item_product_list);
        mProductsAdapter.setClickable(false);

        mRecyclerView.setAdapter(mProductsAdapter);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.basket_name);
        }

        enableSwipeToDeleteAndUndo();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_basket, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_clear_basket:
                clearBasket();
                return true;

            case R.id.app_bar_basket_buy:
                List<Product> products = AppDatabase.getAppDatabase(this).productDao().getAll();
                List<Product> basketProducts = AppDatabase.getAppDatabase(this)
                        .productInBasketDao().getAll();

                products = products.stream()
                        .filter(prod -> basketProducts.stream()
                                .anyMatch(bProd -> prod.id == bProd.id)
                        )
                        .collect(Collectors.toList());

                for(Product p:products){
                    for (Product bp:basketProducts){
                        if(p.id == bp.id){
                            p.amount = p.amount - bp.amount;
                            if (p.amount < 0) {
                                p.amount = 0;
                            }
                        }
                    }
                }

                for(Product p:products){
                    AppDatabase.getAppDatabase(this).productDao().update(p);
                }

                clearBasket();

                setResult(Activity.RESULT_OK);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearBasket(){
        AppDatabase.getAppDatabase(this).productInBasketDao().deleteAll();
        mProductsAdapter.getProducts().clear();
        mProductsAdapter.notifyDataSetChanged();
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final Product item = mProductsAdapter.getProducts().get(position);

                mProductsAdapter.removeItem(position);


                Snackbar snackbar = Snackbar.make(mRecyclerView,
                        "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", view -> {
                    mProductsAdapter.restoreItem(item, position);
                    mRecyclerView.scrollToPosition(position);
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(mRecyclerView);
    }
}
