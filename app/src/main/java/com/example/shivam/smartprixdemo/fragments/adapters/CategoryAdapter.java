package com.example.shivam.smartprixdemo.fragments.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shivam.smartprixdemo.R;
import com.example.shivam.smartprixdemo.activities.MainActivity;
import com.example.shivam.smartprixdemo.fragments.ProductFragment;
import com.example.shivam.smartprixdemo.main.LogToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shivam on 17-06-2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private static final String TAG = CategoryAdapter.class.getSimpleName();

    private Context mContext;
    private List<String> categoryData;

    public CategoryAdapter(Context context) {
        this.mContext = context;
        categoryData = new ArrayList<>();
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_item, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        holder.tvCategory.setText(categoryData.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryData.size();
    }

    public void refreshData(List<String> data) {
        categoryData.clear();
        categoryData.addAll(data);
        notifyDataSetChanged();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        private TextView tvCategory;

        public CategoryHolder(View view) {
            super(view);
            tvCategory = (TextView) view.findViewById(R.id.tv_category);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity activity = (MainActivity) mContext;
                    ProductFragment productFragment = ProductFragment.newInstance(categoryData.get(getLayoutPosition()));
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.ll_fragment, productFragment, TAG).addToBackStack(null).commit();
                    LogToast.toast(mContext, categoryData.get(getLayoutPosition()));
                }
            });
        }
    }
}