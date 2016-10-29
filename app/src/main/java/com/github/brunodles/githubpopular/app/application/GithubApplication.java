package com.github.brunodles.githubpopular.app.application;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by bruno on 29/10/16.
 */

public class GithubApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        ApplicationMods.apply(this);
        LeakCanary.install(this);
        // Normal app init code...
    }
}
