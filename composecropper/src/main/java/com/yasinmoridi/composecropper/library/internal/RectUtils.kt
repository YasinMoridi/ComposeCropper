package com.yasinmoridi.composecropper.library.internal

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.yasinmoridi.composecropper.library.model.Handle

internal fun findActiveHandle(offset: Offset, rect: Rect, threshold: Float): Handle? {
    if ((offset - rect.topLeft).getDistance() < threshold) return Handle.TOP_LEFT
    if ((offset - rect.topRight).getDistance() < threshold) return Handle.TOP_RIGHT
    if ((offset - rect.bottomLeft).getDistance() < threshold) return Handle.BOTTOM_LEFT
    if ((offset - rect.bottomRight).getDistance() < threshold) return Handle.BOTTOM_RIGHT
    return null
}

internal fun resizeRect(rect: Rect, handle: Handle, delta: Offset, aspectRatio: Float?): Rect {
    var left = rect.left
    var top = rect.top
    var right = rect.right
    var bottom = rect.bottom

    when (handle) {
        Handle.TOP_LEFT -> {
            left += delta.x
            top += delta.y
        }
        Handle.TOP_RIGHT -> {
            right += delta.x
            top += delta.y
        }
        Handle.BOTTOM_LEFT -> {
            left += delta.x
            bottom += delta.y
        }
        Handle.BOTTOM_RIGHT -> {
            right += delta.x
            bottom += delta.y
        }
    }

    var newRect = Rect(left, top, right, bottom)

    // Apply Aspect Ratio if set
    aspectRatio?.let { ratio ->
        val currentWidth = newRect.width
        val currentHeight = newRect.height
        val targetHeight = currentWidth / ratio
        
        newRect = when (handle) {
            Handle.TOP_LEFT, Handle.TOP_RIGHT -> newRect.copy(top = newRect.bottom - targetHeight)
            Handle.BOTTOM_LEFT, Handle.BOTTOM_RIGHT -> newRect.copy(bottom = newRect.top + targetHeight)
        }
    }

    // Minimum size constraint
    if (newRect.width < 100f || newRect.height < 100f) return rect

    return newRect
}
