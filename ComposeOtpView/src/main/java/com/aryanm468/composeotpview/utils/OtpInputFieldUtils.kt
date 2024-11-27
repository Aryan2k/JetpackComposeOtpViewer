package com.aryanm468.composeotpview.utils

object OtpInputFieldUtils {
    internal fun getOtpStringFromArray(otpArray: Array<Char>): String {
        return otpArray.joinToString("") { if (it == Char.MIN_VALUE) " " else it.toString() }
            .trimEnd()
    }
}