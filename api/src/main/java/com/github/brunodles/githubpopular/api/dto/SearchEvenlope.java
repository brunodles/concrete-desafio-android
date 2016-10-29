package com.github.brunodles.githubpopular.api.dto;

import java.util.List;

/**
 * Created by bruno on 29/10/16.
 */
public class SearchEvenlope {
    public Long total_count;
    public Boolean incomplete_results;
    public List<Item> items;
}
