package com.ollycredit.injection.component;

import android.app.Application;
import android.content.Context;


import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.remote.BaseApiManager;
import com.ollycredit.api.remote.DataManager;
import com.ollycredit.injection.module.ApplicationModule;
import com.ollycredit.injection.scope.ApplicationContext;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ch8n on 3/5/17.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    Application application();
    PreferencesHelper preferenceHelper();
    BaseApiManager baseApiManager();
    DataManager dataManager();


}
