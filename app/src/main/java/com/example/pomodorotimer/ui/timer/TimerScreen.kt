package com.example.pomodorotimer.ui.timer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pomodorotimer.R
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.History

@ExperimentalMaterial3Api
@Composable
fun TimerScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToHistory: () -> Unit,
    viewModel: TimerViewModel = viewModel()
) {

    val timeLeft = viewModel.timeLeft.collectAsState()
    val isRunning = viewModel.isRunning.collectAsState()
    val progress by viewModel.progress.collectAsState()

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("Pomodoro Timer")
                },

                actions = {

                    IconButton(
                        onClick = onNavigateToHistory
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "History"
                        )
                    }

                    IconButton(
                        onClick = onNavigateToSettings
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        }

    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "FOCUS",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp
                )

                Text(
                    text = "Stay focused on your task",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                TimerCircle(
                    time = viewModel.formatTime(timeLeft.value),
                    isRunning = isRunning.value,
                    progress = progress
                )

                Spacer(modifier = Modifier.height(40.dp))

                Controls(
                    isRunning = isRunning.value,
                    onPlayPauseClick = { viewModel.startPauseTimer() },
                    onResetClick = { viewModel.resetTimer() },
                    onFinishSessionClick = { viewModel.finishSession() }
                )
            }
        }
    }
}

@Composable
fun TimerCircle(
    time: String,
    isRunning: Boolean,
    progress: Float
) {

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = ""
    )

    Box(
        contentAlignment = Alignment.Center
    ) {

        // ===== Earth =====
        Image(
            painter            = painterResource(id = R.drawable.earth),
            contentDescription = null,
            modifier           = Modifier
                .size(350.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            alpha        = 0.85f
        )

        // ===== Progress ring with real glow =====
        Canvas(modifier = Modifier.size(300.dp)) {
            val stroke   = 8.dp.toPx()
            val diameter = size.minDimension
            val topLeft  = Offset(
                (size.width - diameter) / 2,
                (size.height - diameter) / 2
            )
            val arcSize = Size(diameter, diameter)

            // ── Track (background ring) ──────────────────────────
            drawArc(
                color      = Color.DarkGray,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter  = false,
                topLeft    = topLeft,
                size       = arcSize,
                style      = Stroke(width = stroke, cap = StrokeCap.Butt)
            )

            // ── Glow layer 1 — wide soft blur ────────────────────
            // KEY: BlurMaskFilter creates real GPU blur
            val glowPaintWide = android.graphics.Paint().apply {
                isAntiAlias = true
                style       = android.graphics.Paint.Style.STROKE
                strokeWidth = stroke * 6f  // very wide
                strokeCap   = android.graphics.Paint.Cap.BUTT
                color       = android.graphics.Color.argb(80, 110, 220, 255)
                maskFilter  = android.graphics.BlurMaskFilter(
                    stroke * 5f,                             // blur radius
                    android.graphics.BlurMaskFilter.Blur.NORMAL // blur type
                )
            }

            // ── Glow layer 2 — medium blur ───────────────────────
            val glowPaintMedium = android.graphics.Paint().apply {
                isAntiAlias = true
                style       = android.graphics.Paint.Style.STROKE
                strokeWidth = stroke * 3f
                strokeCap   = android.graphics.Paint.Cap.BUTT
                color       = android.graphics.Color.argb(140, 41, 182, 246)
                maskFilter  = android.graphics.BlurMaskFilter(
                    stroke * 3f,
                    android.graphics.BlurMaskFilter.Blur.NORMAL
                )
            }

            // Draw glow layers using native canvas
            // IMPORTANT: drawContext.canvas.nativeCanvas gives access to blur
            val nativeCanvas = drawContext.canvas.nativeCanvas
            val oval = android.graphics.RectF(
                topLeft.x,
                topLeft.y,
                topLeft.x + arcSize.width,
                topLeft.y + arcSize.height
            )
            val sweepAngle = 360f * animatedProgress

            // Draw wide glow
            nativeCanvas.drawArc(oval, -90f, sweepAngle, false, glowPaintWide)
            // Draw medium glow
            nativeCanvas.drawArc(oval, -90f, sweepAngle, false, glowPaintMedium)

            // ── Main progress line (sharp, on top) ───────────────
            val progressBrush = Brush.sweepGradient(
                colors = listOf(
                    Color(0xFF00E5FF), // bright cyan — start point (top)
                    Color(0xFF0288D1), // deep blue
                    Color(0xFF1565C0), // darker blue — bottom
                    Color(0xFF0288D1), // back to blue
                    Color(0xFF00E5FF)  // bright cyan — full circle
                ),
                center = Offset(size.width / 2, size.height / 2)
            )

            drawArc(
                brush      = progressBrush,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter  = false,
                topLeft    = topLeft,
                size       = arcSize,
                style      = Stroke(width = stroke, cap = StrokeCap.Butt)
            )
        }

        // ===== Time text =====
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = time,
                fontSize = 64.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 2.sp,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.8f),
                        offset = Offset(0f, 0f),   // centered — no direction
                        blurRadius = 30f           // spread of the shadow
                    )
                )
            )

            Text(
                text = if (isRunning) "Running..." else "Ready?",
                color = Color(0xFF6EDCFF)
            )
        }
    }
}

@Composable
fun Controls(
    isRunning: Boolean,
    onPlayPauseClick: () -> Unit,
    onResetClick: () -> Unit,
    onFinishSessionClick: () -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Button(
            onClick = onResetClick,
            modifier = Modifier.size(56.dp),
            contentPadding = PaddingValues(0.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFB0BEC5)
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Reset",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }

        Button(
            onClick = onPlayPauseClick,
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0288D1)
            )
        ) {

            Icon(
                imageVector =
                    if (isRunning)
                        Icons.Filled.Pause
                    else
                        Icons.Filled.PlayArrow,

                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(34.dp)
            )
        }

        // Finish session button
        Button(
            onClick = onFinishSessionClick,
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFB0BEC5)
            )
        ) {
            Icon(
                imageVector = Icons.Filled.FastForward,
                contentDescription = "Finish session",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun TimerScreenPreview() {
    TimerScreen(
        onNavigateToSettings = {},
        onNavigateToHistory = {}
    )
}