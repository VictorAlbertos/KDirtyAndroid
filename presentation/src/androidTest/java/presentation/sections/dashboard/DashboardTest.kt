/*
 * Copyright 2017 Victor Albertos
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

package presentation.sections.dashboard

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.DrawerActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.view.View
import io.victoralbertos.device_animation_test_rule.DeviceAnimationTestRule
import org.base_app_android.R
import org.hamcrest.Matchers.allOf
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import presentation.sections.launch.LaunchActivity

class DashboardTest {
    companion object {
        @ClassRule
        @JvmField
        var deviceAnimationTestRule = DeviceAnimationTestRule()
    }

    @Rule
    @JvmField
    var mActivityRule = ActivityTestRule(LaunchActivity::class.java)

    @Test
    fun _1_Verify_Open_And_Close_Users() {
        onView(withId(R.id.drawerLayout)).perform(DrawerActions.open())

        onView(withId(R.id.navigationView)).check(matches(isDisplayed()))

        onView(allOf<View>(withId(R.id.navigationView),
                ViewMatchers.hasDescendant(withText(R.string.users)))).perform(click())

        onView(withId(R.id.drawerLayout)).perform(DrawerActions.close())
    }

    @Test
    fun _2_Verify_Open_And_Close_Search_User() {
        onView(withId(R.id.drawerLayout)).perform(DrawerActions.open())

        onView(allOf<View>(withId(R.id.navigationView),
                ViewMatchers.hasDescendant(withText(R.string.find_user)))).perform(click())

        onView(withId(R.id.drawerLayout)).perform(DrawerActions.close())
    }
}
