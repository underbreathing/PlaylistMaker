package com.example.playlistmaker.settings.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.application.App
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class SettingsViewModel(
    application: Application,
    private val settingsRepository: SettingsRepository,
    private val themeSettingsLiveData: MutableLiveData<ThemeSettings>
) : AndroidViewModel(application) {

    init {
        themeSettingsLiveData.value = getThemeModSettings()
    }

    companion object {
        fun getSettingsViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = Creator.getApplication() as App
                SettingsViewModel(
                    app,
                    app.provideSettingsRepository(),
                    MutableLiveData()
                )
            }
        }
    }

    fun getThemeSettingsLiveData(): LiveData<ThemeSettings> = themeSettingsLiveData

    fun switchTheme(themeSettings: ThemeSettings) {
        settingsRepository.updateThemeSettings(themeSettings)
        themeSettingsLiveData.value = themeSettings
    }

    private fun getThemeModSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }
}