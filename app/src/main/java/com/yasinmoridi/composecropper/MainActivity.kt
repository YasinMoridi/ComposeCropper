package com.yasinmoridi.composecropper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.yasinmoridi.composecropper.library.model.CropResult
import com.yasinmoridi.composecropper.library.model.CropShape
import com.yasinmoridi.composecropper.library.state.rememberCropperState
import com.yasinmoridi.composecropper.library.ui.ImageCropper
import com.yasinmoridi.composecropper.ui.theme.ComposeCropperTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import androidx.compose.ui.graphics.asAndroidBitmap
import android.widget.Toast

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeCropperTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DemoScreen()
                }
            }
        }
    }
}

@Composable
fun DemoScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var croppedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    val state = rememberCropperState(initialShape = CropShape.Rectangle)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            scope.launch {
                val bitmap = withContext(Dispatchers.IO) {
                    context.contentResolver.openInputStream(it)?.use { stream ->
                        BitmapFactory.decodeStream(stream)
                    }
                }
                selectedImage = bitmap?.asImageBitmap()
                state.reset()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "ComposeCropper Demo",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Select Image from Gallery")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(horizontal = 16.dp)
                .background(Color.DarkGray)
        ) {
            selectedImage?.let {
                ImageCropper(
                    image = it,
                    state = state,
                    modifier = Modifier.fillMaxSize()
                )
            } ?: Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No image selected", color = Color.LightGray)
            }
        }

        if (selectedImage != null) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Shape Controls
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Shape: ", style = MaterialTheme.typography.labelLarge)
                    TextButton(onClick = { state.cropShape = CropShape.Rectangle }) { Text("Rect") }
                    TextButton(onClick = { state.cropShape = CropShape.Circle }) { Text("Circle") }
                }

                // Aspect Ratio Controls
                Text("Aspect Ratio:", style = MaterialTheme.typography.labelLarge)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = { state.aspectRatio = null }) { Text("Free") }
                    TextButton(onClick = { state.aspectRatio = 1f }) { Text("1:1") }
                    TextButton(onClick = { state.aspectRatio = 4f / 3f }) { Text("4:3") }
                    TextButton(onClick = { state.aspectRatio = 16f / 9f }) { Text("16:9") }
                    TextButton(onClick = { state.aspectRatio = 9f / 16f }) { Text("9:16") }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        scope.launch {
                            selectedImage?.let { img ->
                                val result = state.crop(img)
                                if (result is CropResult.Success) {
                                    croppedImage = result.bitmap
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Crop Image")
                }

                croppedImage?.let { cropped ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            val uri = ExportUtils.saveToGallery(context, cropped.asAndroidBitmap(), "cropped_${System.currentTimeMillis()}")
                            if (uri != null) {
                                Toast.makeText(context, "Saved to Gallery", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Text("Save to Gallery")
                        }

                        Button(onClick = {
                            ExportUtils.shareImage(context, cropped.asAndroidBitmap(), "shared_crop")
                        }) {
                            Text("Share")
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Result Preview:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        bitmap = cropped,
                        contentDescription = "Cropped Result",
                        modifier = Modifier
                            .size(200.dp)
                            .background(Color.Black.copy(alpha = 0.1f))
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
