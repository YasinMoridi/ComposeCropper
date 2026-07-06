package com.yasinmoridi.composecropper.library.model

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.runtime.Immutable

/**
 * The result of a crop operation.
 */
@Immutable
sealed interface CropResult {
    /**
     * Successfully cropped image.
     * @property bitmap The cropped [ImageBitmap].
     */
    data class Success(val bitmap: ImageBitmap) : CropResult

    /**
     * The crop operation failed.
     * @property error The exception that caused the failure.
     */
    data class Error(val error: Throwable) : CropResult

    /**
     * The crop operation was cancelled.
     */
    data object Cancelled : CropResult
}
