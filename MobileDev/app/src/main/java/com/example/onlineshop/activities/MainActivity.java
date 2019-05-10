package com.example.onlineshop.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.onlineshop.R;
import com.example.onlineshop.adapters.PagerAdapter;
import com.example.onlineshop.db.AppDatabase;
import com.example.onlineshop.db.Keys;
import com.example.onlineshop.db.entities.Product;
import com.example.onlineshop.util.Singleton;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;
    private ViewPager mViewPager;

    private PagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = findViewById(R.id.ViewPager);

        mAdapter = new PagerAdapter(getSupportFragmentManager(),this);
        mViewPager.setAdapter(mAdapter);

        TabLayout tabLayout = findViewById(R.id.TabLayout);
        tabLayout.setupWithViewPager(mViewPager);

        mSharedPreferences = getSharedPreferences("shared_pref", 0);
        mViewPager.setCurrentItem(mSharedPreferences.getInt("display_type",0));

        Singleton.MainActivity = this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.AppMenuSearch:
                return true;
            case R.id.AppMenuAdd:
                Intent i = new Intent(this, AddProductActivity.class);
                startActivityForResult(i, AddProductActivity.ADD_PRODUCT_REQUEST_CODE);
                return true;
            case R.id.AppMenuBasket:
                Intent basketIntent = new Intent(this, BasketActivity.class);
                startActivityForResult(basketIntent,BasketActivity.UPDATE_ITEMS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AddProductActivity.ADD_PRODUCT_REQUEST_CODE
                && resultCode == Activity.RESULT_OK && data != null){

            Bundle bundle = data.getExtras();
            Product product = new Product();
            try {
                product.title = bundle.getString(Keys.TITLE);
                product.producer = bundle.getString(Keys.PRODUCER);
                product.description = bundle.getString(Keys.DESCRIPTION);
                product.amount = bundle.getInt(Keys.AMOUNT,0);
                product.price = bundle.getDouble(Keys.PRICE,0);
                product.image_uri = bundle.getString(Keys.IMAGE_URI);
                AppDatabase.getAppDatabase(this).productDao().insertAll(product);
            } catch (NullPointerException e){
                Snackbar.make(mViewPager,getString(R.string.error_message),Snackbar.LENGTH_LONG).show();
            }
        }

        if (resultCode == Activity.RESULT_OK){
            this.recreate();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save type of layout.
        mSharedPreferences.edit().putInt("display_type", mViewPager.getCurrentItem()).apply();
    }
}
