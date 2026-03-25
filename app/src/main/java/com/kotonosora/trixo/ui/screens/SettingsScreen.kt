package com.kotonosora.trixo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kotonosora.trixo.audio.LocalSoundManager
import com.kotonosora.trixo.ui.MainViewModel
import com.kotonosora.trixo.ui.components.NeonIconButton
import com.kotonosora.trixo.ui.components.NeonText
import com.kotonosora.trixo.ui.components.NeonTitle
import com.kotonosora.trixo.ui.theme.NeonCyan
import com.kotonosora.trixo.ui.theme.NeonMagenta
import com.kotonosora.trixo.ui.theme.NeonYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    mainViewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userPreferences by mainViewModel.userPreferences.collectAsState()
    val soundManager = LocalSoundManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { NeonTitle("OPTIONS", fontSize = 24) },
                navigationIcon = {
                    NeonIconButton(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        onClick = onNavigateBack,
                        tint = NeonCyan
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Preferences Neon Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                .background(NeonCyan.copy(alpha = 0.05f))
                .border(2.dp, NeonCyan, RoundedCornerShape(24.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.AutoMirrored.Rounded.VolumeUp,
                            contentDescription = "Sound",
                            tint = NeonMagenta
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        NeonText("SOUND FX", color = NeonMagenta, fontWeight = FontWeight.Bold)
                    }
                    Switch(
                        checked = userPreferences.soundEnabled,
                        onCheckedChange = {
                            soundManager.playTap()
                            mainViewModel.setSoundEnabled(it)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NeonCyan,
                            checkedTrackColor = NeonCyan.copy(alpha = 0.5f),
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color.DarkGray
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.MusicNote,
                            contentDescription = "Music",
                            tint = NeonYellow
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        NeonText("MUSIC", color = NeonYellow, fontWeight = FontWeight.Bold)
                    }
                    Switch(
                        checked = userPreferences.musicEnabled,
                        onCheckedChange = {
                            soundManager.playTap()
                            mainViewModel.setMusicEnabled(it)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NeonCyan,
                            checkedTrackColor = NeonCyan.copy(alpha = 0.5f),
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color.DarkGray
                        )
                    )
                }
            }
        }
    }
}
}
