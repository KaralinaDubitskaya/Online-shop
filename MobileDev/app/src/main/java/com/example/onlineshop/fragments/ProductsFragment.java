package com.example.onlineshop.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.onlineshop.R;
import com.example.onlineshop.activities.AddProductActivity;
import com.example.onlineshop.activities.BasketActivity;
import com.example.onlineshop.adapters.ProductsAdapter;
import com.example.onlineshop.db.AppDatabase;
import com.example.onlineshop.db.entities.Product;

import java.util.List;

public class ProductsFragment extends Fragment {
    // Name of the argument to set style of the fragment.
    private static final String RV_LAYOUT = "layout";

    public static final int RV_LIST = 1;
    public static final int RV_GRID = 2;
    public static final int GRID_SPAN_COUNT = 2;

    private RecyclerView mRecyclerView;
    private int mCurrentLayoutForItems;
    private ProductsAdapter mProductsAdapter;

    // Called to do initial creation of a fragment.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Report that this fragment would like to participate in populating the options menu
        //by receiving a call to onCreateOptionsMenu(Menu, MenuInflater) and related methods.
        setHasOptionsMenu(true);
    }

    //Called to have the fragment instantiate its user interface view.
    //This will be called between onCreate and onActivityCreated.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create View from XML file.
        // atachToRoot is false because it would be done later.
        View view = inflater.inflate(R.layout.fragment_products, container,false);

        int type = 0;
        if (getArguments() != null) {
            type = getArguments().getInt(RV_LAYOUT);
        }
        mRecyclerView = view.findViewById(R.id.RecyclerViewProducts);

        List<Product> products = AppDatabase.getAppDatabase(getContext()).productDao().getAll();
        if (type == RV_LIST){
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mCurrentLayoutForItems = R.layout.item_product_list;
            mProductsAdapter = new ProductsAdapter(products, mCurrentLayoutForItems);
        } else if (type == RV_GRID) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),GRID_SPAN_COUNT));
            mCurrentLayoutForItems = R.layout.item_product_grid;
            mProductsAdapter = new ProductsAdapter(products, mCurrentLayoutForItems);
        }

        mRecyclerView.setAdapter(mProductsAdapter);
        return view;
    }

    // Inflate the menu for this fragment.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu_app, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.AppMenuSearch)
                .getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                useFilter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                useFilter(s);
                return false;
            }

            private void useFilter(String s){
                mProductsAdapter.getFilter().filter(s);
            }
        });
    }

    public static ProductsFragment newInstance(final int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(RV_LAYOUT, type);

        ProductsFragment fragment = new ProductsFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (mProductsAdapter == null){
            mProductsAdapter = new ProductsAdapter(AppDatabase.getAppDatabase(
                    getContext()).productDao().getAll(), mCurrentLayoutForItems);
        }

        if (requestCode == AddProductActivity.ADD_PRODUCT_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            List<Product> products = AppDatabase.getAppDatabase(getContext()).productDao().getAll();
            mProductsAdapter.setProducts(products);
            mProductsAdapter.notifyItemInserted(mProductsAdapter.getItemCount());
        }

        if (requestCode == BasketActivity.UPDATE_ITEMS && resultCode == Activity.RESULT_OK){
            List<Product> products = AppDatabase.getAppDatabase(getContext()).productDao().getAll();
            mProductsAdapter.setProducts(products);
            mProductsAdapter.notifyDataSetChanged();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
