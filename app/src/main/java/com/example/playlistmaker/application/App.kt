package com.example.playlistmaker.application

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.data.impl.SettingsLocalStorageImpl
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.data.local_storage.SettingsLocalStorage
import com.example.playlistmaker.settings.domain.SettingsRepository

const val KEY_THEME_FILE = "key_theme_file"
const val KEY_IS_DARK_THEME = "key_is_dark_theme"

class App : Application() {

    private lateinit var sharedTheme: SharedPreferences
    private var isDarkTheme = false
    override fun onCreate() {
        super.onCreate()
        sharedTheme = getSharedPreferences(KEY_THEME_FILE, MODE_PRIVATE)
        isDarkTheme = sharedTheme.getBoolean(KEY_IS_DARK_THEME, false)
        switchTheme(isDarkTheme)//при вызове этого метода устанавливается тема для всего приложения
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        Log.d("MYY","dark enabled == $darkThemeEnabled")
        sharedTheme.edit()
            .putBoolean(KEY_IS_DARK_THEME, darkThemeEnabled)
            .apply()
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun provideSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(this, getSettingsLocalStorage())
    }

    private fun getSettingsLocalStorage(): SettingsLocalStorage {
        return SettingsLocalStorageImpl(sharedTheme)
    }

}