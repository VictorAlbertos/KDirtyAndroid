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

package app.sections.users

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.BoundedMatcher
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import app.common.openDrawer
import app.presentation.sections.launch.LaunchActivity
import io.victoralbertos.device_animation_test_rule.DeviceAnimationTestRule
import org.base_app_android.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.ClassRule
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UsersTest {
    @Rule @JvmField var mActivityRule = ActivityTestRule(LaunchActivity::class.java)

    companion object {
        @ClassRule @JvmField var deviceAnimationTestRule = DeviceAnimationTestRule()
    }

    private val USERNAME = "FuckBoilerplate"

    @Test fun _1_Verify_Get_Users() {
        onView(withId(R.id.rvUsers))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
    }

    @Test fun _2_Verify_Get_User() {
        onView(withId(R.id.rvUsers))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(8, click()))

        onView(withId(R.id.tvName)).check(matches(isNotEmpty))
    }

    @Test fun _3_Verify_Search_User() {
        onView(withId(R.id.drawerLayout)).perform(openDrawer())

        onView(Matchers.allOf(withId(R.id.navigationView),
                hasDescendant(withText(R.string.find_user)))).perform(click())

        onView(withId(R.id.etName)).perform(click(), replaceText(USERNAME), closeSoftKeyboard())
        onView(withId(R.id.btFindUser)).perform(click())

        onView(withId(R.id.tvName)).check(matches(isNotEmpty))
    }

    private val isNotEmpty: Matcher<View>
        get() = object : BoundedMatcher<View, TextView>(TextView::class.java) {

            override fun describeTo(description: Description) {
                description.appendText("not empty text: ")
            }

            public override fun matchesSafely(textView: TextView): Boolean {
                return !textView.text.toString().isEmpty()
            }
        }
}
