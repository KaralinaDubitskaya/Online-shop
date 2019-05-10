package com.example.onlineshop.activities;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.Display;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlineshop.R;
import com.example.onlineshop.db.AppDatabase;
import com.example.onlineshop.db.Keys;
import com.example.onlineshop.db.entities.Product;
import com.example.onlineshop.db.entities.ProductInBasket;
import com.example.onlineshop.filters.IntegerInputFilter;
import com.example.onlineshop.util.Singleton;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class ProductDetailsActivity extends AppCompatActivity {

    public static final int MAX_AMOUNT = 1000;

    private ImageView mImageView;
    private TextView mTitle;
    private TextView mProducer;
    private TextView mDescription;
    private TextView mPrice;
    private TextView mAmountTitle;
    private TextView mChosenAmountTitle;
    private EditText mChosenAmount;
    private Button mButtonAdd;
    private Button mButtonDelete;

    private Product mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        int id = getIntent().getIntExtra(Keys.ID, 1);
        mProduct = AppDatabase.getAppDatabase(this).productDao().getById(id);

        mImageView = findViewById(R.id.ImageView);

        mTitle = findViewById(R.id.Title);
        mTitle.setText(mProduct.title);

        mProducer = findViewById(R.id.Producer);
        mProducer.setText(mProduct.producer);

        mDescription = findViewById(R.id.Description);
        mDescription.setText(mProduct.description);

        mPrice = findViewById(R.id.Price);
        mPrice.setText(String.format(Locale.getDefault(),
                "%s %.2f", getString(R.string.price), mProduct.price));

        mAmountTitle = findViewById(R.id.AmountTitle);
        mAmountTitle.setText(String.format(Locale.getDefault(),
                "%s %d", getString(R.string.amount), mProduct.amount));

        mChosenAmountTitle = findViewById(R.id.ChosenAmountTitle);
        mChosenAmountTitle.setText(getString(R.string.chosen_amount));

        mChosenAmount = findViewById(R.id.ChosenAmount);
        mChosenAmount.setFilters(new InputFilter[]{ new IntegerInputFilter(0, MAX_AMOUNT)});

        mButtonAdd = findViewById(R.id.ButtonAdd);
        mButtonAdd.setOnClickListener(view -> {
            try
            {
                int chosenAmount = Integer.parseInt(mChosenAmount.getText().toString());
                if (chosenAmount < 1 || chosenAmount > MAX_AMOUNT)
                {
                    Snackbar.make(view, getString(R.string.add_no_products_in_basket),
                            Snackbar.LENGTH_SHORT).show();
                }
                else if (mProduct.amount < chosenAmount) {
                    Snackbar.make(view, String.format("Sorry, only %d item(s) are available.", mProduct.amount),
                            Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    ProductInBasket productInBasket = new ProductInBasket(mProduct.id, chosenAmount);
                    AppDatabase.getAppDatabase(view.getContext()).productInBasketDao().upsert(productInBasket);
                    Snackbar.make(view, getString(R.string.product_added_to_basket),
                            Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            }
            catch (Exception nfe)
            {
                Snackbar.make(view, getString(R.string.error_message),
                        Snackbar.LENGTH_SHORT).show();
            }
        });

        mButtonDelete = findViewById(R.id.ButtonDelete);
        mButtonDelete.setOnClickListener(view -> {
            try
            {
                AppDatabase db = AppDatabase.getAppDatabase(view.getContext());

                Product product = db.productDao().getById(id);
                db.productDao().delete(product);

                Snackbar.make(view, getString(R.string.product_deleted),
                        Snackbar.LENGTH_SHORT).show();

                Singleton.MainActivity.recreate();
                finish();
            }
            catch (Exception nfe)
            {
                Snackbar.make(view, getString(R.string.error_message),
                        Snackbar.LENGTH_SHORT).show();
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Picasso.get()
                .load(Uri.parse(mProduct.image_uri))
                .resize(width,
                        Math.round(getResources().getDimension(R.dimen.product_image_height)))
                .centerCrop()
                .placeholder(R.drawable.gradient_shape)
                .error(R.drawable.gradient_shape)
                .into(mImageView);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
