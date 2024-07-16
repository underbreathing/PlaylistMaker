package com.example.playlistmaker.settings.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class SettingsViewModel(
    application: Application,
    private val settingsRepository: SettingsRepository,
) : AndroidViewModel(application) {

    private val themeSettingsLiveData: MutableLiveData<ThemeSettings> =
        MutableLiveData(getThemeModSettings())

    fun getThemeSettingsLiveData(): LiveData<ThemeSettings> = themeSettingsLiveData

    fun switchTheme(themeSettings: ThemeSettings) {
        settingsRepository.updateThemeSettings(themeSettings)
        themeSettingsLiveData.value = themeSettings
    }

    private fun getThemeModSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }
}