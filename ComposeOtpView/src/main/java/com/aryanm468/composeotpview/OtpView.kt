package com.aryanm468.composeotpview

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly

@Composable
fun OtpView(
    enteredOtp: String,
    otpCharCount: Int,
    boxShape: Shape = RoundedCornerShape(16.dp),
    textStyle: TextStyle = LocalTextStyle.current.copy(
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.SemiBold
    ),
    spacerWidth: Dp = 8.dp,
    containerSize: Dp? = null,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    focusedBorderThickness: Dp = OutlinedTextFieldDefaults.FocusedBorderThickness,
    unfocusedBorderThickness: Dp = OutlinedTextFieldDefaults.UnfocusedBorderThickness,
    onOtpChanged: (String) -> Unit
) {
    if (enteredOtp.length != otpCharCount) {
        throw IllegalArgumentException("Entered otp length and otp char count do not match")
    }
    val focusRequesterList = List(otpCharCount) { FocusRequester() }
    var wasValueEntered by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        enteredOtp.forEachIndexed { index, otpCharacter ->
            OutlinedOtpBox(
                value = if (otpCharacter != Char.MIN_VALUE) otpCharacter.toString() else "",
                onValueChange = { updatedValue ->
                    if (updatedValue.length == otpCharCount && updatedValue.isDigitsOnly()) {
                        onOtpChanged(updatedValue)
                        focusRequesterList[otpCharCount - 1].requestFocus()
                        wasValueEntered = true
                    } else {
                        val valueToFill =
                            if (updatedValue.isDigitsOnly() && updatedValue.isNotBlank()) updatedValue[0] else Char.MIN_VALUE
                        val otpStringBuilder = StringBuilder(enteredOtp)
                        otpStringBuilder.setCharAt(index, valueToFill)
                        if (valueToFill.isDigit() && index + 1 != otpCharCount) {
                            focusRequesterList[index].freeFocus()
                            focusRequesterList[index + 1].requestFocus()
                        } else if (valueToFill == Char.MIN_VALUE) {
                            wasValueEntered = true
                        }
                        onOtpChanged(otpStringBuilder.toString())
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                textStyle = textStyle,
                modifier = Modifier
                    .let {
                        if (containerSize != null) it.size(containerSize)
                        else it
                            .weight(1f)
                            .aspectRatio(1f)
                    }
                    .focusRequester(focusRequesterList[index])
                    .onKeyEvent { event ->
                        if (event.type == KeyEventType.KeyUp && event.key == Key.Backspace) {
                            if (wasValueEntered) {
                                wasValueEntered = false
                            } else {
                                focusRequesterList[index].freeFocus()
                                if (index != 0) {
                                    focusRequesterList[index - 1].requestFocus()
                                }
                            }
                            true
                        } else {
                            false
                        }
                    },
                shape = boxShape,
                colors = colors,
                focusedBorderThickness = focusedBorderThickness,
                unfocusedBorderThickness = unfocusedBorderThickness
            )
            if (index + 1 != otpCharCount) {
                Spacer(modifier = Modifier.width(spacerWidth))
            }
        }
    }
    LaunchedEffect(key1 = true) {
        if (focusRequesterList.isNotEmpty()) {
            try {
                focusRequesterList[0].requestFocus()
            } catch (e: IllegalStateException) {
                Log.e("FocusError", "FocusRequester is not initialized: ${e.message}")
            }
        }
    }
}