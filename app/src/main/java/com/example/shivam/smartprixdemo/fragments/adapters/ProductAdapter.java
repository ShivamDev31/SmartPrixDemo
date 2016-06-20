package com.example.shivam.smartprixdemo.fragments.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shivam.smartprixdemo.R;
import com.example.shivam.smartprixdemo.activities.MainActivity;
import com.example.shivam.smartprixdemo.fragments.ProductPriceFragment;
import com.example.shivam.smartprixdemo.main.CommonUtils;
import com.example.shivam.smartprixdemo.main.LogToast;
import com.example.shivam.smartprixdemo.network.data.ProductsData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shivam on 18-06-2016.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductsHolder> {

    private Context mContext;
    private List<ProductsData.Results> productsList;

    public ProductAdapter(Context context) {
        productsList = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public ProductsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_item, parent, false);
        return new ProductsHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductsHolder holder, int position) {
        CommonUtils.loadImage(mContext, productsList.get(position).productImageUrl,
                R.drawable.ic_placeholder, holder.ivProductImage);
        holder.tvProductName.setText(productsList.get(position).productName);
        holder.tvProductPrice.setText(mContext.getString(R.string.rs_symbol) + productsList.get(position).productPrice);
        holder.tvBrandName.setText(productsList.get(position).brandName);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public void refreshData(List<ProductsData.Results> data) {
        productsList.clear();
        productsList.addAll(data);
        notifyDataSetChanged();
    }

    public class ProductsHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvBrandName;
        private TextView tvProductName;
        private TextView tvProductPrice;

        public ProductsHolder(View view) {
            super(view);
            ivProductImage = (ImageView) view.findViewById(R.id.iv_product_image);
            tvBrandName = (TextView) view.findViewById(R.id.tv_brand_name);
            tvProductName = (TextView) view.findViewById(R.id.tv_product_name);
            tvProductPrice = (TextView) view.findViewById(R.id.tv_product_price);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogToast.toast(mContext, "Clicked on :" + productsList.get(getLayoutPosition()).productName);
                    MainActivity activity = (MainActivity) mContext;
                    ProductPriceFragment priceFragment = ProductPriceFragment
                            .newInstance(productsList.get(getLayoutPosition()).productId);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment, priceFragment)
                            .addToBackStack(null).commit();
                }
            });
        }
    }
}
