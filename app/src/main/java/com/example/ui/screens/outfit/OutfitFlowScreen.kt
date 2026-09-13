package com.example.ui.screens.outfit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BodyProportion
import com.example.data.model.GenderStylePreference
import com.example.data.model.HeightInput
import com.example.data.model.Occasion
import com.example.data.model.StylePreferenceCategory
import com.example.data.model.WeatherCondition
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.ChampagneGoldLight
import com.example.ui.theme.ElectricAmber
import com.example.ui.theme.GenZLilac
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurfaceVariant

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OutfitFlowScreen(
    currentProfile: GenderStylePreference,
    onProfileSelected: (GenderStylePreference) -> Unit,
    currentHeight: HeightInput,
    onHeightChanged: (HeightInput) -> Unit,
    currentProportion: BodyProportion,
    onProportionSelected: (BodyProportion) -> Unit,
    selectedStyles: Set<StylePreferenceCategory>,
    onToggleStyle: (StylePreferenceCategory) -> Unit,
    selectedOccasion: Occasion,
    onOccasionSelected: (Occasion) -> Unit,
    selectedWeather: WeatherCondition,
    onWeatherSelected: (WeatherCondition) -> Unit,
    onGenerateClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var heightUnitMode by remember { mutableStateOf("CM") } // "CM" or "FT"
    var cmInputText by remember { mutableStateOf(currentHeight.heightCm.toInt().toString()) }
    var ftInputText by remember { mutableStateOf(currentHeight.feetAndInches.first.toString()) }
    var inInputText by remember { mutableStateOf(currentHeight.feetAndInches.second.toString()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("outfit_flow_screen")
    ) {
        // Top Back Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "GEN-Z OUTFIT FINDER",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 1. Style Profile
        Text(
            text = "1. CHOOSE YOUR STYLE PROFILE",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            ),
            color = GenZLilac
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            GenderStylePreference.values().forEach { pref ->
                val isSelected = currentProfile == pref
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .border(
                            1.dp,
                            if (isSelected) GenZLilac else ObsidianBorder,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { onProfileSelected(pref) }
                        .testTag("outfit_profile_${pref.name.lowercase()}"),
                    color = if (isSelected) GenZLilac.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = pref.displayName.uppercase(),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium
                            ),
                            color = if (isSelected) GenZLilac else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Height
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "2. HEIGHT",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                ),
                color = GenZLilac
            )
            // CM vs FT/IN Toggle
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(2.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = if (heightUnitMode == "CM") GenZLilac else Color.Transparent,
                    modifier = Modifier.clickable { heightUnitMode = "CM" }
                ) {
                    Text(
                        text = "CM",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (heightUnitMode == "CM") Color(0xFF1E1430) else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = if (heightUnitMode == "FT") GenZLilac else Color.Transparent,
                    modifier = Modifier.clickable { heightUnitMode = "FT" }
                ) {
                    Text(
                        text = "FT / IN",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (heightUnitMode == "FT") Color(0xFF1E1430) else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (heightUnitMode == "CM") {
            OutlinedTextField(
                value = cmInputText,
                onValueChange = {
                    cmInputText = it
                    it.toFloatOrNull()?.let { cm -> onHeightChanged(HeightInput.fromCm(cm)) }
                },
                trailingIcon = { Text("cm", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_height_cm"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GenZLilac,
                    unfocusedBorderColor = ObsidianBorder
                )
            )
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = ftInputText,
                    onValueChange = {
                        ftInputText = it
                        val ft = it.toIntOrNull() ?: 5
                        val inc = inInputText.toIntOrNull() ?: 9
                        onHeightChanged(HeightInput.fromFeetInches(ft, inc))
                    },
                    label = { Text("Feet") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_height_ft"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GenZLilac,
                        unfocusedBorderColor = ObsidianBorder
                    )
                )
                OutlinedTextField(
                    value = inInputText,
                    onValueChange = {
                        inInputText = it
                        val ft = ftInputText.toIntOrNull() ?: 5
                        val inc = it.toIntOrNull() ?: 9
                        onHeightChanged(HeightInput.fromFeetInches(ft, inc))
                    },
                    label = { Text("Inches") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_height_in"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GenZLilac,
                        unfocusedBorderColor = ObsidianBorder
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Body Proportion (Optional & Respectful)
        Text(
            text = "3. BODY PROPORTION (OPTIONAL)",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            ),
            color = GenZLilac
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Used solely to balance clothing draping and hem levels. Every body proportion is celebrated.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(10.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            BodyProportion.values().forEach { prop ->
                val isSelected = currentProportion == prop
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            1.dp,
                            if (isSelected) GenZLilac else ObsidianBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onProportionSelected(prop) }
                        .testTag("proportion_${prop.name.lowercase()}"),
                    color = if (isSelected) GenZLilac.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
                ) {
                    Text(
                        text = prop.displayName,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = if (isSelected) GenZLilac else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Style Preferences (Multi-select)
        Text(
            text = "4. STYLE PREFERENCES (SELECT MULTIPLE)",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            ),
            color = GenZLilac
        )
        Spacer(modifier = Modifier.height(10.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            StylePreferenceCategory.values().forEach { cat ->
                val isSelected = selectedStyles.contains(cat)
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .border(
                            1.dp,
                            if (isSelected) GenZLilac else ObsidianBorder,
                            RoundedCornerShape(100.dp)
                        )
                        .clickable { onToggleStyle(cat) }
                        .testTag("style_chip_${cat.name.lowercase()}"),
                    color = if (isSelected) GenZLilac.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = GenZLilac,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            text = cat.displayName,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isSelected) GenZLilac else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 5. Occasion
        Text(
            text = "5. OCCASION",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            ),
            color = GenZLilac
        )
        Spacer(modifier = Modifier.height(10.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Occasion.values().forEach { occ ->
                val isSelected = selectedOccasion == occ
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            1.dp,
                            if (isSelected) ChampagneGold else ObsidianBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onOccasionSelected(occ) }
                        .testTag("occasion_${occ.name.lowercase()}"),
                    color = if (isSelected) ChampagneGold.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
                ) {
                    Text(
                        text = occ.displayName,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = if (isSelected) ChampagneGoldLight else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 6. Weather
        Text(
            text = "6. TODAY'S WEATHER",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            ),
            color = GenZLilac
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WeatherCondition.values().forEach { weather ->
                val isSelected = selectedWeather == weather
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .border(
                            1.dp,
                            if (isSelected) ElectricAmber else ObsidianBorder,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { onWeatherSelected(weather) }
                        .testTag("weather_${weather.name.lowercase()}"),
                    color = if (isSelected) ElectricAmber.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = weather.emoji, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = weather.name,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 10.sp
                            ),
                            color = if (isSelected) ElectricAmber else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Submit Button
        Button(
            onClick = onGenerateClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("button_find_my_style_submit"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GenZLilac,
                contentColor = Color(0xFF1E1430)
            )
        ) {
            Text(
                text = "FIND MY TOP 6 LOOKS",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
