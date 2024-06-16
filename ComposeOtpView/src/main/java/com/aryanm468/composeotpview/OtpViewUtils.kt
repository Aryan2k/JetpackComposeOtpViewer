package com.aryanm468.composeotpview

object OtpViewUtils {
    fun getFormattedInitialOtp(otpCharCount: Int): String {
        var initialDefaultOtp = ""
        repeat(otpCharCount) {
            initialDefaultOtp += Char.MIN_VALUE
        }
        return initialDefaultOtp
    }
}