package com.aryanm468.composeotpview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aryanm468.composeotpview.beans.Dimensions
import com.aryanm468.composeotpview.utils.OtpInputFieldUtils

@Composable
fun OtpInputField(
    otpLength: Int,
    enteredOtp: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    focusedBorderThickness: Dp = OutlinedTextFieldDefaults.FocusedBorderThickness,
    unfocusedBorderThickness: Dp = OutlinedTextFieldDefaults.UnfocusedBorderThickness,
    boxShape: Shape = RoundedCornerShape(16.dp),
    spaceBetweenOtpBoxes: Dp = 8.dp,
    otpBoxDimensions: Dimensions = Dimensions(),
    onOtpChanged: (String) -> Unit
) {
    val trimmedOtp = enteredOtp.trim()
    val otpArray = remember {
        Array(otpLength) { index ->
            if (index < trimmedOtp.length) trimmedOtp[index] else Char.MIN_VALUE
        }
    }
    val focusManager = LocalFocusManager.current
    var isBackHandled by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceBetweenOtpBoxes)
    ) {
        repeat(otpLength) { index ->
            OutlinedOtpBox(
                value = if (otpArray[index] != Char.MIN_VALUE) otpArray[index].toString() else "",
                onValueChange = { updatedValue ->
                    val clipboardText = (clipboardManager.getText()?.text ?: "")
                    val isClipboardInput =
                        clipboardText.trim().isNotEmpty() && updatedValue == clipboardText
                    if (isClipboardInput) {
                        updatedValue.forEachIndexed { index, character ->
                            otpArray[index] = character
                        }
                        repeat(otpLength - index - 1) {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                        onOtpChanged(OtpInputFieldUtils.getOtpStringFromArray(otpArray))
                    } else if (updatedValue.isNotBlank()) {
                        otpArray[index] = updatedValue.first()
                        onOtpChanged(OtpInputFieldUtils.getOtpStringFromArray(otpArray))
                        if (index != otpLength - 1) {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    } else {
                        otpArray[index] = Char.MIN_VALUE
                        onOtpChanged(OtpInputFieldUtils.getOtpStringFromArray(otpArray))
                        if (index != 0) {
                            focusManager.moveFocus(FocusDirection.Previous)
                        }
                        isBackHandled = true
                    }
                },

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                textStyle = textStyle.copy(textAlign = TextAlign.Center),
                modifier = Modifier
                    .width(otpBoxDimensions.width)
                    .height(otpBoxDimensions.height)
                    .onKeyEvent { event ->
                        if (!isBackHandled) {
                            if (event.type == KeyEventType.KeyUp && event.key == Key.Backspace && index != 0) {
                                focusManager.moveFocus(FocusDirection.Previous)
                                true
                            } else {
                                isBackHandled = false
                                false
                            }
                        } else {
                            isBackHandled = false
                            false
                        }
                    },
                shape = boxShape,
                colors = colors,
                focusedBorderThickness = focusedBorderThickness,
                unfocusedBorderThickness = unfocusedBorderThickness
            )
        }
    }
}