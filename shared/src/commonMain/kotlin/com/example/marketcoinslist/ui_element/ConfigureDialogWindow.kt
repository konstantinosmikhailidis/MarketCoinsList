package com.example.marketcoinslist.ui_element

import androidx.compose.runtime.Composable

@Composable
expect fun ConfigureDialogWindow(position: Position)

enum class Position{
    CENTER,
    BOTTOM
}