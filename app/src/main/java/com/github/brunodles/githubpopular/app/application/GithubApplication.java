package com.github.brunodles.githubpopular.app.application;

import android.app.Application;

import com.github.brunodles.githubpopular.api.Api;
import com.github.brunodles.githubpopular.api.GithubEndpoint;
import com.github.brunodles.githubpopular.app.BuildConfig;
import com.squareup.leakcanary.LeakCanary;

import java.lang.ref.WeakReference;

import hugo.weaving.DebugLog;

/**
 * Created by bruno on 29/10/16.
 */

public class GithubApplication extends Application {

    private static WeakReference<Application> application;
    private static WeakReference<GithubEndpoint> github;

    @DebugLog
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        ApplicationMods.apply(this);
        LeakCanary.install(this);
        // Normal app init code...
        application = new WeakReference<>(this);
    }

    @DebugLog
    public static GithubEndpoint githubApi() {
        GithubEndpoint result;
        if (github == null || (result = github.get()) == null) {
            result = new Api(BuildConfig.API_URL, application.get().getCacheDir(),
                    () -> BuildConfig.API_CLIENT_ID,
                    () -> BuildConfig.API_CLIENT_SECRET).github();
            github = new WeakReference<>(result);
        }
        return result;
    }
}
