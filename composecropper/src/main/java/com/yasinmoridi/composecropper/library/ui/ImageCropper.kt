package com.yasinmoridi.composecropper.library.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInput
import com.yasinmoridi.composecropper.library.internal.cropperGestures
import com.yasinmoridi.composecropper.library.internal.findActiveHandle
import com.yasinmoridi.composecropper.library.internal.resizeRect
import com.yasinmoridi.composecropper.library.model.CropShape
import com.yasinmoridi.composecropper.library.model.Handle
import com.yasinmoridi.composecropper.library.state.CropperState
import com.yasinmoridi.composecropper.library.state.rememberCropperState

import androidx.compose.ui.draw.clipToBounds

/**
 * A composable that provides image cropping functionality.
 */
@Composable
fun ImageCropper(
    image: ImageBitmap,
    modifier: Modifier = Modifier,
    state: CropperState = rememberCropperState(),
    overlayColor: Color = Color.Black.copy(alpha = 0.5f),
    guideLineColor: Color = Color.White
) {
    // Initial rect calculation
    LaunchedEffect(state.containerSize) {
        if (state.containerSize.width > 0 && state.cropRect == Rect.Zero) {
            val size = state.containerSize * 0.8f
            val left = (state.containerSize.width - size.width) / 2
            val top = (state.containerSize.height - size.height) / 2
            state.cropRect = Rect(Offset(left, top), size)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds() // This prevents the image from bleeding out of the container
            .background(Color.Black)
            .onGloballyPositioned {
                state.containerSize = it.size.toSize()
            }
            .cropperGestures(state)
    ) {
        Image(
            bitmap = image,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = state.zoomState.zoom,
                    scaleY = state.zoomState.zoom,
                    translationX = state.dragState.offset.x,
                    translationY = state.dragState.offset.y,
                    clip = true
                )
        )

        // Overlay with resizing logic
        CropperOverlay(
            state = state,
            overlayColor = overlayColor,
            guideLineColor = guideLineColor
        )
    }
}

@Composable
private fun CropperOverlay(
    state: CropperState,
    overlayColor: Color,
    guideLineColor: Color
) {
    val rect = state.cropRect
    var activeHandle by remember { mutableStateOf<Handle?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(state.containerSize) {
                detectDragGestures(
                    onDragStart = { offset ->
                        activeHandle = findActiveHandle(offset, state.cropRect, 40.dp.toPx())
                    },
                    onDrag = { change, dragAmount ->
                        val currentHandle = activeHandle
                        if (currentHandle != null) {
                            state.cropRect = resizeRect(state.cropRect, currentHandle, dragAmount, state.aspectRatio)
                        } else {
                            state.cropRect = state.cropRect.translate(dragAmount)
                        }
                        change.consume()
                    },
                    onDragEnd = { activeHandle = null }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 2.dp.toPx()
            
            // Draw outside overlay
            drawPath(
                path = Path().apply {
                    addRect(Rect(Offset.Zero, size))
                    when (state.cropShape) {
                        is CropShape.Rectangle -> addRect(rect)
                        is CropShape.Circle -> addOval(rect)
                    }
                    fillType = PathFillType.EvenOdd
                },
                color = overlayColor
            )

            // Draw crop area border
            if (state.cropShape is CropShape.Rectangle) {
                drawRect(
                    color = guideLineColor,
                    topLeft = rect.topLeft,
                    size = rect.size,
                    style = Stroke(width = strokeWidth)
                )
                
                // Grid lines (Rule of Thirds)
                val tw = rect.width / 3
                val th = rect.height / 3
                for (i in 1..2) {
                    drawLine(guideLineColor, rect.topLeft.copy(x = rect.left + i * tw), rect.bottomLeft.copy(x = rect.left + i * tw), 1f)
                    drawLine(guideLineColor, rect.topLeft.copy(y = rect.top + i * th), rect.topRight.copy(y = rect.top + i * th), 1f)
                }
            } else {
                drawOval(
                    color = guideLineColor,
                    topLeft = rect.topLeft,
                    size = rect.size,
                    style = Stroke(width = strokeWidth)
                )
            }
        }
        
        // Handles (Visual)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val handleColor = guideLineColor
            val radius = 8.dp.toPx()
            
            drawCircle(handleColor, radius, rect.topLeft)
            drawCircle(handleColor, radius, rect.topRight)
            drawCircle(handleColor, radius, rect.bottomLeft)
            drawCircle(handleColor, radius, rect.bottomRight)
        }
    }
}
