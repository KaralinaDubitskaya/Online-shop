package com.example.onlineshop.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.onlineshop.R;
import com.example.onlineshop.db.Keys;
import com.squareup.picasso.Picasso;

public class AddProductActivity extends AppCompatActivity {

    public static final int ADD_PRODUCT_REQUEST_CODE = 123;
    private static final int GET_PHOTO_REQUEST_CODE = 200;

    private ImageView mImageView;
    private EditText mEditTitle;
    private EditText mEditPrice;
    private EditText mEditProducer;
    private EditText mEditDescription;
    private EditText mEditAmount;
    private Button mCreateButton;

    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        mImageView = findViewById(R.id.ImageView);
        mImageView.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, GET_PHOTO_REQUEST_CODE);
        });

        mEditTitle = findViewById(R.id.EditTitle);
        mEditDescription = findViewById(R.id.EditDescription);
        mEditAmount = findViewById(R.id.EditAmount);
        mEditPrice = findViewById(R.id.EditPrice);
        mEditProducer = findViewById(R.id.EditProducer);

        mCreateButton = findViewById(R.id.ButtonAdd);
        mCreateButton.setOnClickListener(v -> {
            Intent i = getIntent();
            String title = mEditTitle.getText().toString();
            String description = mEditDescription.getText().toString();
            String strAmount = mEditAmount.getText().toString();
            String producer = mEditProducer.getText().toString();
            String strPrice = mEditPrice.getText().toString();

            if (title.isEmpty() || description.isEmpty() || strAmount.isEmpty()
                    || producer.isEmpty() || strPrice.isEmpty()){
                Snackbar.make(v, getString(R.string.not_full_info), Snackbar.LENGTH_LONG).show();
            } else if (mImageUri == null) {
                Snackbar.make(v, getString(R.string.empty_image), Snackbar.LENGTH_LONG).show();
            } else {
                int amount = Integer.parseInt(strAmount);
                double price = Double.parseDouble(strPrice);
                i.putExtra(Keys.TITLE, title);
                i.putExtra(Keys.AMOUNT, amount);
                i.putExtra(Keys.DESCRIPTION, description);
                i.putExtra(Keys.PRODUCER, producer);
                i.putExtra(Keys.PRICE, price);
                i.putExtra(Keys.IMAGE_URI, mImageUri.toString());
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) return;

        if (requestCode == GET_PHOTO_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            int width = mImageView.getWidth();
            int height = mImageView.getHeight();
            Picasso.get()
                    .load(imageUri)
                    .resize(width, height)
                    .centerCrop()
                    .placeholder(R.drawable.gradient_shape)
                    .error(R.drawable.gradient_shape)
                    .into(mImageView);
            mImageUri = imageUri;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
