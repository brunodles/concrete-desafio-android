package com.github.brunodles.githubpopular.app.application;

import com.github.brunodles.githubpopular.api.GithubEndpoint;

import java.lang.ref.WeakReference;

/**
 * Created by bruno on 03/11/16.
 */

public class ApplicationHelper {
    public static void setGithub(GithubEndpoint githubEndpoint) {
        GithubApplication.github = new WeakReference<>(githubEndpoint);
    }
}
