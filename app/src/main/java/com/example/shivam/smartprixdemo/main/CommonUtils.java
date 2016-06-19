package com.example.shivam.smartprixdemo.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by shivamchopra on 04/06/16.
 */
public class CommonUtils {

    public static void loadImage(Context context, String imageUrl, int placeHolder, ImageView imageView) {
        if (TextUtils.isEmpty(imageUrl)) {
            Glide.with(context).load(placeHolder).into(imageView);
            return;
        }
        Glide.with(context).load(imageUrl).placeholder(placeHolder).centerCrop().into(imageView);
    }

    public static boolean isNetworkConnectedOrConnecting(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(MainApplication.getInstance().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
