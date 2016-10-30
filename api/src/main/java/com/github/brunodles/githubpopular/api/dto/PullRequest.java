package com.github.brunodles.githubpopular.api.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by bruno on 29/10/16.
 */
public class PullRequest {
    public String url;
    public Long id;
    public String html_url;
    public String diff_url;
    public String patch_url;
    public String issue_url;
    public Long number;
    public String state;
    public Boolean locked;
    public String title;
    public UserShort user;
    public String body;
    public String created_at;
    public String updated_at;
    public Date closed_at;
    public Date merged_at;
    public String merge_commit_sha;
    public UserShort assignee;
    public List<UserShort> assignees;
    //        public Milestone milestone;
    public String commits_url;
    public String review_comments_url;
    public String review_comment_url;
    public String comments_url;
    public String statuses_url;
    public Commit head;
    public Commit base;
    @SerializedName("_links") public Links links;
}
