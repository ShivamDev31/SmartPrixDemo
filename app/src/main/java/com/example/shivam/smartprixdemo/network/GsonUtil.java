package com.example.shivam.smartprixdemo.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by shivamchopra on 04/06/16.
 */
public class GsonUtil {
    private static GsonUtil gsonUtils;
    private final Gson gson;

    private GsonUtil() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
    }

    public static synchronized GsonUtil getInstance() {
        if (gsonUtils == null) {
            gsonUtils = new GsonUtil();
        }
        return gsonUtils;
    }


    public <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public Gson getGson() {
        return gson;
    }
}