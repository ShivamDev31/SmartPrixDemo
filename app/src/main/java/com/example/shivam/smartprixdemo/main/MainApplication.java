package com.example.shivam.smartprixdemo.main;

import android.app.Application;

import com.example.shivam.smartprixdemo.dagger.DaggerServiceComponent;
import com.example.shivam.smartprixdemo.dagger.ServiceComponent;


/**
 * Created by shivamchopra on 03/06/16.
 */
public class MainApplication extends Application {
    private static MainApplication instance;
    private ServiceComponent component;

    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        component = DaggerServiceComponent.builder().build();
    }

    public ServiceComponent component() {
        return component;
    }
}
