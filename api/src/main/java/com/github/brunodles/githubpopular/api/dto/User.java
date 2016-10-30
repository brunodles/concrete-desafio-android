package com.github.brunodles.githubpopular.api.dto;

/**
 * Created by bruno on 30/10/16.
 */

public class User extends UserShort {
    public String name;
    public String company;
    public String blog;
    public String location;
    public String email;
    public Boolean hireable;
    public String bio;
    public Long public_repos;
    public Long public_gists;
    public Long followers;
    public Long following;
    public String created_at;
    public String updated_at;
}
