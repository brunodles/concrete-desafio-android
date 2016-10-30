package com.github.brunodles.githubpopular.api.dto;

/**
 * Created by bruno on 29/10/16.
 */
public class Commit {
    public String label;
    public String ref;
    public String sha;
    public UserShort user;
    public Repository repo;
}
