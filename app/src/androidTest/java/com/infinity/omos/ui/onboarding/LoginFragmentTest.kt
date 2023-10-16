package com.infinity.omos.ui.onboarding

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.infinity.omos.ui.MainActivity
import com.infinity.omos.R
import com.infinity.omos.ui.custom.OmosViewActions
import com.infinity.omos.ui.custom.OmosViewMatchers
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@HiltAndroidTest
class LoginFragmentTest {

    private val hiltRule = HiltAndroidRule(this)
    private val activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val rule: RuleChain = RuleChain
        .outerRule(hiltRule)
        .around(activityTestRule)

    @Test
    fun `check_WrongEmail_ShowErrorMessage`() {
        // given

        // when
        onView(withId(R.id.ofv_email)).perform(OmosViewActions.replaceText("email"))
        onView(withId(R.id.ofv_password)).perform(click())

        // then
        onView(withId(R.id.ofv_email)).check(matches(OmosViewMatchers.isDisplayedErrorMessage()))
    }

    @Test
    fun `check_EmailAndPassword_ActivateLoginButton`() {
        // given

        // when
        onView(withId(R.id.ofv_email)).perform(OmosViewActions.replaceText("email@naver.com"))
        onView(withId(R.id.ofv_password)).perform(OmosViewActions.replaceText("password"))

        // then
        onView(withId(R.id.btn_login)).check(matches(isEnabled()))
    }
}