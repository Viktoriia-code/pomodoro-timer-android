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

@Composable
fun TimerScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToHistory: () -> Unit,
    viewModel: TimerViewModel = viewModel()
    ) {
    val timeLeft = viewModel.timeLeft.collectAsState()
    val isRunning = viewModel.isRunning.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "FOCUS",
                color = Color(0xFF6EDCFF),
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            TimerCircle(
                time = viewModel.formatTime(timeLeft.value)
            )

            Spacer(modifier = Modifier.height(40.dp))

            PlayPauseButton(
                isRunning = isRunning.value,
                onClick = { viewModel.startPauseTimer() }
            )

        }
    }
}

@Composable
fun TimerCircle(time: String) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        // Earth image
        Image(
            painter = painterResource(id = R.drawable.earth),
            contentDescription = null,
            modifier = Modifier
                .size(350.dp)
                .clip(CircleShape)
                .padding(top = 36.dp),
            contentScale = ContentScale.Crop,
            alpha = 0.8F
        )

        // Progress circle
        CircularProgressIndicator(
            progress = 0.65f,
            modifier = Modifier.size(300.dp),
            strokeWidth = 8.dp,
            color = Color(0xFF6EDCFF),
            trackColor = Color.DarkGray
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = time,
                fontSize = 64.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Focus Time",
                color = Color(0xFF6EDCFF)
            )
        }
    }
}

@Composable
fun PlayPauseButton(
    isRunning: Boolean,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        modifier = Modifier.size(80.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6EDCFF)
        )
    ) {

        Text(
            if (isRunning) "⏸" else "▶",
            fontSize = 28.sp,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimerScreenPreview() {
    TimerScreen(
        onNavigateToSettings = {},
        onNavigateToHistory = {}
    )
}