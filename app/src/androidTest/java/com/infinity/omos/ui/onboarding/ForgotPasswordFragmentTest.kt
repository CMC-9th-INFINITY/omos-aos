package com.infinity.omos.ui.onboarding

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.infinity.omos.ui.MainActivity
import com.infinity.omos.R
import com.infinity.omos.ui.custom.OmosViewActions
import com.infinity.omos.ui.custom.OmosViewMatchers
import com.infinity.omos.util.SUCCESS_AUTH_CODE
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@HiltAndroidTest
class ForgotPasswordFragmentTest {

    private val hiltRule = HiltAndroidRule(this)
    private val activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val rule: RuleChain = RuleChain
        .outerRule(hiltRule)
        .around(activityTestRule)

    @Before
    fun moveScreen() {
        onView(withId(R.id.tv_find_pw)).perform(click())
    }

    @Test
    fun `email_whenWrongEmail_showErrorMessage`() {
        // given

        // when
        onView(withId(R.id.ofv_email)).perform(OmosViewActions.replaceText("email"))
        onView(withId(R.id.tv_send_auth_mail)).perform(click())
        onView(withId(R.id.tv_ok)).perform(click())

        // then
        onView(withId(R.id.ofv_email))
            .check(matches(OmosViewMatchers.isDisplayedErrorMessage()))
    }

    @Test
    fun `email_whenCorrectEmail_showAuthCodeField`() {
        // given

        // when
        onView(withId(R.id.ofv_email)).perform(OmosViewActions.replaceText("email@naver.com"))
        onView(withId(R.id.tv_send_auth_mail)).perform(click())
        onView(withId(R.id.tv_ok)).perform(click())
        onView(withId(R.id.tv_ok)).perform(click())

        // then
        onView(withId(R.id.ofv_email_auth_code)).check(matches(isDisplayed()))
    }

    @Test
    fun `authCode_whenWrongAuthCode_showErrorMessage`() {
        // given

        // when
        onView(withId(R.id.ofv_email)).perform(OmosViewActions.replaceText("email@naver.com"))
        onView(withId(R.id.tv_send_auth_mail)).perform(click())
        onView(withId(R.id.tv_ok)).perform(click())
        onView(withId(R.id.tv_ok)).perform(click())
        onView(withId(R.id.ofv_email_auth_code)).perform(OmosViewActions.replaceText("000000"))

        // then
        onView(withId(R.id.ofv_email_auth_code)).check(matches(OmosViewMatchers.isDisplayedErrorMessage()))
    }

    /**
     * 각 검증을 분리해야하는가 고민했는데 nowinandroid에서도 하나의 상황에 대해 여러 결과를 검증하도록 구현되어 있음.
     * 굳이 하나하나 분리할 필요가 없다고 판단.
     */
    @Test
    fun `authCode_whenCorrectAuthCode_showSuccessMessageAndHideAuthCodeFieldAndActivateNextButton`() {
        // given

        // when
        onView(withId(R.id.ofv_email)).perform(OmosViewActions.replaceText("email@naver.com"))
        onView(withId(R.id.tv_send_auth_mail)).perform(click())
        onView(withId(R.id.tv_ok)).perform(click())
        onView(withId(R.id.tv_ok)).perform(click())
        onView(withId(R.id.ofv_email_auth_code)).perform(OmosViewActions.replaceText(SUCCESS_AUTH_CODE))

        // then
        onView(withId(R.id.ofv_email)).check(matches(OmosViewMatchers.isDisplayedSuccessMessage()))
        onView(withId(R.id.ofv_email_auth_code)).check(matches(not(isDisplayed())))
        onView(withId(R.id.btn_next)).check(matches(isEnabled()))
    }
}