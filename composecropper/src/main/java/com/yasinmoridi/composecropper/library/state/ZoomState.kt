package com.yasinmoridi.composecropper.library.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Stable

/**
 * State representing the zoom level of the image.
 */
@Stable
class ZoomState(
    initialZoom: Float = 1f,
    val minZoom: Float = 1f,
    val maxZoom: Float = 5f
) {
    var zoom by mutableFloatStateOf(initialZoom.coerceIn(minZoom, maxZoom))
        private set

    fun updateZoom(newZoom: Float) {
        zoom = newZoom.coerceIn(minZoom, maxZoom)
    }

    fun reset() {
        zoom = 1f
    }
}
