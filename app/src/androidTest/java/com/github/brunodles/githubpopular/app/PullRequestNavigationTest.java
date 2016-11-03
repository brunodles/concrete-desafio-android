package com.github.brunodles.githubpopular.app;


import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.github.brunodles.githubpopular.app.application.ApplicationHelper;
import com.github.brunodles.githubpopular.app.application.Factory;
import com.github.brunodles.githubpopular.app.view.pull_request_list.PullRequestsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.parceler.Parcels;

import java.io.IOException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.any;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PullRequestNavigationTest {
    @Rule
    public ActivityTestRule<PullRequestsActivity> mActivityTestRule =
            new ActivityTestRule<>(PullRequestsActivity.class, false, false);

    @Before
    public void initIntent() {
        ApplicationHelper.setGithub(Factory.mockedGithub());
        Intent intent = new Intent();
        intent.putExtra(PullRequestsActivity.EXTRA_REPOSITORY, Parcels.wrap(Factory.repository()));
        mActivityTestRule.launchActivity(intent);

        Intents.init();
    }

    @After
    public void releaseIntents() throws IOException {
        Intents.release();
    }

    @Test
    public void whenClickOnPullRequestItem_LaunchBrowser() {
        ViewInteraction recyclerView = onView(allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));
        intended(allOf(hasAction(Intent.ACTION_VIEW), hasData(any(Uri.class))));
    }
}
