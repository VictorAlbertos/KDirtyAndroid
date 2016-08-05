/*
 * Copyright 2016 Victor Albertos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.sections.dashboard;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import app.presentation.sections.launch.LaunchActivity;
import org.base_app_android.R;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static app.common.ViewActions.closeDrawer;
import static app.common.ViewActions.openDrawer;
import static org.hamcrest.Matchers.allOf;

public class DashboardTest {
  @Rule public ActivityTestRule<LaunchActivity> mActivityRule =
      new ActivityTestRule<>(LaunchActivity.class);

  @Test public void _1_Verify_Open_And_Close_Users() {
    onView(withId(R.id.drawer_layout)).perform(openDrawer());

    onView(withId(R.id.navigation_view)).check(matches(isDisplayed()));

    onView(allOf(withId(R.id.navigation_view),
        ViewMatchers.hasDescendant(withText(R.string.users)))).perform(click());

    onView(withId(R.id.drawer_layout)).perform(closeDrawer());
  }

  @Test public void _2_Verify_Open_And_Close_Search_User() {
    onView(withId(R.id.drawer_layout)).perform(openDrawer());

    onView(allOf(withId(R.id.navigation_view),
        ViewMatchers.hasDescendant(withText(R.string.find_user)))).perform(click());

    onView(withId(R.id.drawer_layout)).perform(closeDrawer());
  }

}
