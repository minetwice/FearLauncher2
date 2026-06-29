package com.fearlauncher.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fearlauncher.logic.VersionManager
import com.fearlauncher.ui.theme.*
import java.util.Locale

@Composable
fun DragonEpicDownloadBar(status: VersionManager.DownloadStatus) {
    val infiniteTransition = rememberInfiniteTransition(label = "dragon_flame")
    val flameOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BlackSurface.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                DragonHeadIcon()
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    status.fileName,
                    color = SilverAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1
                )
            }
            Text(
                "${(status.progress * 100).toInt()}%",
                color = SilverPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Dragon Path Progress
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(DeepBlack)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(status.progress)
                    .fillMaxHeight()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFFE25822), Color(0xFFF19CBB), SilverPrimary)
                        )
                    )
            )

            // Flame Particles Animation
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Drawing simple flame particles on the progress bar
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    String.format(Locale.US, "🔥 %.2f MB/s", status.speedMBs),
                    color = Color(0xFFE25822),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    String.format(Locale.US, "📦 %.1f / %.1f MB", status.downloadedMB, status.totalMB),
                    color = SilverDark,
                    fontSize = 10.sp
                )
            }
            Text(
                "⌛ ${formatETA(status.etaSeconds)}",
                color = SilverAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun DragonHeadIcon() {
    Canvas(modifier = Modifier.size(24.dp)) {
        val path = Path().apply {
            moveTo(size.width * 0.2f, size.height * 0.8f)
            lineTo(size.width * 0.8f, size.height * 0.8f)
            lineTo(size.width * 0.9f, size.height * 0.4f)
            lineTo(size.width * 0.7f, size.height * 0.2f)
            lineTo(size.width * 0.5f, size.height * 0.4f)
            lineTo(size.width * 0.3f, size.height * 0.2f)
            close()
        }
        drawPath(path, color = SilverPrimary, style = Fill)
        // Add eye
        drawCircle(Color.Red, radius = 2.dp.toPx(), center = androidx.compose.ui.geometry.Offset(size.width * 0.65f, size.height * 0.45f))
    }
}

private fun formatETA(seconds: Long): String {
    if (seconds <= 0) return "Ready"
    val mins = seconds / 60
    val secs = seconds % 60
    return if (mins > 0) "${mins}m ${secs}s" else "${secs}s"
}
