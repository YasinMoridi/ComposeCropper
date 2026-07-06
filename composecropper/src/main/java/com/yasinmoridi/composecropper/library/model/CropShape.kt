package com.yasinmoridi.composecropper.library.model

import androidx.compose.runtime.Immutable

/**
 * Defines the shape of the crop area.
 */
@Immutable
sealed interface CropShape {
    /**
     * A rectangular crop area.
     */
    data object Rectangle : CropShape

    /**
     * A circular crop area.
     */
    data object Circle : CropShape
}
