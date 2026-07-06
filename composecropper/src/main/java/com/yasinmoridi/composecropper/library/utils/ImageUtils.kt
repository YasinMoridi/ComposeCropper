package com.yasinmoridi.composecropper.library.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.Bitmap

/**
 * Utility functions for image processing.
 */
object ImageUtils {
    /**
     * Resizes a bitmap.
     */
    fun resize(bitmap: ImageBitmap, width: Int, height: Int): ImageBitmap {
        val androidBitmap = bitmap.asAndroidBitmap()
        return Bitmap.createScaledBitmap(androidBitmap, width, height, true).asImageBitmap()
    }
}
