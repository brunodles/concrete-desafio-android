package com.github.brunodles.githubpopular.app;


import android.content.Intent;
import android.os.Parcelable;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.github.brunodles.githubpopular.app.application.ApplicationHelper;
import com.github.brunodles.githubpopular.app.application.Factory;
import com.github.brunodles.githubpopular.app.view.pull_request_list.PullRequestsActivity;
import com.github.brunodles.githubpopular.app.view.repository_list.RepositoryListActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RepositoryNavigationTest {

    @Rule
    public ActivityTestRule<RepositoryListActivity> mActivityTestRule =
            new ActivityTestRule<>(RepositoryListActivity.class, false, false);

    @Before
    public void initIntents() {
        ApplicationHelper.setGithub(Factory.mockedGithub());

        mActivityTestRule.launchActivity(new Intent());
        Intents.init();
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    @Test
    public void whenClickOnRepositoryItem_LaunchPullRequestActivity() {
        ViewInteraction recyclerView = onView(allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));
        intended(allOf(hasComponent(PullRequestsActivity.class.getName()),
                hasExtra(equalTo(PullRequestsActivity.EXTRA_REPOSITORY), any(Parcelable.class))
        ));
    }
}
