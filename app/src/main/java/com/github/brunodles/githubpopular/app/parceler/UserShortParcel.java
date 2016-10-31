package com.github.brunodles.githubpopular.app.parceler;

import com.github.brunodles.githubpopular.api.dto.Commit;
import com.github.brunodles.githubpopular.api.dto.Href;
import com.github.brunodles.githubpopular.api.dto.Links;
import com.github.brunodles.githubpopular.api.dto.PullRequest;
import com.github.brunodles.githubpopular.api.dto.Repository;
import com.github.brunodles.githubpopular.api.dto.SearchEvenlope;
import com.github.brunodles.githubpopular.api.dto.User;
import com.github.brunodles.githubpopular.api.dto.UserShort;

import org.parceler.ParcelClass;
import org.parceler.ParcelClasses;

/**
 * Created by bruno on 31/10/16.
 */
//@Parcel(analyze = {Commit.class, Href.class, Links.class, PullRequest.class, Repository.class,
//        SearchEvenlope.class, User.class, UserShort.class})

@ParcelClasses({
        @ParcelClass(Commit.class),
        @ParcelClass(Href.class),
        @ParcelClass(Links.class),
        @ParcelClass(PullRequest.class),
        @ParcelClass(Repository.class),
        @ParcelClass(SearchEvenlope.class),
        @ParcelClass(User.class),
        @ParcelClass(UserShort.class)
})
public class UserShortParcel {
}
