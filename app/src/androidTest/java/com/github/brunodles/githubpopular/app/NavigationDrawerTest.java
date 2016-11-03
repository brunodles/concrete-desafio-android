package com.github.brunodles.githubpopular.app;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.github.brunodles.githubpopular.app.view.repository_list.RepositoryListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.PositionAssertions.isBelow;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NavigationDrawerTest {

    @Rule
    public ActivityTestRule<RepositoryListActivity> mActivityTestRule =
            new ActivityTestRule<>(RepositoryListActivity.class);

    @Test
    public void testNavigationDrawer() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withClassName(is("android.support.v7.widget.AppCompatImageButton")),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(withId(R.id.appbar)))),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction aboutText = onView(allOf(withText(R.string.about_text),
                isDisplayed()
        ));
        aboutText.check(isAbove(withId(R.id.aboutImage)));

        ViewInteraction aboutName = onView(allOf(withText(R.string.about_name),
                isDisplayed()
        ));
        aboutName.check(isBelow(withId(R.id.aboutImage)));

        ViewInteraction aboutDescription = onView(allOf(withText(R.string.about_description),
                isDisplayed()
        ));
        aboutDescription.check(isBelow(withId(R.id.aboutImage)));
    }

}
