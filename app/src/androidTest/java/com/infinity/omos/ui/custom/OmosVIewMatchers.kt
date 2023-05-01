package com.infinity.omos.ui.custom

import android.view.View
import androidx.core.content.ContextCompat
import com.infinity.omos.R
import com.infinity.omos.ui.onboarding.error.ErrorMessage
import com.infinity.omos.ui.view.OmosFieldView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object OmosViewMatchers {

    fun isDisplayedErrorMessage(): Matcher<View> {
        return object : TypeSafeMatcher<View>() {

            override fun matchesSafely(item: View?): Boolean {
                return (item as OmosFieldView).error != ErrorMessage.NO_ERROR
            }

            override fun describeTo(description: Description?) {}

            override fun describeMismatchSafely(item: View?, mismatchDescription: Description?) {
                super.describeMismatchSafely(item, mismatchDescription)
            }
        }
    }

    fun isDisplayedSuccessMessage(): Matcher<View> {
        return object : TypeSafeMatcher<View>() {

            override fun matchesSafely(item: View?): Boolean {
                return (item as OmosFieldView).binding.tvMsg.currentTextColor == ContextCompat.getColor(item.context, R.color.gray_05)
            }

            override fun describeTo(description: Description?) {}

            override fun describeMismatchSafely(item: View?, mismatchDescription: Description?) {
                super.describeMismatchSafely(item, mismatchDescription)
            }
        }
    }
}