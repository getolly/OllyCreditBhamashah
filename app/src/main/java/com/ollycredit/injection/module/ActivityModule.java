package com.ollycredit.injection.module;

import android.app.Activity;
import android.content.Context;


import com.ollycredit.injection.scope.ActivityContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ch8n on 3/5/17.
 */
@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    Activity providesActivity() {
        return activity;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return activity;
    }

}
