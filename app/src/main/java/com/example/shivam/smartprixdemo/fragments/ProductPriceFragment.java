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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shivam.smartprixdemo.R;
import com.example.shivam.smartprixdemo.fragments.adapters.ProductPriceAdapter;
import com.example.shivam.smartprixdemo.main.CommonUtils;
import com.example.shivam.smartprixdemo.main.Constants;
import com.example.shivam.smartprixdemo.main.LogToast;
import com.example.shivam.smartprixdemo.main.MainApplication;
import com.example.shivam.smartprixdemo.network.data.ProductPriceData;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Shivam on 20-06-2016.
 */
public class ProductPriceFragment extends Fragment {

    private static final String TAG = ProductPriceFragment.class.getSimpleName();
    private static final String ID_KEY = "id";
    private static final String PRODUCT_FULL = "product_full";

    private LinearLayout llProductDetails;
    private LinearLayout llError;
    private TextView tvErrorMessage;
    private RecyclerView rvPrices;
    private ImageView ivProductImage;
    private TextView tvProductName;
    private TextView tvBestPrice;
    private TextView tvNoStoresAvailabe;
    private TextView tvStoresNos;
    private ProgressBar pbLoader;
    private ProductPriceAdapter adapter;
    private SwipeRefreshLayout srlRefresh;

    private CompositeSubscription compositeSubscription;

    public static ProductPriceFragment newInstance(String id) {
        ProductPriceFragment fragment = new ProductPriceFragment();
        Bundle args = new Bundle();
        args.putString(ID_KEY, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_prices_frag, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        compositeSubscription = new CompositeSubscription();
        llProductDetails = (LinearLayout) view.findViewById(R.id.ll_product_details);
        llError = (LinearLayout) view.findViewById(R.id.ll_error_layout);
        tvErrorMessage = (TextView) view.findViewById(R.id.tv_error_text);
        rvPrices = (RecyclerView) view.findViewById(R.id.rv_product_prices);
        ivProductImage = (ImageView) view.findViewById(R.id.iv_product_image);
        tvProductName = (TextView) view.findViewById(R.id.tv_product_name);
        tvBestPrice = (TextView) view.findViewById(R.id.tv_best_price);
        tvNoStoresAvailabe = (TextView) view.findViewById(R.id.tv_no_stores_avail);
        tvStoresNos = (TextView) view.findViewById(R.id.tv_available_at);
        srlRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl_refresh);
        srlRefresh.setColorSchemeColors(getActivity().getResources().getColor(R.color.color_accent));
        srlRefresh.setOnRefreshListener(new RefreshData());
        pbLoader = (ProgressBar) view.findViewById(R.id.pb_loader);
        adapter = new ProductPriceAdapter(getActivity());
        rvPrices.setAdapter(adapter);
        rvPrices.setLayoutManager(new LinearLayoutManager(getActivity()));

        getProductPrices();
    }

    private void getProductPrices() {
        Map<String, String> query = new HashMap<>();
        query.put(Constants.TYPE, PRODUCT_FULL);
        query.put(Constants.KEY, Constants.AUTH_KEY);
        query.put(ID_KEY, getArguments().getString(ID_KEY));

        srlRefresh.setRefreshing(false);
        showLoader(true);

        Subscription subs = MainApplication.getInstance().component().getSmartPrixApi().getProductDetails(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProductPriceData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogToast.log(TAG, "onError: " + e);
                        showError(true);
                    }

                    @Override
                    public void onNext(ProductPriceData productPriceData) {
                        showLoader(false);
                        if (Constants.ResultType.SUCCESS.equals(productPriceData.requestStatus)) {
                            setDataOnUi(productPriceData);
                        } else {
                            showError(true);
                            tvErrorMessage.setText(productPriceData.requestError);
                        }
                    }
                });
        compositeSubscription.add(subs);
    }

    private void setDataOnUi(ProductPriceData productPriceData) {
        tvProductName.setText(productPriceData.result.productName);
        tvStoresNos.setText(String.format(getString(R.string.available_at), productPriceData.result.prices.size()));
        CommonUtils.loadImage(getActivity(), productPriceData.result.imageUrl, R.drawable.logo_big, ivProductImage);
        if (productPriceData.result.prices.size() > 0) {
            tvBestPrice.setText(String.format(getString(R.string.best_price), productPriceData.result.prices.get(0).productPrice));
            rvPrices.setVisibility(View.VISIBLE);
            adapter.refreshData(productPriceData.result.prices);
            tvNoStoresAvailabe.setVisibility(View.GONE);
        } else {
            rvPrices.setVisibility(View.GONE);
            tvNoStoresAvailabe.setVisibility(View.VISIBLE);
            tvBestPrice.setText(String.format(getString(R.string.best_price), productPriceData.result.productPrice));
        }
    }

    private void showError(boolean isVisible) {
        if (isVisible) {
            llError.setVisibility(View.VISIBLE);
            llProductDetails.setVisibility(View.GONE);
            pbLoader.setVisibility(View.GONE);
            tvErrorMessage.setText(R.string.product_price_error);
        } else {
            llError.setVisibility(View.GONE);
            llProductDetails.setVisibility(View.VISIBLE);
        }
    }

    private void showLoader(boolean isVisible) {
        if (isVisible) {
            pbLoader.setVisibility(View.VISIBLE);
            llProductDetails.setVisibility(View.GONE);
        } else {
            pbLoader.setVisibility(View.GONE);
            llProductDetails.setVisibility(View.VISIBLE);
        }
    }

    private class RefreshData implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            srlRefresh.setRefreshing(true);
            getProductPrices();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
}
