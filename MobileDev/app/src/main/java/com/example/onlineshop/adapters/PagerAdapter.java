package com.example.onlineshop.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.onlineshop.R;
import com.example.onlineshop.fragments.ProductsFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return ProductsFragment.newInstance(ProductsFragment.RV_LIST);
            case 1:
                return  ProductsFragment.newInstance(ProductsFragment.RV_GRID);
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return mContext.getString(R.string.rows);

        } else {
            return mContext.getString(R.string.columns);
        }
    }
}
