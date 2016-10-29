package com.github.brunodles.githubpopular.api.dto;

import com.github.brunodles.githubpopular.api.GithubEndpoint;

/**
 * Created by bruno on 29/10/16.
 */
public class Commit {
    public String label;
    public String ref;
    public String sha;
    public User user;
    public Repo repo;
}
