package com.example.playlistmaker.settings.data.impl

import android.app.Application
import com.example.playlistmaker.application.App
import com.example.playlistmaker.settings.data.local_storage.SettingsLocalStorage
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(
    private val application: Application,
    private val localStorage: SettingsLocalStorage
) : SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(localStorage.getDarkThemeMode())
    }

    override fun updateThemeSettings(themeSettings: ThemeSettings) {
        (application as App).switchTheme(themeSettings.isDarkThemeEnabled)
    }

}