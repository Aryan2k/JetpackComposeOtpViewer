package com.aryanm468.composeotpview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
    otpCharCount: Int = 6,
    shape: Shape = RoundedCornerShape(16.dp),
    textStyle: TextStyle = LocalTextStyle.current.copy(
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.SemiBold
    ),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    focusedBorderThickness: Dp = OutlinedTextFieldDefaults.FocusedBorderThickness,
    unfocusedBorderThickness: Dp = OutlinedTextFieldDefaults.UnfocusedBorderThickness,
    onOtpChanged: (String) -> Unit
) {
    val focusRequesterList = List(otpCharCount) { FocusRequester() }
    var wasValueEntered by remember { mutableStateOf(false) }
    val otpState = remember { mutableStateOf(" ".repeat(otpCharCount)) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(otpCharCount) { index ->
            val enteredValue = otpState.value[index].toString()
            OutlinedOtpBox(
                value = enteredValue.ifBlank { "" },
                onValueChange = { updatedValue ->
                    val valueToFill =
                        if (updatedValue.isDigitsOnly() && updatedValue.isNotBlank()) updatedValue[0].toString() else " "
                    val updatedOTP = if (index == 0) {
                        "$valueToFill${otpState.value.substring(1)}"
                    } else {
                        val currentOtp = otpState.value
                        "${
                            currentOtp.substring(
                                0,
                                index
                            )
                        }$valueToFill${currentOtp.substring(index + 1)}"
                    }
                    otpState.value = updatedOTP
                    onOtpChanged(otpState.value)
                    if (valueToFill.isDigitsOnly() && index + 1 != otpCharCount) {
                        focusRequesterList[index + 1].requestFocus()
                    } else if (valueToFill.isBlank()) {
                        wasValueEntered = true
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                textStyle = textStyle,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .focusRequester(focusRequesterList[index])
                    .onKeyEvent { event ->
                        if (event.type == KeyEventType.KeyUp && event.key == Key.Backspace) {
                            if (wasValueEntered) {
                                wasValueEntered = false
                            } else {
                                if (index != 0) {
                                    focusRequesterList[index - 1].requestFocus()
                                }
                            }
                            true
                        } else {
                            false
                        }
                    },
                shape = shape,
                colors = colors,
                focusedBorderThickness = focusedBorderThickness,
                unfocusedBorderThickness = unfocusedBorderThickness
            )
            if (index + 1 != otpCharCount) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
    LaunchedEffect(key1 = true) {
        if (focusRequesterList.isNotEmpty()) {
            focusRequesterList[0].requestFocus()
        }
    }
}