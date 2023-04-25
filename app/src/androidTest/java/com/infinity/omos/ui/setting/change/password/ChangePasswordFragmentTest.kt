package com.infinity.omos.ui.setting.change.password

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.infinity.omos.OnboardingActivity
import com.infinity.omos.R
import com.infinity.omos.ui.custom.OmosViewActions
import com.infinity.omos.ui.custom.OmosViewMatchers
import com.infinity.omos.util.SUCCESS_AUTH_CODE
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@HiltAndroidTest
class ChangePasswordFragmentTest {

    private val hiltRule = HiltAndroidRule(this)
    private val activityTestRule = ActivityScenarioRule(OnboardingActivity::class.java)

    @get:Rule
    val rule: RuleChain = RuleChain
        .outerRule(hiltRule)
        .around(activityTestRule)

    @Before
    fun moveScreen() {
        onView(withId(R.id.tv_find_pw)).perform(click())
        onView(withId(R.id.ofv_email)).perform(OmosViewActions.replaceText("email@naver.com"))
        onView(withId(R.id.tv_send_auth_mail)).perform(click())
        onView(withId(R.id.tv_ok)).perform(click())
        onView(withId(R.id.tv_ok)).perform(click())
        onView(withId(R.id.ofv_email_auth_code)).perform(OmosViewActions.replaceText(SUCCESS_AUTH_CODE))
        onView(withId(R.id.btn_next)).perform(click())
    }

    @Test
    fun `password_whenWrongPassword_showErrorMessage`() {
        // given

        // when
        onView(withId(R.id.ofv_password)).perform(OmosViewActions.replaceText("password"))
        onView(withId(R.id.ofv_confirm_password)).perform(click())

        // then
        onView(withId(R.id.ofv_password)).check(matches(OmosViewMatchers.isDisplayedErrorMessage()))
    }

    @Test
    fun `confirmPassword_whenNotMatchPassword_showErrorMessage`() {
        // given

        // when
        onView(withId(R.id.ofv_password)).perform(OmosViewActions.replaceText("password123!"))
        onView(withId(R.id.ofv_confirm_password)).perform(OmosViewActions.replaceText("password123"))
        onView(withId(R.id.ofv_password)).perform(click())

        // then
        onView(withId(R.id.ofv_confirm_password)).check(matches(OmosViewMatchers.isDisplayedErrorMessage()))
    }

    @Test
    fun `completeButton_whenCorrectPassword_ActivateCompleteButton`() {
        // given

        // when
        onView(withId(R.id.ofv_password)).perform(OmosViewActions.replaceText("password123!"))
        onView(withId(R.id.ofv_confirm_password)).perform(OmosViewActions.replaceText("password123!"))

        // then
        onView(withId(R.id.btn_complete)).check(matches(isEnabled()))
    }
}