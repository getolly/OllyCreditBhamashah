package com.ollycredit.injection.module;

import android.app.Service;
import android.content.Context;


import com.ollycredit.injection.scope.ActivityContext;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    Service service;

    public ServiceModule(Service service) {
        this.service = service;
    }


    @Provides
    Service providesService() {
        return service;
    }


    @Provides
    @ActivityContext
    Context providesContext() { return service; }
}
