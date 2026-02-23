package com.pedro.test_1.ui.screen.auth

import android.R.attr.y
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.pedro.test_1.R
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import kotlin.math.max

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AuthScreen(
    state: AuthState,
    onIntent: (AuthIntent) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    var inputUsername by remember { mutableStateOf("") }
    var inputPassword by remember { mutableStateOf("") }
    var loginEntered by remember { mutableStateOf(false) }
    val allowedSymbolsUser = Pattern.compile("[a-zA-Z0-9]{0,9}")
    val allowedSymbolsPass = Pattern.compile("[0-9]{0,4}")
    val pagerState = rememberPagerState(pageCount = {2})
    val isFocused = WindowInsets.isImeVisible

    val density = LocalDensity.current
    val imeBottom = WindowInsets.ime.getBottom(density)
    val globalPadding by animateDpAsState(
        if (imeBottom > 0) 20.dp else 48.dp
    )

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    fun handleContinue() {
        if (!loginEntered) {
            loginEntered = true
            focusRequester.requestFocus()
        }
        else {
            onIntent(AuthIntent.Submit(inputUsername, inputPassword))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state) {
            is AuthState.Idle -> {
                Column(
                    modifier = Modifier
                        .padding(globalPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Icon",
                        modifier = Modifier.size(150.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )

                    Spacer(modifier = Modifier.size(20.dp))

                    Text(
                        text = "Sign in",
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "with your S-App account",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.size(128.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth().height(60.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            userScrollEnabled = true,
                            modifier = Modifier
                                .weight(1f),
                            beyondViewportPageCount = 1
                        ) { page ->
                            when(page) {
                                0 -> OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = inputUsername,
                                    onValueChange = {
                                        if (allowedSymbolsUser.matcher(it).matches()) {
                                            inputUsername = it
                                            onIntent(AuthIntent.UpdateInput(it))
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    label = {
                                        Text(
                                            text = if(isFocused) {
                                                "Login"
                                            } else { "Enter your login" }
                                        )
                                    },
                                )
                                1 -> OutlinedTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .focusRequester(focusRequester),
                                    value = inputPassword,
                                    onValueChange = {
                                        if (allowedSymbolsPass.matcher(it).matches()) {
                                            inputPassword = it
                                            onIntent(AuthIntent.UpdateInput(it))
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    label = {
                                        Text(
                                            text = "Password",
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                    }
                                )
                            }
                        }

                        AnimatedVisibility(isFocused) {
                            Spacer(modifier = Modifier.width(20.dp))
                        }
                        AnimatedVisibility(isFocused) {
                            FilledTonalButton(
                                onClick = { handleContinue() },
                                modifier = Modifier
                                    .size(56.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                ButtonIcon(loginEntered)
                            }
                        }
                    }

                    AnimatedVisibility(!isFocused) {
                        Spacer(modifier = Modifier.size(20.dp))
                    }

                    AnimatedVisibility(!isFocused) {
                        FilledTonalButton(
                            onClick = { handleContinue() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if(loginEntered) {"Continue"} else {"Log in"},
                                    style = MaterialTheme.typography.titleLarge
                                )
                                ButtonIcon(loginEntered)
                            }
                        }
                    }
                }
                Spacer(
                    modifier = Modifier
                        .size(androidx.compose.ui.unit.max(with(density) { imeBottom.toDp() }, screenHeight/4.5f))
                )
            }
            is AuthState.Loading -> {
                CircularProgressIndicator()
            }
            is AuthState.Error -> {
                Text("Error: ${state.message}")
            }
            is AuthState.Data -> {
                Text("Logged in as ${state.user.name}")
            }
        }
    }
}

@Composable
fun ButtonIcon(loginEntered: Boolean) {
    Image(
        painter = painterResource(if(!loginEntered) {
            R.drawable.rounded_arrow_cool_down_24
        } else { R.drawable.rounded_check_24}),
        contentDescription = "Icon",
        modifier = Modifier
            .size(30.dp)
            .rotate(if(!loginEntered) {
                -90f
            } else { 0f }),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
    )
}
