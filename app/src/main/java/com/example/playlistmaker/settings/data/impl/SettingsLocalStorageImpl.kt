package com.example.playlistmaker.settings.data.impl

import android.content.SharedPreferences
import com.example.playlistmaker.application.KEY_IS_DARK_THEME
import com.example.playlistmaker.settings.data.local_storage.SettingsLocalStorage

class SettingsLocalStorageImpl(private val sharedPreferences: SharedPreferences) :
    SettingsLocalStorage {
    override fun getDarkThemeMode(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_DARK_THEME, false)
    }
}