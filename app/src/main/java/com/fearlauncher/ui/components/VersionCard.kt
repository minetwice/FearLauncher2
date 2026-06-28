package com.fearlauncher.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fearlauncher.ui.theme.*

@Composable
fun VersionCard(versionName: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            SilverGloss.copy(alpha = 0.1f),
                            BlackSurface.copy(alpha = 0.9f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(SilverPrimary, SilverDark)
                            ),
                            RoundedCornerShape(8.dp)
                        )
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(versionName, color = SilverPrimary, fontWeight = FontWeight.SemiBold)
                    Text("Last played: Today", color = SilverDark, fontSize = 12.sp)
                }
                IconButton(
                    onClick = {},
                    modifier = Modifier.background(SilverPrimary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                ) {
                    Icon(Icons.Default.PlayArrow, "Play", tint = SilverPrimary)
                }
            }
        }
    }
}
