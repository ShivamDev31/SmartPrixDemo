package com.example.shivam.smartprixdemo.network;

import com.example.shivam.smartprixdemo.network.data.CategoryData;
import com.example.shivam.smartprixdemo.network.data.ProductsData;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Shivam on 17-06-2016.
 */
public interface SmartPrixApi {

    @GET("/simple/v1")
    Observable<CategoryData> getCategories(@QueryMap Map<String, Object> queryMap);

    //http://api.smartprix.com/simple/v1?type=search&key=NVgien7bb7P5Gsc8DWqc&category=Mobiles&q=3g&indent=1
    @GET("/simple/v1")
    Observable<ProductsData> searchProducts(@QueryMap Map<String, Object> queryMap);
}
