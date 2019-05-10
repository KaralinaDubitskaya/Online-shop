package com.example.onlineshop.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.onlineshop.activities.ProductDetailsActivity;
import com.example.onlineshop.db.Keys;
import com.example.onlineshop.db.entities.Product;
import com.example.onlineshop.viewholders.ProductViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductViewHolder> implements Filterable {

    private List<Product> mProducts;
    private List<Product> mProductsFiltered;
    private int mLayoutId;

    private boolean mIsClickable = true;

    public ProductsAdapter(List<Product> products, int layout_id) {
        mProducts =  products;
        mProductsFiltered = products;
        mLayoutId = layout_id;
    }

    public List<Product> getProducts() {
        return mProducts;
    }

    public void setProducts(List<Product> products) {
        mProducts = products;
        mProductsFiltered = products;
    }

    public void removeItem(int position) {
        mProducts.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Product item, int position) {
        mProducts.add(position, item);
        notifyItemInserted(position);
    }

    public void setClickable(boolean isClickable) {
        mIsClickable = isClickable;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(viewGroup.getContext())
                .inflate(mLayoutId, viewGroup,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i) {
        final Product product = mProductsFiltered.get(i);
        productViewHolder.bind(product);
        productViewHolder.itemView.setOnClickListener(view -> {
            if (mIsClickable){
                Intent intent = new Intent(view.getContext(), ProductDetailsActivity.class);
                intent.putExtra(Keys.ID, product.id);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()  {
        return mProductsFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence searchCharSequence) {
                String charString = searchCharSequence.toString().toLowerCase();
                if (charString.length() < 3) {
                    mProductsFiltered = mProducts;
                } else {
                    List<Product> filteredList = new ArrayList<>();
                    for (Product product : mProducts) {
                        if (product.title.toLowerCase().contains(charString)
                                || product.description.toLowerCase().contains(charString)
                                || product.producer.toLowerCase().contains(charString)){
                            filteredList.add(product);
                        }
                    }

                    mProductsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mProductsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mProductsFiltered = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
