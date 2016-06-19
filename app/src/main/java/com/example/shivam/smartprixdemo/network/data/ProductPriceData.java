package com.example.shivam.smartprixdemo.network.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shivam on 18-06-2016.
 */
public class ProductPriceData {

    @SerializedName("request_status")
    public String requestStatus;

    @SerializedName("request_result")
    public Result result;

    @SerializedName("request_error")
    public String requestError;

    public static class Result {

        @SerializedName("id")
        public int id;

        @SerializedName("name")
        public String productName;

        @SerializedName("img_url")
        public String imageUrl;

        @SerializedName("prices")
        public Prices prices;
    }

    public static class Prices {

        @SerializedName("logo")
        public String storeLogoUrl;

        @SerializedName("name")
        public String productName;

        @SerializedName("price")
        public String productPrice;
    }
}
