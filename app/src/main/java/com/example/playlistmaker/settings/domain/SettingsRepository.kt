package com.example.playlistmaker.settings.domain

import com.example.playlistmaker.settings.domain.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings

    fun updateThemeSettings(themeSettings: ThemeSettings)
}