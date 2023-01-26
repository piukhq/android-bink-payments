package com.bink.payments.screens

import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.FontRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.bink.payments.*
import kotlinx.parcelize.Parcelize

/**
 * UI options for the BinkPaymentsActivity.
 *
 * @param font: Font for the Bink payments UI.
 * @param backgroundColor: Colour used for the surface background.
 * @param toolBarOptions: UI options for the toolbar.
 * @param inputFieldOptions: Ui options for the input fields.
 */
@Parcelize
data class BinkPaymentsOptions(
    val font: BinkTypography = BinkTypography(),
    @ColorInt
    val backgroundColor: Int = Color(0xFFFFFFFF).toArgb(),
    val toolBarOptions: ToolBarOptions = ToolBarOptions(),
    val binkPaymentsTheme: BinkPaymentsTheme = BinkPaymentsTheme(),
    val inputFieldOptions: InputFieldOptions = InputFieldOptions(),
) : Parcelable

/**
 * UI options for the BinkPaymentsActivity.
 *
 * @param primaryColor: Primary Colour for toolbar and buttons.
 * @param secondaryColor: Secondary Colour for text and checkboxes.
 */
@Parcelize
data class BinkPaymentsTheme(
    val primaryColor: Int = Color.Blue.toArgb(),
    val secondaryColor: Int = Color.White.toArgb(),
    val uncheckedColor: Int = Color.Black.toArgb(),
) : Parcelable

/**
 * UI options for the BinkPaymentsActivity.
 *
 * @param backButtonTitle: Text displayed to the right of the back button.
 * @param backButtonIcon: Icon used to display the back button.
 * @param toolBarTitle: Title displayed in the toolbar.
 */
@Parcelize
data class ToolBarOptions(
    val backButtonTitle: String = "Back",
    val backButtonIcon: Int = R.drawable.ic_baseline_arrow_back_24,
    val toolBarTitle: String = "Bink Payments",
) : Parcelable

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
@Parcelize
data class InputFieldOptions(
    val upperCaseHints: Boolean = false,
    val hintStyle: InputFieldHintStyle = InputFieldHintStyle.HEADER,
    val hintTextColor: Int = Color.Gray.toArgb(),
    val cursorColor: Int = Color.Gray.toArgb(),
    val backgroundColor: Int = Color.White.toArgb(),
    val textColor: Int = Color.Black.toArgb(),
    val borderWidth: Int = 2,
    val borderColor: Int = Color.Gray.toArgb(),
    val borderStyle: InputFieldBorderStyle = InputFieldBorderStyle.BOX,
    val checkBoxStyle: CheckBoxStyle = CheckBoxStyle.SWITCH,
) : Parcelable

enum class InputFieldHintStyle {
    INLINE,
    HEADER
}

enum class InputFieldBorderStyle {
    BOX,
    UNDERLINE
}

enum class CheckBoxStyle {
    BOX,
    SWITCH
}

@Parcelize
data class BinkTypography(
    /**
     * The font used in text. This should be a resource ID value.
     */
    @FontRes
    val fontResId: Int? = null,
    /**
     * Fonts from the compose FontFamily
     */
    val fontName: String = "",
) : Parcelable {

    fun fontFamily(): FontFamily {
        return if (fontResId != null) {
            return FontFamily(Font(fontResId))
        } else baseFonts()
    }

    private fun baseFonts(): FontFamily {
        return when (fontName) {
            SANS_SERIF -> FontFamily.SansSerif
            SERIF -> FontFamily.Serif
            MONOSPACE -> FontFamily.Monospace
            CURSIVE -> FontFamily.Cursive
            else -> FontFamily.Default
        }
    }

}