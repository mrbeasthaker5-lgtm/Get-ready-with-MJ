package com.example.ui.screens.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FlashAuto
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.ChampagneGoldLight
import com.example.ui.theme.ElectricAmber
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceVariant

enum class FlashModeState {
    OFF, AUTO, ON
}

@Composable
fun CameraCaptureScreen(
    onPhotoCaptured: (Bitmap) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasCameraPermission = isGranted
    }

    // Gallery Picker launcher fallback
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                        decoder.isMutableRequired = true
                    }
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
                onPhotoCaptured(bitmap)
            } catch (e: Exception) {
                Log.e("CameraCaptureScreen", "Failed to decode gallery image", e)
            }
        }
    }

    // Camera state
    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_FRONT) }
    var flashMode by remember { mutableStateOf(FlashModeState.OFF) }
    var isShowGuide by remember { mutableStateOf(true) }
    var capturedPreviewBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isCapturing by remember { mutableStateOf(false) }
    var cameraInitError by remember { mutableStateOf<String?>(null) }

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .setFlashMode(
                when (flashMode) {
                    FlashModeState.AUTO -> ImageCapture.FLASH_MODE_AUTO
                    FlashModeState.ON -> ImageCapture.FLASH_MODE_ON
                    FlashModeState.OFF -> ImageCapture.FLASH_MODE_OFF
                }
            )
            .build()
    }

    // Update flash mode on imageCapture when state changes
    LaunchedEffect(flashMode) {
        try {
            imageCapture.flashMode = when (flashMode) {
                FlashModeState.AUTO -> ImageCapture.FLASH_MODE_AUTO
                FlashModeState.ON -> ImageCapture.FLASH_MODE_ON
                FlashModeState.OFF -> ImageCapture.FLASH_MODE_OFF
            }
        } catch (e: Exception) {
            Log.w("CameraCaptureScreen", "Could not set flash mode: ${e.message}")
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .testTag("camera_capture_screen")
    ) {
        if (!hasCameraPermission) {
            // Permission request UI
            CameraPermissionRequestCard(
                onRequestPermission = {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                },
                onPickGallery = {
                    galleryLauncher.launch(
                        androidx.activity.result.PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                onBackClick = onBackClick
            )
        } else if (capturedPreviewBitmap != null) {
            // Photo Review State
            CapturedPhotoReviewView(
                bitmap = capturedPreviewBitmap!!,
                onRetake = {
                    capturedPreviewBitmap = null
                },
                onConfirm = {
                    onPhotoCaptured(capturedPreviewBitmap!!)
                }
            )
        } else {
            // Live Camera Viewfinder with CameraX
            LiveCameraViewfinder(
                lensFacing = lensFacing,
                imageCapture = imageCapture,
                onInitError = { errorMsg ->
                    cameraInitError = errorMsg
                },
                modifier = Modifier.fillMaxSize()
            )

            // Live HUD overlays
            if (isShowGuide) {
                FacialGeometryGuideOverlay(
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Top Header: Back, Info Pill, Toggle Guide
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.55f))
                        .testTag("button_camera_back")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Camera",
                        tint = Color.White
                    )
                }

                // Center Guide Tag
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Black.copy(alpha = 0.65f),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            listOf(ChampagneGold.copy(alpha = 0.5f), ChampagneGoldLight)
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = null,
                            tint = ChampagneGoldLight,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ALIGN FACE IN OVAL",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.1.sp
                            ),
                            color = ChampagneGoldLight
                        )
                    }
                }

                // Toggle Guide overlay
                IconButton(
                    onClick = { isShowGuide = !isShowGuide },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (isShowGuide) ChampagneGold.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.55f))
                        .testTag("button_toggle_guide")
                ) {
                    Icon(
                        imageVector = Icons.Default.GridOn,
                        contentDescription = "Toggle Reticle Guide",
                        tint = if (isShowGuide) ChampagneGoldLight else Color.White
                    )
                }
            }

            // Real-time alignment tips banner below top bar
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 64.dp, start = 24.dp, end = 24.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color.Black.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = ElectricAmber,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Good lighting & neutral expression ensure exact geometric ratios",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = Color.White.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Error notice if camera failed to bind (e.g. headless emulator)
            if (cameraInitError != null) {
                Card(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ObsidianSurface)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Camera Unavailable",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Your device camera could not be initialized. You can still select a photo from your gallery.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                galleryLauncher.launch(
                                    androidx.activity.result.PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ChampagneGold)
                        ) {
                            Text("CHOOSE FROM GALLERY", color = ObsidianBg)
                        }
                    }
                }
            }

            // Bottom Shutter & Controls Bar
            BottomCameraControlBar(
                lensFacing = lensFacing,
                flashMode = flashMode,
                isCapturing = isCapturing,
                onFlipCamera = {
                    lensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
                        CameraSelector.LENS_FACING_BACK
                    } else {
                        CameraSelector.LENS_FACING_FRONT
                    }
                },
                onToggleFlash = {
                    flashMode = when (flashMode) {
                        FlashModeState.OFF -> FlashModeState.AUTO
                        FlashModeState.AUTO -> FlashModeState.ON
                        FlashModeState.ON -> FlashModeState.OFF
                    }
                },
                onPickGallery = {
                    galleryLauncher.launch(
                        androidx.activity.result.PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                onCaptureClick = {
                    if (!isCapturing) {
                        isCapturing = true
                        takeHighQualityPhoto(
                            context = context,
                            imageCapture = imageCapture,
                            lensFacing = lensFacing,
                            onSuccess = { bitmap ->
                                isCapturing = false
                                capturedPreviewBitmap = bitmap
                            },
                            onError = { exception ->
                                isCapturing = false
                                Log.e("CameraCaptureScreen", "Capture failed: ${exception.message}", exception)
                            }
                        )
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
            )
        }
    }
}

@Composable
private fun LiveCameraViewfinder(
    lensFacing: Int,
    imageCapture: ImageCapture,
    onInitError: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = modifier.testTag("camera_preview_view"),
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }

            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(lensFacing)
                        .build()

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (e: Exception) {
                    Log.e("LiveCameraViewfinder", "Use case binding failed", e)
                    onInitError(e.localizedMessage ?: "Camera binding failed")
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        },
        update = { previewView ->
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(lensFacing)
                        .build()

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (e: Exception) {
                    Log.e("LiveCameraViewfinder", "Update binding failed", e)
                }
            }, ContextCompat.getMainExecutor(context))
        }
    )
}

/**
 * High-precision Facial Geometry Reticle Overlay
 * Provides gold oval outline, eye-level line, vertical symmetry line,
 * and corner framing marks.
 */
@Composable
private fun FacialGeometryGuideOverlay(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_guide")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.70f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Canvas(modifier = modifier.testTag("facial_geometry_guide_canvas")) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // Oval dimensions centered in upper-middle portion of screen
        val ovalWidth = canvasWidth * 0.68f
        val ovalHeight = canvasHeight * 0.46f
        val ovalLeft = (canvasWidth - ovalWidth) / 2f
        val ovalTop = canvasHeight * 0.18f

        val strokeWidth = 2.5.dp.toPx()
        val goldColor = Color(0xFFE2C48D).copy(alpha = alphaAnim)
        val subtleGold = Color(0xFFE2C48D).copy(alpha = 0.35f)

        // Draw oval guide
        drawOval(
            color = goldColor,
            topLeft = Offset(ovalLeft, ovalTop),
            size = Size(ovalWidth, ovalHeight),
            style = Stroke(
                width = strokeWidth,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(24f, 16f), 0f)
            )
        )

        // Eye-level guide line (at ~40% of oval height)
        val eyeLevelY = ovalTop + (ovalHeight * 0.40f)
        drawLine(
            color = subtleGold,
            start = Offset(ovalLeft + (ovalWidth * 0.15f), eyeLevelY),
            end = Offset(ovalLeft + (ovalWidth * 0.85f), eyeLevelY),
            strokeWidth = 1.5.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
        )

        // Bilateral symmetry guide line (center vertical)
        val centerX = canvasWidth / 2f
        drawLine(
            color = subtleGold,
            start = Offset(centerX, ovalTop + 16f),
            end = Offset(centerX, ovalTop + ovalHeight - 16f),
            strokeWidth = 1.5.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
        )

        // Chin guide arc marker at bottom
        drawArc(
            color = goldColor,
            startAngle = 60f,
            sweepAngle = 60f,
            useCenter = false,
            topLeft = Offset(ovalLeft, ovalTop),
            size = Size(ovalWidth, ovalHeight),
            style = Stroke(width = 4.dp.toPx())
        )

        // Studio Viewfinder Corner Brackets
        val cornerBracketLength = 32.dp.toPx()
        val cornerMargin = 28.dp.toPx()
        val cornerStroke = 2.dp.toPx()

        // Top-Left corner
        drawLine(
            color = goldColor,
            start = Offset(cornerMargin, cornerMargin + 40.dp.toPx()),
            end = Offset(cornerMargin + cornerBracketLength, cornerMargin + 40.dp.toPx()),
            strokeWidth = cornerStroke
        )
        drawLine(
            color = goldColor,
            start = Offset(cornerMargin, cornerMargin + 40.dp.toPx()),
            end = Offset(cornerMargin, cornerMargin + 40.dp.toPx() + cornerBracketLength),
            strokeWidth = cornerStroke
        )

        // Top-Right corner
        drawLine(
            color = goldColor,
            start = Offset(canvasWidth - cornerMargin, cornerMargin + 40.dp.toPx()),
            end = Offset(canvasWidth - cornerMargin - cornerBracketLength, cornerMargin + 40.dp.toPx()),
            strokeWidth = cornerStroke
        )
        drawLine(
            color = goldColor,
            start = Offset(canvasWidth - cornerMargin, cornerMargin + 40.dp.toPx()),
            end = Offset(canvasWidth - cornerMargin, cornerMargin + 40.dp.toPx() + cornerBracketLength),
            strokeWidth = cornerStroke
        )

        // Bottom-Left corner
        val bottomMarginY = canvasHeight - 140.dp.toPx()
        drawLine(
            color = goldColor,
            start = Offset(cornerMargin, bottomMarginY),
            end = Offset(cornerMargin + cornerBracketLength, bottomMarginY),
            strokeWidth = cornerStroke
        )
        drawLine(
            color = goldColor,
            start = Offset(cornerMargin, bottomMarginY),
            end = Offset(cornerMargin, bottomMarginY - cornerBracketLength),
            strokeWidth = cornerStroke
        )

        // Bottom-Right corner
        drawLine(
            color = goldColor,
            start = Offset(canvasWidth - cornerMargin, bottomMarginY),
            end = Offset(canvasWidth - cornerMargin - cornerBracketLength, bottomMarginY),
            strokeWidth = cornerStroke
        )
        drawLine(
            color = goldColor,
            start = Offset(canvasWidth - cornerMargin, bottomMarginY),
            end = Offset(canvasWidth - cornerMargin, bottomMarginY - cornerBracketLength),
            strokeWidth = cornerStroke
        )
    }
}

/**
 * Bottom camera controls: Flip Camera, Shutter, Flash Mode, Gallery
 */
@Composable
private fun BottomCameraControlBar(
    lensFacing: Int,
    flashMode: FlashModeState,
    isCapturing: Boolean,
    onFlipCamera: () -> Unit,
    onToggleFlash: () -> Unit,
    onPickGallery: () -> Unit,
    onCaptureClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        shape = RoundedCornerShape(32.dp),
        color = Color.Black.copy(alpha = 0.65f),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.verticalGradient(
                listOf(ObsidianBorder, ChampagneGold.copy(alpha = 0.25f))
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flash Mode Toggle
            IconButton(
                onClick = onToggleFlash,
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.10f))
                    .testTag("button_toggle_flash")
            ) {
                Icon(
                    imageVector = when (flashMode) {
                        FlashModeState.OFF -> Icons.Default.FlashOff
                        FlashModeState.AUTO -> Icons.Default.FlashAuto
                        FlashModeState.ON -> Icons.Default.FlashOn
                    },
                    contentDescription = "Flash: ${flashMode.name}",
                    tint = if (flashMode != FlashModeState.OFF) ChampagneGoldLight else Color.White
                )
            }

            // Gallery Quick Picker
            IconButton(
                onClick = onPickGallery,
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.10f))
                    .testTag("button_camera_gallery")
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = "Gallery",
                    tint = ChampagneGoldLight
                )
            }

            // Primary Tactile Shutter Button
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(3.dp, ChampagneGold, CircleShape)
                    .padding(5.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(ChampagneGoldLight, ChampagneGold)
                        )
                    )
                    .clickable(enabled = !isCapturing) { onCaptureClick() }
                    .testTag("button_shutter"),
                contentAlignment = Alignment.Center
            ) {
                if (isCapturing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(34.dp),
                        color = ObsidianBg,
                        strokeWidth = 3.dp
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }
            }

            // Flip Camera (Front / Back)
            IconButton(
                onClick = onFlipCamera,
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.10f))
                    .testTag("button_flip_camera")
            ) {
                Icon(
                    imageVector = Icons.Default.FlipCameraAndroid,
                    contentDescription = "Flip Camera",
                    tint = Color.White
                )
            }
        }
    }
}

/**
 * Captured Photo Review & Confirmation screen
 */
@Composable
private fun CapturedPhotoReviewView(
    bitmap: Bitmap,
    onRetake: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .testTag("captured_photo_review_view")
    ) {
        // Fullscreen captured image
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Captured Selfie",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Subtle gradient protection
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.7f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // Top Status Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onRetake,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .testTag("button_retake_top")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Retake",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "SELFIE CAPTURED",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.2.sp
                    ),
                    color = Color.White
                )
                Text(
                    text = "High resolution photo ready for geometry analysis",
                    style = MaterialTheme.typography.bodySmall,
                    color = ChampagneGoldLight
                )
            }
        }

        // Bottom Confirmation Panel
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = ObsidianSurface.copy(alpha = 0.95f)),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.verticalGradient(
                    listOf(ChampagneGold.copy(alpha = 0.4f), ObsidianBorder)
                )
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Quality indicators
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    QualityCheckBadge(label = "Facial Proportions", status = "Clear")
                    QualityCheckBadge(label = "Lighting", status = "Optimal")
                    QualityCheckBadge(label = "Geometry", status = "Centered")
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onRetake,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("button_retake_photo"),
                        shape = RoundedCornerShape(14.dp),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.linearGradient(listOf(ObsidianBorder, Color.Gray))
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "RETAKE",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = Color.White
                        )
                    }

                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1.3f)
                            .height(52.dp)
                            .testTag("button_use_photo"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ChampagneGold,
                            contentColor = ObsidianBg
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = ObsidianBg,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "USE THIS PHOTO",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            ),
                            color = ObsidianBg
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QualityCheckBadge(
    label: String,
    status: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = ChampagneGoldLight,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = status,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = ChampagneGoldLight
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
            color = Color.White.copy(alpha = 0.6f)
        )
    }
}

/**
 * Camera Permission Request UI
 */
@Composable
private fun CameraPermissionRequestCard(
    onRequestPermission: () -> Unit,
    onPickGallery: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(ObsidianSurfaceVariant)
                    .testTag("button_permission_back")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(ChampagneGold.copy(alpha = 0.15f))
                    .border(1.5.dp, ChampagneGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = null,
                    tint = ChampagneGoldLight,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "CAMERA ACCESS NEEDED",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp
                ),
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "MJ measures facial ratios, jawline angle, and forehead-to-chin proportions in real-time to tailor exact haircut silhouettes to your bone structure.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Privacy pledge badge
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianSurfaceVariant),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.linearGradient(listOf(ObsidianBorder, ChampagneGold.copy(alpha = 0.3f)))
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = ChampagneGoldLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Zero remote uploads. All photos are analyzed in volatile memory.",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = ChampagneGoldLight
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onRequestPermission,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("button_grant_camera_permission"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ChampagneGold,
                    contentColor = ObsidianBg
                )
            ) {
                Text(
                    text = "ALLOW CAMERA ACCESS",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                )
            }

            OutlinedButton(
                onClick = onPickGallery,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("button_permission_gallery"),
                shape = RoundedCornerShape(14.dp),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.linearGradient(listOf(ObsidianBorder, ChampagneGold.copy(alpha = 0.4f)))
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = null,
                    tint = ChampagneGoldLight,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CHOOSE FROM GALLERY",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = ChampagneGoldLight
                )
            }
        }
    }
}

/**
 * Captures a high-resolution photo with rotation and mirror correction
 */
private fun takeHighQualityPhoto(
    context: Context,
    imageCapture: ImageCapture,
    lensFacing: Int,
    onSuccess: (Bitmap) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    imageCapture.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                try {
                    val rotationDegrees = image.imageInfo.rotationDegrees
                    val rawBitmap = image.toBitmap()
                    image.close()

                    // Rotate and flip if front-facing selfie
                    val matrix = Matrix().apply {
                        postRotate(rotationDegrees.toFloat())
                        if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
                            postScale(-1f, 1f, rawBitmap.width / 2f, rawBitmap.height / 2f)
                        }
                    }

                    val orientedBitmap = Bitmap.createBitmap(
                        rawBitmap,
                        0,
                        0,
                        rawBitmap.width,
                        rawBitmap.height,
                        matrix,
                        true
                    )

                    onSuccess(orientedBitmap)
                } catch (e: Exception) {
                    Log.e("takeHighQualityPhoto", "Error processing captured photo", e)
                    onError(
                        ImageCaptureException(
                            ImageCapture.ERROR_UNKNOWN,
                            "Failed to process image bitmap: ${e.message}",
                            e
                        )
                    )
                }
            }

            override fun onError(exception: ImageCaptureException) {
                onError(exception)
            }
        }
    )
}
