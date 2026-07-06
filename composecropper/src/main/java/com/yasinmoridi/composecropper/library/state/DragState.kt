package com.yasinmoridi.composecropper.library.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.runtime.Stable

/**
 * State representing the drag offset of the image.
 */
@Stable
class DragState(initialOffset: Offset = Offset.Zero) {
    var offset by mutableStateOf(initialOffset)
        private set

    fun updateOffset(delta: Offset) {
        // We can add logic here to limit dragging based on zoom and image size
        // For now, let's just update the offset
        offset += delta
    }

    fun snapTo(newOffset: Offset) {
        offset = newOffset
    }

    fun reset() {
        offset = Offset.Zero
    }
}
