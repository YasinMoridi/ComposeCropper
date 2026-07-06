package com.yasinmoridi.composecropper.library.internal

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.yasinmoridi.composecropper.library.model.Handle
import org.junit.Assert.assertEquals
import org.junit.Test

class RectUtilsTest {

    @Test
    fun `resizeRect TOP_LEFT move should update left and top`() {
        val initialRect = Rect(100f, 100f, 300f, 300f)
        val delta = Offset(10f, 20f)
        val result = resizeRect(initialRect, Handle.TOP_LEFT, delta, null)
        
        assertEquals(110f, result.left, 0.01f)
        assertEquals(120f, result.top, 0.01f)
        assertEquals(300f, result.right, 0.01f)
        assertEquals(300f, result.bottom, 0.01f)
    }

    @Test
    fun `resizeRect with Aspect Ratio 1 to 1 should maintain square shape`() {
        val initialRect = Rect(100f, 100f, 300f, 300f)
        val delta = Offset(50f, 0f) // Move only X
        val result = resizeRect(initialRect, Handle.BOTTOM_RIGHT, delta, 1.0f)
        
        // Width became 250, so height must also become 250
        assertEquals(250f, result.width, 0.01f)
        assertEquals(250f, result.height, 0.01f)
    }

    @Test
    fun `findActiveHandle should return correct handle within threshold`() {
        val rect = Rect(0f, 0f, 100f, 100f)
        val touchOffset = Offset(5f, 5f)
        val handle = findActiveHandle(touchOffset, rect, 20f)
        
        assertEquals(Handle.TOP_LEFT, handle)
    }

    @Test
    fun `findActiveHandle should return null outside threshold`() {
        val rect = Rect(0f, 0f, 100f, 100f)
        val touchOffset = Offset(50f, 50f)
        val handle = findActiveHandle(touchOffset, rect, 20f)
        
        assertEquals(null, handle)
    }
}
