package com.example.spellcastingcompanion.data.model

// Based on export interface Category from
data class Category(
    val id: String,
    val name: String,
    val iconName: String, // Will map to a drawable resource or Compose Icon later
    val colorName: String   // Will map to a Color object later
)