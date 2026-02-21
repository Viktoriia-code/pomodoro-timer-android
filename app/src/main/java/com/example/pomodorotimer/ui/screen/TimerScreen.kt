package com.example.pomodorotimer.ui.screen

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

@Composable
fun TimerScreen(onNavigateToSettings: () -> Unit, onNavigateToHistory: () -> Unit) {

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

            TimerCircle()

            Spacer(modifier = Modifier.height(40.dp))

            PlayPauseButton()

        }
    }
}

@Composable
fun TimerCircle() {
    Box(
        contentAlignment = Alignment.Center
    ) {

        // Progress circle
        CircularProgressIndicator(
            progress = 0.65f,
            modifier = Modifier.size(300.dp),
            strokeWidth = 8.dp,
            color = Color(0xFF6EDCFF),
            trackColor = Color.DarkGray
        )

        // Earth image
        Image(
            painter = painterResource(id = R.drawable.earth),
            contentDescription = null,
            modifier = Modifier
                .size(350.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "25:00",
                fontSize = 48.sp,
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
fun PlayPauseButton() {

    Button(
        onClick = {},
        modifier = Modifier.size(80.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6EDCFF)
        )
    ) {

        Text(
            "▶",
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