package com.bink.payments.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily

/**
 * UI options for the BinkPaymentsActivity.
 *
 * @param font: Font for the Bink payments UI.
 * @param backgroundColor: Colour used for the surface background.
 * @param toolBarOptions: UI options for the toolbar.
 * @param inputFieldOptions: Ui options for the input fields.
 */
data class BinkPaymentsOptions(
    val font: FontFamily = FontFamily.Default,
    val backgroundColor: Color = Color.White,
    val toolBarOptions: ToolBarOptions = ToolBarOptions(),
    val inputFieldOptions: InputFieldOptions = InputFieldOptions(),
) : java.io.Serializable

/**
 * UI options for the BinkPaymentsActivity.
 *
 * @param backButtonTitle: Text displayed to the right of the back button.
 * @param backButtonIcon: Icon used to display the back button.
 * @param toolBarTitle: Title displayed in the toolbar.
 * @param toolBarColor: Colour displayed on the toolbar.
 * @param toolbarTextColor: Colour used for text in the toolbar.
 */
data class ToolBarOptions(
    val backButtonTitle: String = "Back",
    val backButtonIcon: ImageVector = Icons.Filled.ArrowBack,
    val toolBarTitle: String = "Bink Payments",
    val toolBarColor: Color = Color.Blue,
    val toolbarTextColor: Color = Color.White,
) : java.io.Serializable

/**
 * UI options for the BinkPaymentsActivity.
 *
 * @param upperCaseHints: Toggle between upper case and lower case hints.
 * @param hintStyle: Options between a hint inline or a hint on the header.
 * @param hintTextColor: Text colour for the provided hint.
 * @param cursorColor: Colour displayed on an input cursor.
 * @param backgroundColor: Colour displayed on the background of an input field.
 * @param textColor: Text colour for the user input.
 * @param borderWidth: Size in dp of width of border.
 * @param borderColor: Colour displayed around the border of an input field.
 * @param borderStyle: Options between an underlined border or a box style border.
 */
data class InputFieldOptions(
    val upperCaseHints: Boolean = false,
    val hintStyle: InputFieldHintStyle = InputFieldHintStyle.HEADER,
    val hintTextColor: Color = Color.Gray,
    val cursorColor: Color = Color.Gray,
    val backgroundColor: Color = Color.White,
    val textColor: Color = Color.Black,
    val borderWidth: Int = 2,
    val borderColor: Color = Color.Gray,
    val borderStyle: InputFieldBorderStyle = InputFieldBorderStyle.BOX,
)

enum class InputFieldHintStyle {
    INLINE,
    HEADER
}

enum class InputFieldBorderStyle {
    BOX,
    UNDERLINE
}