package io.github.hanihashemi.tomaten.ui.dialogs.profile

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.tomaten.Button
import io.github.hanihashemi.tomaten.ButtonStyles
import io.github.hanihashemi.tomaten.User
import io.github.hanihashemi.tomaten.theme.Dimens
import io.github.hanihashemi.tomaten.theme.TomatenTheme
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Suppress("FunctionName")
@Composable
fun UserProfileDialog(
    user: User,
    onDismiss: () -> Unit,
    onLogout: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingNormal),
            ) {
                // User Avatar
                if (!user.photoUrl.isNullOrEmpty()) {
                    RemoteImage(
                        url = user.photoUrl,
                        modifier =
                            Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                    )
                } else {
                    // Fallback avatar
                    Box(
                        modifier =
                            Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Default avatar",
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.PaddingSmall))

                // User Information Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(Dimens.PaddingNormal),
                        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall),
                    ) {
                        if (!user.name.isNullOrEmpty()) {
                            UserInfoRow(
                                label = "Name",
                                value = user.name,
                            )
                        }

                        if (!user.email.isNullOrEmpty()) {
                            UserInfoRow(
                                label = "Email",
                                value = user.email,
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall),
            ) {
                Button(
                    text = "Logout",
                    style = ButtonStyles.Primary,
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                )
                Button(
                    text = "Close",
                    style = ButtonStyles.Secondary,
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        dismissButton = {},
    )
}

@Composable
private fun UserInfoRow(
    label: String,
    value: String,
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun RemoteImage(
    url: String,
    modifier: Modifier = Modifier,
) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(url) {
        withContext(Dispatchers.IO) {
            try {
                val byteArray = HttpClient().get(url).body<ByteArray>()
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                imageBitmap = bitmap.asImageBitmap()
            } catch (e: Exception) {
                // Silently handle network errors when offline
                imageBitmap = null
            }
        }
    }

    imageBitmap?.let {
        Image(bitmap = it, contentDescription = "Profile image", modifier = modifier)
    } ?: run {
        // Fallback to default avatar if image fails to load
        Box(
            modifier =
                modifier
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Default avatar",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Suppress("FunctionName")
@Preview
@Composable
private fun UserProfileDialogPreview() {
    TomatenTheme {
        UserProfileDialog(
            user =
                User(
                    name = "John Doe",
                    email = "john.doe@example.com",
                    photoUrl = "https://picsum.photos/200",
                    uid = "123",
                ),
            onDismiss = {},
            onLogout = {},
        )
    }
}

@Suppress("FunctionName")
@Preview
@Composable
private fun UserProfileDialogNoImagePreview() {
    TomatenTheme {
        UserProfileDialog(
            user =
                User(
                    name = "Jane Smith",
                    email = "jane.smith@example.com",
                    photoUrl = null,
                    uid = "456",
                ),
            onDismiss = {},
            onLogout = {},
        )
    }
}
