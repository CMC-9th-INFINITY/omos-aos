package com.infinity.omos.ui.onboarding

import android.view.View
import android.widget.EditText
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.infinity.omos.ui.view.OmosFieldView
import com.infinity.omos.utils.Pattern
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

object OmosViewActions {

    fun replaceText(text: String): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "replace text"
            }

            override fun getConstraints(): Matcher<View> {
                return allOf(isDisplayed(), isAssignableFrom(OmosFieldView::class.java))
            }

            override fun perform(uic: UiController, view: View) {
                val omosFieldView = view as OmosFieldView
                omosFieldView.requestFocus()
                omosFieldView.text = text
            }
        }
    }
}