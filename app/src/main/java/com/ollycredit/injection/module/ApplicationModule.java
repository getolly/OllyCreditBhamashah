package com.ollycredit.injection.module;

import android.app.Application;
import android.content.Context;


import com.ollycredit.OllyCreditApplication;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.remote.BaseApiManager;
import com.ollycredit.injection.scope.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ch8n on 3/5/17.
 */
@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(OllyCreditApplication application) {
        this.application = application;
    }

    @Provides
    @ApplicationContext
    Context providesContext() {
        return application;
    }

    @Provides
    Application providesApplication() {
        return application;
    }


    @Provides
    @Singleton
    PreferencesHelper providePrefManager(@ApplicationContext Context context) {
        return new PreferencesHelper(context);
    }

    @Provides
    @Singleton
    BaseApiManager provideBaseApiManager() {
        return new BaseApiManager();
    }


}
