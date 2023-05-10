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
import com.infinity.omos.util.SUCCESS_AUTH_CODE
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@HiltAndroidTest
class NicknameFragmentTest {

    private val hiltRule = HiltAndroidRule(this)
    private val activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val rule: RuleChain = RuleChain
        .outerRule(hiltRule)
        .around(activityTestRule)

    @Before
    fun moveScreen() {
        onView(withId(R.id.tv_sign_up)).perform(click())
        onView(withId(R.id.ofv_email)).perform(OmosViewActions.replaceText("email@naver.com"))
        onView(withId(R.id.tv_send_auth_mail)).perform(click())
        onView(withId(R.id.tv_ok)).perform(click())
        onView(withId(R.id.tv_ok)).perform(click())
        onView(withId(R.id.ofv_email_auth_code)).perform(
            OmosViewActions.replaceText(
                SUCCESS_AUTH_CODE
            )
        )
        onView(withId(R.id.ofv_password)).perform(OmosViewActions.replaceText("password123!"))
        onView(withId(R.id.ofv_confirm_password)).perform(OmosViewActions.replaceText("password123!"))
        onView(withId(R.id.btn_next)).perform(click())
    }

    @Test
    fun `completeButton_whenNicknameInputAndTosAndPPCheck_activateCompleteButton`() {
        // given

        // when
        onView(withId(R.id.ofv_nickname)).perform(OmosViewActions.replaceText("nickname"))
        onView(withId(R.id.cb_tos)).perform(click())
        onView(withId(R.id.cb_pp)).perform(click())

        // then
        onView(withId(R.id.btn_complete)).check(matches(isEnabled()))
    }
}