package com.example.testcryptoapp.ui.screens.login_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testcryptoapp.ui.theme.ErrorColor
import com.example.testcryptoapp.ui.theme.PrimaryColor
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyOtpBottomSheet(
    email: String,
    onVerify: (String) -> Unit,
    onResendCode: () -> Unit,
    onDismiss: () -> Unit,
    resendSeconds: Int = 30,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    val keyboard = LocalSoftwareKeyboardController.current

    var otp by rememberSaveable { mutableStateOf(List(6) { "" }) }
    var isVerifying by rememberSaveable { mutableStateOf(false) }
    var isError by rememberSaveable { mutableStateOf(false) }

    var secondsLeft by rememberSaveable { mutableIntStateOf(resendSeconds) }
    val canResend = secondsLeft <= 0
    fun code(): String = otp.joinToString("")

    LaunchedEffect(secondsLeft) {
        if (secondsLeft > 0) {
            delay(1000)
            secondsLeft -= 1
        }
    }

    LaunchedEffect(isVerifying) {
        if (!isVerifying) return@LaunchedEffect
        isVerifying = false
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Verify Email",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Email icon
            Surface(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(40.dp),
                color = Color(0xFFE3F2FD)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📧",
                        fontSize = 40.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Check your email",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We sent a verification code to",
                fontSize = 14.sp,
                color = Color(0xFF808080),
                textAlign = TextAlign.Center
            )

            Text(
                text = email,
                fontSize = 14.sp,
                color = Color(0xFF1A1A1A),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            OtpInputRow(
                otp = otp,
                onOtpChange = { newOtp ->
                    otp = newOtp
                    if (isError) isError = false
                },
                isError = isError,
                onCompleted = {
                    keyboard?.hide()
                }
            )

            if (isError) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Invalid code. Please try again.",
                    color = ErrorColor,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (isVerifying) return@Button
                    isVerifying = true
                    keyboard?.hide()

                    val entered = code()
                    if (entered.length != 6) {
                        isVerifying = false
                        return@Button
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A9EFF)
                ),
                enabled = otp.all { it.isNotEmpty() } && !isVerifying
            ) {
                Text(
                    text = "Verify Code",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Didn't receive the code?",
                    fontSize = 14.sp,
                    color = Color(0xFF808080)
                )


                if (!canResend) {
                    Text(
                        text = "Resend code in ${formatTime(secondsLeft)}",
                        fontSize = 14.sp,
                        color = Color(0xFF808080)
                    )
                }

                TextButton(onClick = onResendCode) {
                    Text(
                        text = "Resend Code",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (canResend) Color(0xFF4A9EFF) else Color(0xFFB0B0B0)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun OtpInputRow(
    otp: List<String>,
    onOtpChange: (List<String>) -> Unit,
    isError: Boolean,
    onCompleted: () -> Unit,
    length: Int = 6
) {
    val focusRequesters = remember { List(length) { FocusRequester() } }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        repeat(length) { index ->
            OtpCell(
                value = otp[index],
                isError = isError,
                modifier = Modifier.weight(1f),
                focusRequester = focusRequesters[index],
                onValueChange = { input ->
                    val digits = input.filter { it.isDigit() }

                    if (digits.isEmpty()) {
                        onOtpChange(otp.toMutableList().also { it[index] = "" })
                        return@OtpCell
                    }

                    if (digits.length > 1) {
                        val new = otp.toMutableList()
                        var i = index
                        for (c in digits) {
                            if (i >= length) break
                            new[i] = c.toString()
                            i++
                        }
                        onOtpChange(new)

                        val next = new.indexOfFirst { it.isEmpty() }
                        if (next == -1) {
                            onCompleted()
                        } else {
                            focusRequesters[next].requestFocus()
                        }
                        return@OtpCell
                    }

                    val new = otp.toMutableList().also { it[index] = digits }
                    onOtpChange(new)

                    if (index < length - 1) {
                        focusRequesters[index + 1].requestFocus()
                    } else {
                        if (new.all { it.isNotEmpty() }) onCompleted()
                    }
                },
                onBackspaceOnEmpty = {
                    if (index > 0) {
                        val new = otp.toMutableList().also { it[index - 1] = "" }
                        onOtpChange(new)
                        focusRequesters[index - 1].requestFocus()
                    }
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        focusRequesters.first().requestFocus()
    }
}

@Composable
private fun OtpCell(
    value: String,
    isError: Boolean,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit,
    onBackspaceOnEmpty: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    val borderColor = when {
        isError -> ErrorColor
        isFocused -> PrimaryColor
        else -> Color(0xFFE0E0E0)
    }

    Box(
        modifier = modifier
            .height(56.dp)
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A),
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { isFocused = it.isFocused }
                .onPreviewKeyEvent { event ->
                    if (event.type == KeyEventType.KeyDown &&
                        event.key == Key.Backspace &&
                        value.isEmpty()
                    ) {
                        onBackspaceOnEmpty()
                        true
                    } else false
                },
            decorationBox = { inner ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (value.isEmpty()) {
                        Text("—", fontSize = 24.sp, color = Color(0xFFE0E0E0))
                    }
                    inner()
                }
            }
        )
    }
}


private fun formatTime(totalSeconds: Int): String {
    val m = totalSeconds / 60
    val s = totalSeconds % 60
    val ss = if (s < 10) "0$s" else "$s"
    val mm = if (m < 10) "0$m" else "$m"
    return "$mm:$ss"
}