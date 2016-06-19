package com.example.shivam.smartprixdemo.network;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shivamchopra on 04/06/16.
 */
@Module
public class NetworkModule {

    @Provides
    @Singleton
    public SmartPrixApi getSmartPrixApi() {
        return RetrofitAdapter.get().getRetrofit().create(SmartPrixApi.class);
    }
}
