package io.github.hanihashemi.tomaten.ui.screens.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.tomaten.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("FunctionName")
@Composable
fun SettingsScreen(
    appName: String = "Tomaten",
    appVersion: String = "1.0.0",
    appImagePainter: Painter? = null,
    @DrawableRes appLogoRes: Int? = null,
    onWishlistClick: () -> Unit = {},
    onFeedbackClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = Dimens.PaddingNormal),
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingLarge),
        ) {
            item {
                Spacer(modifier = Modifier.padding(top = Dimens.PaddingSmall))
            }

            // Welcome Section
            item {
                WelcomeSection()
            }

            // Subscription Status
            item {
                SubscriptionSection()
            }

            // Support Section
            item {
                SupportSection(
                    onWishlistClick = onWishlistClick,
                    onFeedbackClick = onFeedbackClick,
                )
            }

            // About Us Section
            item {
                AboutSection(
                    appVersion = appVersion,
                    onAboutClick = onAboutClick,
                )
            }

            // Footer
            item {
                AppFooter(
                    appName = appName,
                    appImagePainter = appImagePainter,
                    appLogoRes = appLogoRes,
                )
            }

            item {
                Spacer(modifier = Modifier.padding(bottom = Dimens.PaddingLarge))
            }
        }
    }
}

@Composable
private fun WelcomeSection() {
    Column {
        SettingsRow(
            title = "Daily Motivation",
            subtitle = "What I do today is important because I am exchanging a day of my life for it.",
            onClick = { /* No action needed for welcome message */ },
        )
    }
}

@Composable
private fun SubscriptionSection() {
    Column {
        SectionHeader(text = "Subscription")

        SettingsRow(
            title = "Subscription status",
            subtitle = "Tap to view plans",
            onClick = { },
        )
    }
}

@Composable
private fun SupportSection(
    onWishlistClick: () -> Unit,
    onFeedbackClick: () -> Unit,
) {
    Column {
        SectionHeader(text = "Support")

        SettingsRow(
            title = "New feature wishlist",
            subtitle = "Tell us the features you want, they might come true",
            onClick = onWishlistClick,
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = Dimens.PaddingNormal),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
        )

        SettingsRow(
            title = "Send feedback",
            subtitle = "Write to us and tell your feedback",
            onClick = onFeedbackClick,
        )
    }
}

@Composable
private fun AboutSection(
    appVersion: String,
    onAboutClick: () -> Unit,
) {
    Column {
        SectionHeader(text = "About Us")

        SettingsRow(
            title = "App version",
            subtitle = "Your valuable feedback will help us to make our product better",
            trailingText = appVersion,
            onClick = onAboutClick,
        )
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Bold,
        modifier =
            Modifier.padding(
                horizontal = Dimens.PaddingNormal,
                vertical = Dimens.PaddingSmall,
            ),
    )
}

@Composable
private fun SettingsRow(
    title: String,
    subtitle: String? = null,
    trailingText: String? = null,
    showArrow: Boolean = false,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(Dimens.PaddingNormal),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
            )

            if (subtitle != null) {
                Spacer(modifier = Modifier.padding(top = Dimens.PaddingXXXSmall))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        if (trailingText != null) {
            Text(
                text = trailingText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.width(Dimens.PaddingSmall))
        }

        if (showArrow) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun AppFooter(
    appName: String,
    appImagePainter: Painter?,
    @DrawableRes appLogoRes: Int?,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(Dimens.PaddingXLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // App Logo
        when {
            appImagePainter != null -> {
                Image(
                    painter = appImagePainter,
                    contentDescription = appName,
                    modifier = Modifier.size(64.dp),
                )
            }
            appLogoRes != null -> {
                Image(
                    painter = painterResource(id = appLogoRes),
                    contentDescription = appName,
                    modifier = Modifier.size(64.dp),
                )
            }
        }

        Spacer(modifier = Modifier.padding(top = Dimens.PaddingSmall))

        // App Name
        Text(
            text = appName,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
        )
    }
}
