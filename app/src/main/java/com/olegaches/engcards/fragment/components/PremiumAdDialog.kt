package com.olegaches.engcards.fragment.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.olegaches.engcards.R

@Composable
fun PremiumAdDialog(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(
                text = title
            )
        },
        text = {
            Text(
                stringResource(R.string.premium_ad_text)
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onConfirm
            ) {
                Text(
                    text = stringResource(R.string.buy)
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(
                    text = stringResource(R.string.decline)
                )
            }
        }
    )
}