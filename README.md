# 📸 ComposeCropper

**Modern, Fast, and Highly Customizable Image Cropping for Jetpack Compose.**

<p align="center">
  <a href="https://kotlinlang.org"><img src="https://img.shields.io/badge/Kotlin-2.x-blue.svg" alt="Kotlin Version"></a>
  <a href="https://www.jetbrains.com/lp/compose-multiplatform/"><img src="https://img.shields.io/badge/Compose-Multiplatform-orange.svg" alt="Compose Version"></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/License-Apache%202.0-green.svg" alt="License"></a>
</p>

ComposeCropper is a production-ready Android library built from the ground up using Jetpack Compose. It provides a seamless image cropping experience with support for custom shapes, aspect ratios, and intuitive gestures.

---

## ✨ Features

- 🤏 **Pinch to Zoom**: Smooth multi-touch support for precise zooming.
- 🖐️ **Drag to Pan**: Fluid image movement within the cropper.
- 🔳 **Resizable Overlay**: Corner handles for intuitive crop area adjustment.
- 📐 **Aspect Ratio Support**: Lock to standard ratios (1:1, 16:9, etc.) or use Free mode.
- 🔴 **Shape Support**: Easily switch between Rectangle and Circle crop areas.
- ⚡ **Double Tap to Zoom**: Quick zoom-in and reset functionality.
- 🎨 **Fully Customizable**: Control colors, grid lines, and handle sizes.
- 🧩 **Clean API**: State hoisting for maximum control and integration.

---

## 🚀 Quick Start

### 1. Installation

Add JitPack to your `settings.gradle.kts`:
```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Add the dependency to your `build.gradle.kts`:
```kotlin
dependencies {
    implementation("com.github.yasinmoridi:ComposeCropper:1.0.0")
}
```

### 2. Basic Usage
```kotlin
val state = rememberCropperState()
val imageBitmap = ... // Load your ImageBitmap

ImageCropper(
    image = imageBitmap,
    state = state,
    modifier = Modifier.fillMaxSize()
)

// To get the result:
val scope = rememberCoroutineScope()
Button(onClick = {
    scope.launch {
        val result = state.crop(imageBitmap)
        if (result is CropResult.Success) {
            val cropped = result.bitmap
            // Use your cropped image!
        }
    }
}) { Text("Crop") }
```

---

## 🛠 Advanced Configuration

```kotlin
val state = rememberCropperState(
    initialShape = CropShape.Circle
)

// Lock to 16:9 aspect ratio
state.aspectRatio = 16f / 9f

ImageCropper(
    image = imageBitmap,
    state = state,
    overlayColor = Color.Black.copy(alpha = 0.7f),
    guideLineColor = Color.Cyan
)
```

---

## 🗺 Roadmap

- [ ] **Rotate & Flip**: Support for image rotation and mirroring.
- [ ] **Edge Detection**: Smart document detection using ML Kit.
- [ ] **Compose Multiplatform**: Support for Desktop and iOS.
- [ ] **Magnifier**: Loupe tool for pixel-perfect cropping.

---

## 🤝 Contributing
Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License
Distributed under the Apache 2.0 License. See `LICENSE` for more information.

---

<p align="center">
  <b>Built with ❤️ for the Android Community by Yasin Moridi.</b>
</p>
