package com.yasinmoridi.composecropper.library.crop

import androidx.compose.ui.graphics.ImageBitmap
import com.yasinmoridi.composecropper.library.model.CropResult
import com.yasinmoridi.composecropper.library.state.CropperState

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.geometry.Rect
import kotlin.math.roundToInt

/**
 * Interface for managing crop operations.
 */
interface CropManager {
    suspend fun crop(image: ImageBitmap, state: CropperState): CropResult
}

/**
 * Default implementation of [CropManager].
 */
internal class DefaultCropManager : CropManager {
    override suspend fun crop(image: ImageBitmap, state: CropperState): CropResult {
        return try {
            val bitmap = image.asAndroidBitmap()
            val viewWidth = state.containerSize.width
            val viewHeight = state.containerSize.height
            
            if (viewWidth <= 0 || viewHeight <= 0) return CropResult.Error(Exception("Invalid container size"))

            // 1. Calculate ContentScale.Fit scale and centering offsets
            val scale = minOf(viewWidth / image.width, viewHeight / image.height)
            val offsetX = (viewWidth - image.width * scale) / 2
            val offsetY = (viewHeight - image.height * scale) / 2

            // 2. Get user transformations
            val userScale = state.zoomState.zoom
            val tx = state.dragState.offset.x
            val ty = state.dragState.offset.y

            // 3. Map screen CropRect to Bitmap coordinates
            // Formula: u = ((sx - viewWidth/2 - tx) / z + viewWidth/2 - ox) / scale
            val cropRect = state.cropRect

            fun mapCoordinateX(sx: Float): Float {
                return ((sx - viewWidth / 2 - tx) / userScale + viewWidth / 2 - offsetX) / scale
            }

            fun mapCoordinateY(sy: Float): Float {
                return ((sy - viewHeight / 2 - ty) / userScale + viewHeight / 2 - offsetY) / scale
            }

            val left = mapCoordinateX(cropRect.left).roundToInt().coerceIn(0, image.width)
            val top = mapCoordinateY(cropRect.top).roundToInt().coerceIn(0, image.height)
            val right = mapCoordinateX(cropRect.right).roundToInt().coerceIn(0, image.width)
            val bottom = mapCoordinateY(cropRect.bottom).roundToInt().coerceIn(0, image.height)

            val width = (right - left).coerceIn(1, image.width - left)
            val height = (bottom - top).coerceIn(1, image.height - top)

            val croppedBitmap = Bitmap.createBitmap(bitmap, left, top, width, height)
            CropResult.Success(croppedBitmap.asImageBitmap())
        } catch (e: Exception) {
            CropResult.Error(e)
        }
    }
}
