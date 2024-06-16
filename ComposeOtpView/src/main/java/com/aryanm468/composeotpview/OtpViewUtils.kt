package com.aryanm468.composeotpview

object OtpViewUtils {
    fun getFormattedInitialOtp(otpCharCount: Int): String {
        return " ".repeat(otpCharCount)
    }
}