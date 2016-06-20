package com.example.shivam.smartprixdemo.activities;

import android.os.Bundle;

import com.example.shivam.smartprixdemo.R;
import com.example.shivam.smartprixdemo.fragments.CategoryFragment;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeToolbar(getString(R.string.app_name));
        addCategoryFragment();
    }

    private void addCategoryFragment() {
        CategoryFragment categoryFragment = CategoryFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.ll_fragment, categoryFragment, TAG)
                .commit();
    }
}
