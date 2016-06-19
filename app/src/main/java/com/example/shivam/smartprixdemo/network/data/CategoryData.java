package com.example.shivam.smartprixdemo.network.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Shivam on 17-06-2016.
 */
public class CategoryData {
    @SerializedName("request_status")
    public String requestStatus;

    @SerializedName("request_error")
    public String requestError;

    @SerializedName("request_result")
    public List<String> categories;
}
