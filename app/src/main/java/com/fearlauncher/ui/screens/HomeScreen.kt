package com.fearlauncher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Person
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
import com.fearlauncher.ui.components.VersionCard
import com.fearlauncher.ui.theme.*

@Composable
fun HomeScreen(username: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            "FEAR LAUNCHER",
            style = MaterialTheme.typography.headlineMedium,
            color = SilverAccent,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))
        Card(
            modifier = Modifier.fillMaxWidth().height(180.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = BlackSurface.copy(alpha = 0.8f))
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(20.dp)
            ) {
                Column {
                    Text("Welcome back,", color = SilverDark, fontSize = 14.sp)
                    Text("$username!", color = SilverPrimary, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Ready to explore?", color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.7f))
                }
                Button(
                    onClick = { },
                    modifier = Modifier.align(Alignment.BottomEnd).width(140.dp).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SilverPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, "Play", tint = androidx.compose.ui.graphics.Color.Black)
                    Spacer(Modifier.width(8.dp))
                    Text("PLAY NOW", color = androidx.compose.ui.graphics.Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Skin Section
        Text("Your Character", color = SilverAccent, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = BlackSurface.copy(alpha = 0.8f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(DeepBlack, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, "Skin", tint = SilverDark, modifier = Modifier.size(48.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Current Skin", color = SilverPrimary, fontWeight = FontWeight.Bold)
                    Text("Default Alex", color = SilverDark, fontSize = 12.sp)
                }
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = SilverPrimary.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.FileUpload, "Upload", tint = SilverPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Change", color = SilverPrimary, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Recent Installations", color = SilverAccent, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(listOf("1.20.4 Vanilla", "1.19.2 Modded", "1.20.1 SMP")) { version ->
                VersionCard(versionName = version)
            }
        }
    }
}
