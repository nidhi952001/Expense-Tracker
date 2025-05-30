package com.example.expensetracker.ui.theme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp


val primaryLight = Color(0xFF415F91)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFFD6E3FF)
val onPrimaryContainerLight = Color(0xFF284777)
val secondaryLight = Color(0xFF565F71)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFDAE2F9)
val onSecondaryContainerLight = Color(0xFF3E4759)
val tertiaryLight = Color(0xFF705575)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFFAD8FD)
val onTertiaryContainerLight = Color(0xFF573E5C)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF93000A)
val backgroundLight = Color(0xFFF9F9FF)
val onBackgroundLight = Color(0xFF191C20)
val surfaceLight = Color(0xFFF9F9FF)
val onSurfaceLight = Color(0xFF191C20)
val surfaceVariantLight = Color(0xFFE0E2EC)
val onSurfaceVariantLight = Color(0xFF44474E)
val outlineLight = Color(0xFF74777F)
val outlineVariantLight = Color(0xFFC4C6D0)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF2E3036)
val inverseOnSurfaceLight = Color(0xFFF0F0F7)
val inversePrimaryLight = Color(0xFFAAC7FF)


val primaryDark = Color(0xFFAAC7FF)
val onPrimaryDark = Color(0xFF0A305F)
val primaryContainerDark = Color(0xFF284777)
val onPrimaryContainerDark = Color(0xFFD6E3FF)
val secondaryDark = Color(0xFFBEC6DC)
val onSecondaryDark = Color(0xFF283141)
val secondaryContainerDark = Color(0xFF3E4759)
val onSecondaryContainerDark = Color(0xFFDAE2F9)
val tertiaryDark = Color(0xFFDDBCE0)
val onTertiaryDark = Color(0xFF3F2844)
val tertiaryContainerDark = Color(0xFF573E5C)
val onTertiaryContainerDark = Color(0xFFFAD8FD)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF111318)
val onBackgroundDark = Color(0xFFE2E2E9)
val surfaceDark = Color(0xFF111318)
val onSurfaceDark = Color(0xFFE2E2E9)
val surfaceVariantDark = Color(0xFF44474E)
val onSurfaceVariantDark = Color(0xFFC4C6D0)
val outlineDark = Color(0xFF8E9099)
val outlineVariantDark = Color(0xFF44474E)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFE2E2E9)
val inverseOnSurfaceDark = Color(0xFF2E3036)
val inversePrimaryDark = Color(0xFF415F91)


object AppColors {
    val surface: Color
        @Composable get() = MaterialTheme.colorScheme.surface

    val onSurface: Color
        @Composable get() = MaterialTheme.colorScheme.onSurface

    val surfaceVariant: Color
        @Composable get() = MaterialTheme.colorScheme.surfaceVariant

    val inverseSurface: Color
        @Composable get() = MaterialTheme.colorScheme.inverseSurface

    val inverseOnSurface: Color
        @Composable get() = MaterialTheme.colorScheme.inverseOnSurface

    val outlineVariant: Color
        @Composable get() = MaterialTheme.colorScheme.outlineVariant

    val onBackground: Color
        @Composable get() = MaterialTheme.colorScheme.onBackground

    val inversePrimary: Color
        @Composable get() = MaterialTheme.colorScheme.inversePrimary

    val primaryContainer: Color
        @Composable get() = MaterialTheme.colorScheme.primaryContainer

    val onPrimaryContainer: Color
        @Composable get() = MaterialTheme.colorScheme.onPrimaryContainer

    val onPrimary:Color
        @Composable get() = MaterialTheme.colorScheme.onPrimary

    val secondaryContainer: Color
        @Composable get() = MaterialTheme.colorScheme.secondaryContainer

    val secondary: Color
        @Composable get() = MaterialTheme.colorScheme.secondary

    val errorColor: Color
        @Composable get() = MaterialTheme.colorScheme.error

    val onError: Color
        @Composable get() = MaterialTheme.colorScheme.onError

    val inputFieldShape:RoundedCornerShape
        @Composable get() = RoundedCornerShape(10.dp)

    val inputFieldBackgroundColors:Color
        @Composable get() = outlineVariant.copy(alpha = 0.4f)

    val inputTextWeight:FontWeight
        @Composable get() = FontWeight.Normal

    val inputTextSize:TextUnit
        @Composable get() = MaterialTheme.typography.titleSmall.fontSize

    val inputTextStyle: FontStyle?
        @Composable get() = MaterialTheme.typography.titleSmall.fontStyle
}


