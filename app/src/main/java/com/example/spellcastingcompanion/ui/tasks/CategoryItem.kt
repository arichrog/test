package com.example.spellcastingcompanion.ui.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Star // Placeholder for 'sparkles', 'sun'
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Eco // Placeholder for 'leaf'
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.spellcastingcompanion.data.model.Category
import com.example.spellcastingcompanion.ui.theme.Charm // Your custom font
import com.example.spellcastingcompanion.ui.theme.MagicArcane
import com.example.spellcastingcompanion.ui.theme.MagicGreen
import com.example.spellcastingcompanion.ui.theme.MagicRed
import com.example.spellcastingcompanion.ui.theme.MagicYellow
import com.example.spellcastingcompanion.ui.theme.ParchmentDefault
import com.example.spellcastingcompanion.ui.theme.ParchmentLight
import com.example.spellcastingcompanion.ui.theme.SpellcastingTheme
import com.example.spellcastingcompanion.ui.theme.WoodDefault

// Helper function to map category colorName to actual Color object
// and iconName to an ImageVector
@Composable
fun getCategoryVisuals(category: Category): Pair<Color, ImageVector> {
    return when (category.colorName.lowercase()) {
        "magic-arcane" -> MagicArcane to Icons.Filled.Star // Placeholder for sparkles
        "magic-red" -> MagicRed to Icons.Filled.LocalFireDepartment
        "magic-green" -> MagicGreen to Icons.Filled.Eco
        "magic-yellow" -> MagicYellow to Icons.Filled.Star // Placeholder for sun
        // Add other mappings as needed
        else -> MaterialTheme.colorScheme.secondary to Icons.Filled.Star // Default
    }
}

@Composable
fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit,
    count: Int,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, iconVector) = getCategoryVisuals(category)
    val borderColor = if (isSelected) WoodDefault else backgroundColor.copy(alpha = 0.7f)
    val itemBackgroundColor = if (isSelected) ParchmentDefault else ParchmentLight

    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(12.dp))
            .background(itemBackgroundColor)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 12.dp, horizontal = 8.dp), // Adjusted padding
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(56.dp) // Size of the circle
                .clip(CircleShape)
                .background(backgroundColor)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = category.name,
                tint = ParchmentLight, // Icon color
                modifier = Modifier.size(32.dp)
            )
        }
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelMedium.copy(fontFamily = Charm),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 6.dp)
        )
        if (count > 0) {
            Text(
                text = "$count",
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = Charm),
                color = WoodDefault, // Or a color from your theme
                modifier = Modifier
                    .padding(top = 2.dp)
                    .background(
                        ParchmentDefault.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    SpellcastingTheme {
        val sampleCategory = Category(id = "cat-arcane", name = "Arcane", iconName = "sparkles", colorName = "magic-arcane")
        CategoryItem(
            category = sampleCategory,
            isSelected = false,
            onClick = { },
            count = 5
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryItemSelectedPreview() {
    SpellcastingTheme {
        val sampleCategory = Category(id = "cat-elemental", name = "Elemental", iconName = "flame", colorName = "magic-red")
        CategoryItem(
            category = sampleCategory,
            isSelected = true,
            onClick = { },
            count = 3
        )
    }
}