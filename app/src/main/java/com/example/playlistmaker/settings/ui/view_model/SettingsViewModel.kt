package com.example.playlistmaker.settings.ui.view_model

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.application.App
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.model.EmailData

class SettingsViewModel(
    application: Application,
    private val settingsRepository: SettingsRepository,
    private val sharingInteractor: SharingInteractor
) : AndroidViewModel(application) {


    fun switchTheme(themeSettings: ThemeSettings) {
        settingsRepository.updateThemeSettings(themeSettings)

    }

    fun getThemeModSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    fun shareApp(appLink: String, sharedTitle: String) {
        sharingInteractor.shareApp(appLink, sharedTitle)
    }

    fun openTerms(link: String) {
        sharingInteractor.openTerms(link)
    }

    fun writeToSupport(emailData: EmailData) {
        sharingInteractor.openSupport(emailData)
    }


    companion object {
        fun getSettingsViewModelFactory(activity: Activity): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val application = this[APPLICATION_KEY] as App
                    SettingsViewModel(
                        application,
                        application.provideSettingsRepository(),
                        Creator.provideSharingInteractor(activity)
                    )

                }
            }
    }
}