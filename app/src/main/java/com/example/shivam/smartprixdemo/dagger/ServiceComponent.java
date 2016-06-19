package com.example.shivam.smartprixdemo.dagger;

import com.example.shivam.smartprixdemo.network.NetworkModule;
import com.example.shivam.smartprixdemo.network.SmartPrixApi;

import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by shivamchopra on 04/06/16.
 */

@Singleton
@Component(modules = {NetworkModule.class})
public interface ServiceComponent {

    SmartPrixApi getSmartPrixApi();
}
