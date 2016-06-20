package com.example.shivam.smartprixdemo.fragments.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shivam.smartprixdemo.R;
import com.example.shivam.smartprixdemo.main.CommonUtils;
import com.example.shivam.smartprixdemo.main.LogToast;
import com.example.shivam.smartprixdemo.network.data.ProductPriceData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shivam on 20-06-2016.
 */
public class ProductPriceAdapter extends RecyclerView.Adapter<ProductPriceAdapter.PriceHolder> {

    private Context mContext;
    private List<ProductPriceData.Prices> pricesList;

    public ProductPriceAdapter(Context context) {
        this.mContext = context;
        pricesList = new ArrayList<>();
    }

    @Override
    public PriceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_price_item, parent, false);
        return new PriceHolder(view);
    }

    @Override
    public void onBindViewHolder(PriceHolder holder, int position) {
        holder.tvPrice.setText(mContext.getString(R.string.rs_symbol) + pricesList.get(position).productPrice);
        CommonUtils.loadImage(mContext, pricesList.get(position).storeLogoUrl, R.drawable.ic_sm_logo, holder.ivStoreImage);
    }

    @Override
    public int getItemCount() {
        return pricesList.size();
    }

    public void refreshData(List<ProductPriceData.Prices> prices) {
        pricesList.clear();
        pricesList.addAll(prices);
        notifyDataSetChanged();
    }

    public class PriceHolder extends RecyclerView.ViewHolder {
        private ImageView ivStoreImage;
        private TextView tvPrice;
        private Button bBuyNow;

        public PriceHolder(View view) {
            super(view);
            ivStoreImage = (ImageView) view.findViewById(R.id.iv_store_logo);
            tvPrice = (TextView) view.findViewById(R.id.tv_price);
            bBuyNow = (Button) view.findViewById(R.id.b_buy_now);
            bBuyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogToast.toast(mContext, mContext.getString(R.string.buy_now_click));
                }
            });
        }
    }
}
