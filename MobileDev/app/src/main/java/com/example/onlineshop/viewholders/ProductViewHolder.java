package com.example.onlineshop.viewholders;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlineshop.R;
import com.example.onlineshop.db.entities.Product;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    private ImageView mImageView;
    private TextView mTitle;
    private TextView mProducer;
    private TextView mDescription;
    private TextView mPrice;
    private TextView mAmount;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        mImageView = itemView.findViewById(R.id.ImageView);
        mTitle = itemView.findViewById(R.id.Title);
        mProducer = itemView.findViewById(R.id.Producer);
        mDescription = itemView.findViewById(R.id.Description);
        mPrice = itemView.findViewById(R.id.Price);
        mAmount = itemView.findViewById(R.id.Amount);
    }

    public void bind(Product product){
        final Uri imageUri = Uri.parse(product.image_uri);
        int width = Math.round(itemView.getContext().getResources().getDimension(R.dimen.product_image_width));
        int height = Math.round(itemView.getContext().getResources().getDimension(R.dimen.product_image_height));
        Picasso.get()
                .load(imageUri)
                .resize(width, height)
                .centerCrop()
                .placeholder(R.drawable.gradient_shape)
                .error(R.drawable.gradient_shape)
                .into(mImageView);

        mTitle.setText(product.title);
        mProducer.setText(product.producer);
        mDescription.setText(product.description);
        mPrice.setText(String.format(Locale.getDefault(),
                "%s %.2f", itemView.getResources().getString(R.string.price), product.price));
        mAmount.setText(String.format(Locale.getDefault(),
                "%s %d", itemView.getResources().getString(R.string.amount), product.amount));
    }
}
