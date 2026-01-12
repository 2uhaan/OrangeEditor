package com.ruhaan.orangeeditor.util

sealed interface BottomBarMode {
    object Primary : BottomBarMode
    object TextLayerSelected : BottomBarMode
    object ImageLayerSelected : BottomBarMode
}
