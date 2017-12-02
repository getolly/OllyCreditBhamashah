package com.ollycredit;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.ollycredit.features.receivers.google_analytic.AnalyticsTrackers;
import com.ollycredit.injection.component.ApplicationComponent;
import com.ollycredit.injection.component.DaggerApplicationComponent;
import com.ollycredit.injection.module.ApplicationModule;
import com.onesignal.OneSignal;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class OllyCreditApplication extends Application {

    public static final String TAG = OllyCreditApplication.class.getSimpleName();

    ApplicationComponent applicationComponent;

    private static OllyCreditApplication instance;

    public static OllyCreditApplication get(Context context) {
        return (OllyCreditApplication) context.getApplicationContext();
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static OllyCreditApplication getInstance() {
        return instance;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();

        instance=this;

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Muli-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        Log.d("OneSignalTag", "Before OneSignal init");

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);

        Log.d("OneSignalTag", "After OneSignal init");

    }

    public ApplicationComponent component() {
        return applicationComponent;
    }


    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }


    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    /***
     * Tracking exception
     *
     * @param e exception to be tracked
     */
    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(this, null)
                                    .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }

    /***
     * Tracking event
     *
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }
}
