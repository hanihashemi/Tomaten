package io.github.hanihashemi.tomaten.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.hanihashemi.tomaten.data.model.Tag
import io.github.hanihashemi.tomaten.ui.actions.TagAction
import io.github.hanihashemi.tomaten.ui.states.TagUiState

@Composable
fun SelectTagDialog(
    tagState: TagUiState,
    tagAction: TagAction,
    timerRunning: Boolean = false,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties =
            DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false,
            ),
    ) {
        Card(
            modifier =
                modifier
                    .padding(16.dp)
                    .testTag("select_tag_dialog"),
            shape = RoundedCornerShape(16.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
            ) {
                // Header with title and overflow menu
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Select Tag",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.testTag("dialog_title"),
                    )

                    var showOverflowMenu by remember { mutableStateOf(false) }
                    Box {
                        IconButton(
                            onClick = { showOverflowMenu = !showOverflowMenu },
                            modifier = Modifier.testTag("overflow_menu_button"),
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                            )
                        }

                        DropdownMenu(
                            expanded = showOverflowMenu,
                            onDismissRequest = { showOverflowMenu = false },
                        ) {
                            DropdownMenuItem(
                                text = { Text("Manage tags…") },
                                onClick = {
                                    showOverflowMenu = false
                                    tagAction.manageTags()
                                },
                                modifier = Modifier.testTag("manage_tags_menu_item"),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Timer running warning
                if (timerRunning) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors =
                            CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                            ),
                    ) {
                        Text(
                            text = "Tag can't be changed during a running session",
                            modifier =
                                Modifier
                                    .padding(16.dp)
                                    .testTag("timer_running_message"),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            textAlign = TextAlign.Center,
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // New Tag button
                FilledTonalButton(
                    onClick = { tagAction.showCreateDialog() },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .testTag("new_tag_button"),
                    enabled = !timerRunning,
                    colors =
                        androidx.compose.material3.ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "New Tag",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Tag grid
                TagGrid(
                    tags = tagState.tags,
                    selectedTagId = tagState.selectedTagId,
                    onTagSelected = { tagId -> tagAction.selectTag(tagId) },
                    enabled = !timerRunning,
                    modifier = Modifier.testTag("tag_grid"),
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("cancel_button"),
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    FilledTonalButton(
                        onClick = {
                            tagAction.confirmTagSelection()
                        },
                        enabled = tagState.isConfirmEnabled && !timerRunning,
                        modifier = Modifier.testTag("confirm_button"),
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }

    // Create Tag Dialog
    if (tagState.isCreateDialogVisible) {
        CreateTagDialog(
            tagState = tagState,
            tagAction = tagAction,
            onDismiss = { tagAction.hideCreateDialog() },
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TagGrid(
    tags: List<Tag>,
    selectedTagId: String?,
    onTagSelected: (String?) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            tags.forEach { tag ->
                TagChip(
                    tag = tag,
                    selected = tag.id == selectedTagId,
                    onClick = { onTagSelected(if (tag.id == selectedTagId) null else tag.id) },
                    enabled = enabled,
                )
            }
        }

        if (tags.isEmpty()) {
            Text(
                text = "No tags yet. Create your first tag!",
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                        .testTag("empty_state_message"),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun TagChip(
    tag: Tag,
    selected: Boolean,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    modifier =
                        Modifier
                            .size(8.dp)
                            .semantics {
                                contentDescription = "${tag.color.displayName} color"
                            },
                    shape = CircleShape,
                    color = if (selected) MaterialTheme.colorScheme.onPrimary else tag.color.displayColor,
                ) {}

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = tag.name,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        },
        enabled = enabled,
        colors =
            FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            ),
        modifier =
            modifier
                .testTag("tag_chip_${tag.id}")
                .semantics {
                    contentDescription =
                        if (selected) {
                            "Selected ${tag.name} tag"
                        } else {
                            "${tag.name} tag, tap to select"
                        }
                },
    )
}
