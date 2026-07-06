package com.yasinmoridi.composecropper.library.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import com.yasinmoridi.composecropper.library.crop.DefaultCropManager
import com.yasinmoridi.composecropper.library.model.CropResult
import com.yasinmoridi.composecropper.library.model.CropShape

/**
 * Main state class for the ImageCropper.
 */
@Stable
class CropperState(
    val zoomState: ZoomState = ZoomState(),
    val dragState: DragState = DragState(),
    initialShape: CropShape = CropShape.Rectangle
) {
    var cropShape by mutableStateOf(initialShape)

    var containerSize by mutableStateOf(Size.Zero)
        internal set

    var imageSize by mutableStateOf(Size.Zero)
        internal set

    var cropRect by mutableStateOf(Rect.Zero)
        internal set

    var aspectRatio by mutableStateOf<Float?>(null)

    /**
     * Resets the zoom and drag state.
     */
    fun reset() {
        zoomState.reset()
        dragState.reset()
    }

    suspend fun crop(image: ImageBitmap): CropResult {
        return DefaultCropManager().crop(image, this)
    }
}

/**
 * Remembers a [CropperState].
 */
@Composable
fun rememberCropperState(
    initialShape: CropShape = CropShape.Rectangle
): CropperState {
    return remember {
        CropperState(initialShape = initialShape)
    }
}
