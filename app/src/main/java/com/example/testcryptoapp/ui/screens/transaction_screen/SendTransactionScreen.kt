@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.testcryptoapp.ui.screens.transaction_screen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.testcryptoapp.ui.components.PrimaryButton
import com.example.testcryptoapp.ui.theme.ErrorColor
import com.example.testcryptoapp.ui.theme.SuccessColor
import com.example.testcryptoapp.ui.theme.TextColorSecondary
import com.example.testcryptoapp.ui_models.SendTxUiState
import com.example.testcryptoapp.ui_models.TxStatusUi

@Composable
fun SendTransactionScreen(
    state: SendTxUiState,
    onBack: () -> Unit,
    onRecipientChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onDismissStatus: () -> Unit = {},
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
    ) {
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = state.recipient,
            onValueChange = onRecipientChange,
            label = { Text("Recipient Address") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(14.dp))

        OutlinedTextField(
            value = state.amountEth,
            onValueChange = onAmountChange,
            label = { Text("Amount (ETH)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(Modifier.height(18.dp))

        val isLoading = state.status is TxStatusUi.Loading
        val canSend = state.recipient.isNotBlank() && state.amountEth.isNotBlank() && !isLoading
        PrimaryButton(
            enabled = canSend,
            onClick = onSendClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp
                )
                Spacer(Modifier.width(10.dp))
                Text("Sending…")
            } else {
                Text("Send Transaction")
            }
        }

        Spacer(Modifier.height(18.dp))

        when (val st = state.status) {
            TxStatusUi.Idle -> Unit

            TxStatusUi.Loading -> {
                StatusCardLoading(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            is TxStatusUi.Success -> {
                StatusCardSuccess(
                    txHash = st.txHash,
                    etherscanUrl = st.etherscanUrl,
                    onCopyHash = {
                        copyToClipboard(context, "Transaction Hash", st.txHash)
                    },
                    onOpenEtherscan = {
                        openUrl(context, st.etherscanUrl)
                    },
                    onDismiss = onDismissStatus,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            is TxStatusUi.Error -> {
                StatusCardError(
                    message = st.message,
                    onDismiss = onDismissStatus,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun StatusCardLoading(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Sending...", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Please wait while we submit your transaction.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun StatusCardSuccess(
    txHash: String,
    etherscanUrl: String,
    onCopyHash: () -> Unit,
    onOpenEtherscan: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = SuccessColor.copy(alpha = 0.5f)
    val container = SuccessColor.copy(alpha = 0.08f)

    Card(
        modifier = modifier.border(1.dp, borderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = container),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = SuccessColor
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    "Transaction Success!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.weight(1f))
                TextButton(onClick = onDismiss, contentPadding = PaddingValues(0.dp)) {
                    Text("OK")
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                "Transaction Hash:",
                style = MaterialTheme.typography.bodyMedium,
                color = TextColorSecondary
            )

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 1.dp,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = shortenHash(txHash),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(Modifier.width(10.dp))
                FilledTonalIconButton(onClick = onCopyHash) {
                    Icon(Icons.Outlined.ContentCopy, contentDescription = "Copy")
                }
            }

            Spacer(Modifier.height(10.dp))

            TextButton(
                onClick = onOpenEtherscan,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("View on Etherscan")
            }
        }
    }
}

@Composable
private fun StatusCardError(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = ErrorColor.copy(alpha = 0.5f)
    val container = ErrorColor.copy(alpha = 0.08f)

    Card(
        modifier = modifier.border(1.dp, borderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = container),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.ErrorOutline,
                    contentDescription = null,
                    tint = ErrorColor
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    "Transaction Failed",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.weight(1f))
                TextButton(onClick = onDismiss, contentPadding = PaddingValues(0.dp)) {
                    Text("Dismiss")
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun shortenHash(hash: String): String {
    val h = hash.trim()
    if (h.length <= 14) return h
    return "${h.take(8)}...${h.takeLast(6)}"
}

private fun copyToClipboard(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
}

private fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    context.startActivity(intent)
}
