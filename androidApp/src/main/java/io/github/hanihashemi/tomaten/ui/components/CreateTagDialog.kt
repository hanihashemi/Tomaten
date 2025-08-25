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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.hanihashemi.tomaten.data.model.Tag
import io.github.hanihashemi.tomaten.data.model.TagColor
import io.github.hanihashemi.tomaten.theme.TomatenTheme
import io.github.hanihashemi.tomaten.ui.actions.TagAction
import io.github.hanihashemi.tomaten.ui.actions.previewActions
import io.github.hanihashemi.tomaten.ui.states.TagUiState

@Composable
fun CreateTagDialog(
    tagState: TagUiState,
    tagAction: TagAction,
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
                    .testTag("create_tag_dialog"),
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
                // Title
                Text(
                    text = "New Tag",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.testTag("create_dialog_title"),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Tag name input
                OutlinedTextField(
                    value = tagState.createTagName,
                    onValueChange = { tagAction.updateCreateTagName(it) },
                    label = { Text("Tag name") },
                    isError = tagState.createTagNameError != null,
                    supportingText =
                        if (tagState.createTagNameError != null) {
                            { Text(tagState.createTagNameError) }
                        } else {
                            { Text("${tagState.createTagName.length}/${Tag.MAX_NAME_LENGTH}") }
                        },
                    singleLine = true,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .testTag("tag_name_input"),
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Color picker
                Text(
                    text = "Color",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.testTag("color_picker_label"),
                )

                Spacer(modifier = Modifier.height(12.dp))

                ColorPicker(
                    selectedColor = tagState.createTagColor,
                    onColorSelected = { tagAction.updateCreateTagColor(it) },
                    modifier = Modifier.testTag("color_picker"),
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("cancel_create_button"),
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    FilledTonalButton(
                        onClick = { tagAction.createTag() },
                        enabled = tagState.isCreateConfirmEnabled,
                        modifier = Modifier.testTag("confirm_create_button"),
                    ) {
                        Text("Create")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColorPicker(
    selectedColor: TagColor,
    onColorSelected: (TagColor) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TagColor.entries.forEach { color ->
            ColorOption(
                color = color,
                selected = color == selectedColor,
                onClick = { onColorSelected(color) },
            )
        }
    }
}

@Composable
private fun ColorOption(
    color: TagColor,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .size(40.dp)
                .selectable(
                    selected = selected,
                    onClick = onClick,
                    role = Role.RadioButton,
                )
                .semantics {
                    contentDescription =
                        if (selected) {
                            "Selected ${color.displayName} color"
                        } else {
                            "${color.displayName} color, tap to select"
                        }
                }
                .testTag("color_option_${color.name}"),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier.size(32.dp),
            shape = CircleShape,
            color = color.displayColor,
            shadowElevation = if (selected) 4.dp else 0.dp,
        ) {
            if (selected) {
                Box(
                    modifier = Modifier.size(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}

// Preview providers
class CreateTagDialogPreviewProvider : PreviewParameterProvider<TagUiState> {
    override val values =
        sequenceOf(
            TagUiState(
                isCreateDialogVisible = true,
                createTagName = "",
                createTagColor = TagColor.DEFAULT,
            ),
            TagUiState(
                isCreateDialogVisible = true,
                createTagName = "Work",
                createTagColor = TagColor.BLUE,
            ),
            TagUiState(
                isCreateDialogVisible = true,
                createTagName = "This name is too long for the field",
                createTagColor = TagColor.RED,
                createTagNameError = "Tag name must be 1-20 characters",
            ),
        )
}

@Preview(showBackground = true)
@Composable
private fun CreateTagDialogPreview(
    @PreviewParameter(CreateTagDialogPreviewProvider::class) tagState: TagUiState,
) {
    TomatenTheme {
        CreateTagDialog(
            tagState = tagState,
            tagAction = previewActions.tag,
            onDismiss = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ColorPickerPreview() {
    TomatenTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = "Color Picker",
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(16.dp))
                ColorPicker(
                    selectedColor = TagColor.BLUE,
                    onColorSelected = {},
                )
            }
        }
    }
}
