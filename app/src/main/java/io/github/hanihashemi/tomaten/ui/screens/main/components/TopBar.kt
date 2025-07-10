package io.github.hanihashemi.tomaten.ui.screens.main.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.tomaten.Button
import io.github.hanihashemi.tomaten.ButtonStyles
import io.github.hanihashemi.tomaten.User
import io.github.hanihashemi.tomaten.theme.Dimens
import io.github.hanihashemi.tomaten.theme.TomatenTheme
import io.github.hanihashemi.tomaten.theme.Typography
import io.github.hanihashemi.tomaten.ui.actions.Actions
import io.github.hanihashemi.tomaten.ui.actions.previewActions
import io.github.hanihashemi.tomaten.ui.states.LoginUiState
import io.github.hanihashemi.tomaten.ui.states.UIState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun TopBar(actions: Actions, uiState: UIState) {
    val photoUrl = uiState.login.user?.photoUrl
    var showLogoutDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = Dimens.PaddingNormal),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Tomaten",
            style = Typography.titleLarge,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                text = uiState.login.userDisplayName ?: "Login",
                style = ButtonStyles.Secondary,
                modifier = Modifier.offset(x = Dimens.PaddingNormal)
            ) {
                if (!uiState.login.isLoggedIn) {
                    actions.login.displayDialog(true)
                } else {
                    showLogoutDialog = true
                }
            }

            if (!photoUrl.isNullOrEmpty()) {
                RemoteImage(
                    url = photoUrl,
                    modifier = Modifier
                        .padding(start = Dimens.PaddingSmall)
                        .size(24.dp)
                        .clip(CircleShape)
                )
            }
        }
    }

    if (showLogoutDialog) {
        LogoutConfirmDialog(
            onConfirm = {
                actions.login.logout()
                showLogoutDialog = false
            },
            onDismiss = {
                showLogoutDialog = false
            }
        )
    }
}

@Composable
fun LogoutConfirmDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Logout")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Confirm Logout") },
        text = { Text("Are you sure you want to logout?") }
    )
}

@Composable
private fun RemoteImage(url: String, modifier: Modifier = Modifier) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(url) {
        withContext(Dispatchers.IO) {
            val byteArray = HttpClient().get(url).body<ByteArray>()
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            imageBitmap = bitmap.asImageBitmap()
        }
    }

    imageBitmap?.let {
        Image(bitmap = it, contentDescription = null, modifier = modifier)
    }
}

@Preview(showBackground = true)
@Composable
private fun TopBarPreview() {
    TomatenTheme {
        TopBar(
            actions = previewActions,
            uiState = UIState(
                login = LoginUiState(
                    user = User(
                        name = "Radin",
                        photoUrl = "https://picsum.photos/200",
                        uid = "123"
                    )
                )
            )
        )
    }
}