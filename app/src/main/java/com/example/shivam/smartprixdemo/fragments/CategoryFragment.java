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
import com.example.shivam.smartprixdemo.fragments.adapters.CategoryAdapter;
import com.example.shivam.smartprixdemo.main.Constants;
import com.example.shivam.smartprixdemo.main.LogToast;
import com.example.shivam.smartprixdemo.main.MainApplication;
import com.example.shivam.smartprixdemo.network.SmartPrixApi;
import com.example.shivam.smartprixdemo.network.data.CategoryData;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Shivam on 17-06-2016.
 */
public class CategoryFragment extends Fragment {

    // Constants
    private static final String TAG = CategoryFragment.class.getSimpleName();
    private static final String CATEGORIES = "categories";

    // UI Stuff
    private LinearLayout llError;
    private RecyclerView rvCategories;
    private ProgressBar pbLoader;
    private SwipeRefreshLayout srlRefresh;
    private TextView tvErrorMessage;

    private CategoryAdapter adapter;

    // Rxjava stuff
    private CompositeSubscription compositeSubscription;

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_frag, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        compositeSubscription = new CompositeSubscription();

        llError = (LinearLayout) view.findViewById(R.id.ll_error_layout);
        rvCategories = (RecyclerView) view.findViewById(R.id.rv_categories);
        pbLoader = (ProgressBar) view.findViewById(R.id.pb_loader);
        tvErrorMessage = (TextView) view.findViewById(R.id.tv_error_text);
        srlRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl_refresh);
        srlRefresh.setColorSchemeColors(getActivity().getResources().getColor(R.color.color_accent));
        srlRefresh.setOnRefreshListener(new RefreshData());
        adapter = new CategoryAdapter(getActivity());
        rvCategories.setAdapter(adapter);
        rvCategories.setLayoutManager(new LinearLayoutManager(getActivity()));

        getCategories();
    }

    private void getCategories() {
        SmartPrixApi smartPrixApi = MainApplication.getInstance().component().getSmartPrixApi();

        Map<String, String> map = new HashMap<>();
        map.put(Constants.KEY, Constants.AUTH_KEY);
        map.put(Constants.TYPE, CATEGORIES);
        srlRefresh.setRefreshing(false);
        showLoader(true);

        Subscription subs = smartPrixApi.getCategories(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CategoryData>() {
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
                    public void onNext(CategoryData categoryData) {
                        showLoader(false);
                        if (Constants.ResultType.SUCCESS.equals(categoryData.requestStatus)) {
                            showError(false);
                            adapter.refreshData(categoryData.categories);
                        } else {
                            showError(true);
                            tvErrorMessage.setText(categoryData.requestError);
                        }
                    }
                });
        compositeSubscription.add(subs);
    }

    private class RefreshData implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            srlRefresh.setRefreshing(true);
            getCategories();
        }
    }

    private void showError(boolean isVisible) {
        if (isVisible) {
            llError.setVisibility(View.VISIBLE);
            rvCategories.setVisibility(View.GONE);
            showLoader(false);
            srlRefresh.setRefreshing(false);
        } else {
            llError.setVisibility(View.GONE);
            rvCategories.setVisibility(View.VISIBLE);
        }
    }

    private void showLoader(boolean isVisible) {
        if (isVisible) {
            pbLoader.setVisibility(View.VISIBLE);
            rvCategories.setVisibility(View.GONE);
            llError.setVisibility(View.GONE);
        } else {
            pbLoader.setVisibility(View.GONE);
            rvCategories.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
}
