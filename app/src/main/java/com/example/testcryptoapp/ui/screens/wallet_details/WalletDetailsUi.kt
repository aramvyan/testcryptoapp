@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.testcryptoapp.ui.screens.wallet_details

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowRightAlt
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.testcryptoapp.ui.components.PrimaryButton
import com.example.testcryptoapp.ui.theme.DisabledColor
import com.example.testcryptoapp.ui.theme.ErrorColor
import com.example.testcryptoapp.ui.theme.PrimaryColor
import com.example.testcryptoapp.ui.theme.TextColor

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun WalletDetailsScreen(
    onBack: () -> Unit,
    onSendTransaction: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WalletDetailsViewModel
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uiState = state.ui

    val pullState = rememberPullToRefreshState()
    PullToRefreshBox(
        state = pullState,
        modifier = Modifier.fillMaxSize(),
        isRefreshing = state.isRefreshing,
        onRefresh = { viewModel.refresh() },
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(12.dp))

            uiState?.let {
                WalletCard(
                    chainLabel = uiState.chainLabel,
                    address = it.address,
                    networkLine = uiState.networkName,
                    balanceEth = uiState.balanceEth,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(14.dp))

            ActionRow(
                icon = { Icon(Icons.Outlined.ContentCopy, contentDescription = null) },
                title = "Copy Address",
                onClick = {
                    copyToClipboard(context, "Wallet Address", uiState?.address)
                }
            )

            Spacer(Modifier.height(14.dp))

            PrimaryButton(
                onClick = onSendTransaction,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
            ) {
                Icon(
                    Icons.Outlined.ArrowRightAlt, contentDescription = null,
                    modifier = Modifier.fillMaxHeight(),
                    tint = Color.White
                )
                Spacer(Modifier.width(10.dp))
                Text("Send Transaction", color = Color.White)
            }

            Spacer(Modifier.height(14.dp))

            ActionRow(
                icon = {
                    Icon(
                        Icons.Outlined.ExitToApp,
                        contentDescription = null,
                        tint = ErrorColor
                    )
                },
                title = "Logout",
                titleColor = ErrorColor,
                onClick = onLogout
            )
            Spacer(Modifier.height(40.dp))
        }
    }
}


@Composable
private fun WalletCard(
    chainLabel: String,
    address: String,
    networkLine: String,
    balanceEth: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(
                text = chainLabel,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryColor,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(PrimaryColor.copy(alpha = 0.3f))
                    .padding(8.dp)
            )

            Spacer(Modifier.height(12.dp))

            Text("Address", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 1.dp,
                color = DisabledColor.copy(alpha = 0.2f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = address,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = DisabledColor.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = 12.dp)
            )

            Text("Current Network", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            Text(
                text = networkLine,
                style = MaterialTheme.typography.bodyLarge
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = DisabledColor.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = 12.dp)
            )


            Text("Balance", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            Text(
                text = "$balanceEth ETH",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = TextColor
            )
        }
    }
}

@Composable
private fun ActionRow(
    icon: @Composable () -> Unit,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    titleColor: Color = TextColor
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            Spacer(Modifier.width(12.dp))
            Text(
                text = title,
                color = titleColor,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Outlined.KeyboardArrowRight, contentDescription = null)
        }
    }
}

private fun copyToClipboard(context: Context, label: String, text: String? = "") {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
}
