package com.example.shivam.smartprixdemo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shivam.smartprixdemo.R;
import com.example.shivam.smartprixdemo.fragments.adapters.ProductAdapter;
import com.example.shivam.smartprixdemo.main.Constants;
import com.example.shivam.smartprixdemo.main.LogToast;
import com.example.shivam.smartprixdemo.main.MainApplication;
import com.example.shivam.smartprixdemo.network.data.ProductsData;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Shivam on 18-06-2016.
 */
public class ProductFragment extends Fragment {
    private static final String TAG = ProductFragment.class.getSimpleName();
    private static final String CATEGORY_NAME = "cat_name";
    private static final String CATEGORY = "category";
    private static final String SEARCH = "search";

    private ProductAdapter adapter;
    private RecyclerView rvProducts;
    private ProgressBar pbLoader;
    private LinearLayout llError;
    private TextView tvErrorMessage;
    private SwipeRefreshLayout srlRefresh;

    private CompositeSubscription compositeSubscription;

    public static ProductFragment newInstance(String category) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY_NAME, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.products_frag, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        compositeSubscription = new CompositeSubscription();
        llError = (LinearLayout) view.findViewById(R.id.ll_error_layout);
        rvProducts = (RecyclerView) view.findViewById(R.id.rv_products);
        pbLoader = (ProgressBar) view.findViewById(R.id.pb_loader);
        tvErrorMessage = (TextView) view.findViewById(R.id.tv_error_text);
        srlRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl_refresh);
        srlRefresh.setColorSchemeColors(getActivity().getResources().getColor(R.color.color_accent));
        srlRefresh.setOnRefreshListener(new RefreshData());
        adapter = new ProductAdapter(getActivity());
        rvProducts.setAdapter(adapter);
        rvProducts.setLayoutManager(new LinearLayoutManager(getActivity()));

        String category = getArguments().getString(CATEGORY_NAME);
        TextView tvCategory = (TextView) view.findViewById(R.id.tv_products_header);
        tvCategory.setText(category);

        getProductsFromServer();
    }

    private void getProductsFromServer() {
        Map<String, String> query = new HashMap<>();
        query.put(Constants.TYPE, SEARCH);
        query.put(Constants.KEY, Constants.AUTH_KEY);
        query.put(CATEGORY, getArguments().getString(CATEGORY_NAME));

        srlRefresh.setRefreshing(false);
        showLoader(true);
        Subscription subs = MainApplication.getInstance().component().getSmartPrixApi().searchProducts(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProductsData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogToast.log(TAG, "onError: " + e);
                        showError(true);
                        showLoader(false);
                    }

                    @Override
                    public void onNext(ProductsData productsData) {
                        showLoader(false);
                        if (Constants.ResultType.SUCCESS.equals(productsData.requestStatus)) {
                            showError(false);
                            adapter.refreshData(productsData.requestResult.products);
                        } else {
                            showError(true);
                            tvErrorMessage.setText(productsData.requestError);
                        }
                    }
                });
        compositeSubscription.add(subs);
    }

    private void showError(boolean isVisible) {
        if (isVisible) {
            llError.setVisibility(View.VISIBLE);
            rvProducts.setVisibility(View.GONE);
            tvErrorMessage.setText(R.string.error_fetching_products);
            showLoader(false);
        } else {
            llError.setVisibility(View.GONE);
            rvProducts.setVisibility(View.VISIBLE);
        }
    }

    private void showLoader(boolean isVisible) {
        if (isVisible) {
            pbLoader.setVisibility(View.VISIBLE);
            rvProducts.setVisibility(View.GONE);
            llError.setVisibility(View.GONE);
            srlRefresh.setRefreshing(false);
        } else {
            pbLoader.setVisibility(View.GONE);
            rvProducts.setVisibility(View.VISIBLE);
            srlRefresh.setRefreshing(false);
        }
    }

    private class RefreshData implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            srlRefresh.setRefreshing(true);
            getProductsFromServer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
}