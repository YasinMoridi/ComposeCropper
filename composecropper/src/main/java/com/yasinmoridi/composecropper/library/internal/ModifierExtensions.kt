package com.yasinmoridi.composecropper.library.internal

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.yasinmoridi.composecropper.library.state.CropperState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Internal modifier for handling zoom and drag gestures.
 */
internal fun Modifier.cropperGestures(
    state: CropperState,
    enabled: Boolean = true
): Modifier = if (enabled) {
    this.pointerInput(Unit) {
        coroutineScope {
            launch {
                detectTransformGestures { _, pan, zoom, _ ->
                    state.zoomState.updateZoom(state.zoomState.zoom * zoom)
                    state.dragState.updateOffset(pan)
                }
            }
            launch {
                detectTapGestures(
                    onDoubleTap = {
                        if (state.zoomState.zoom > 1f) {
                            state.reset()
                        } else {
                            state.zoomState.updateZoom(2.5f)
                        }
                    }
                )
            }
        }
    }
} else {
    this
}
