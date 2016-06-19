package com.example.shivam.smartprixdemo.network.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Shivam on 18-06-2016.
 */
public class ProductsData {

    @SerializedName("request_status")
    public String requestStatus;

    @SerializedName("request_result")
    public RequestResult requestResult;

    @SerializedName("request_error")
    public String requestError;

    public static class RequestResult {

        @SerializedName("results")
        public List<Results> products;
    }

    public static class Results {

        @SerializedName("id")
        public String productId;

        @SerializedName("name")
        public String productName;

        @SerializedName("price")
        public String productPrice;

        @SerializedName("brand")
        public String brandName;

        @SerializedName("img_url")
        public String productImageUrl;
    }
}
