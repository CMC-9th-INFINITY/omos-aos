package com.infinity.omos.utils

import androidx.core.util.PatternsCompat
import java.util.regex.Pattern

class Pattern {

    companion object {
        val emailPattern: Pattern = PatternsCompat.EMAIL_ADDRESS
        val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}\$".toRegex()
    }
}