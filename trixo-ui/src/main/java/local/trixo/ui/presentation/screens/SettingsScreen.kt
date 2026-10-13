package local.trixo.ui.presentation.screens

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
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import local.trixo.ui.infrastructure.audio.LocalSoundManager
import local.trixo.ui.infrastructure.audio.SoundManager
import local.trixo.ui.presentation.components.NeonText
import local.trixo.ui.presentation.theme.AppTheme
import local.trixo.ui.presentation.theme.NeonCyan
import local.trixo.ui.presentation.theme.NeonMagenta
import local.trixo.ui.presentation.viewmodel.MainEvent
import local.trixo.ui.presentation.viewmodel.MainViewModel

@Composable
fun SettingsScreen(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by mainViewModel.uiState.collectAsState()
    val soundManager = LocalSoundManager.current

    SettingsScreenContent(
        soundEnabled = uiState.userPreferences.soundEnabled,
        onSoundEnabledChange = {
            soundManager.playTap()
            mainViewModel.onEvent(MainEvent.SetSoundEnabled(it))
        },
        modifier = modifier
    )
}

@Composable
fun SettingsScreenContent(
    soundEnabled: Boolean,
    onSoundEnabledChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
                        checked = soundEnabled,
                        onCheckedChange = onSoundEnabledChange,
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

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    CompositionLocalProvider(LocalSoundManager provides SoundManager(null)) {
        AppTheme {
            SettingsScreenContent(
                soundEnabled = true,
                onSoundEnabledChange = {}
            )
        }
    }
}
