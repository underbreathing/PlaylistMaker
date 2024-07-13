package com.example.playlistmaker.application

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.useCaseModule
import com.example.playlistmaker.di.viewModelModule
import kotlinx.coroutines.Job
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf

const val KEY_IS_DARK_THEME = "key_is_dark_theme"
const val KEY_THEME_FILE = "key_theme_file"

class App : Application() {

    private lateinit var sharedTheme: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, useCaseModule, viewModelModule)
        }
        sharedTheme = getKoin().get { parametersOf(KEY_THEME_FILE) }//getSharedPreferences(KEY_THEME_FILE, MODE_PRIVATE)
        switchTheme(sharedTheme.getBoolean(KEY_IS_DARK_THEME, false))//при вызове этого метода устанавливается тема для всего приложения
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        Log.d("MYY", "dark enabled == $darkThemeEnabled")
        sharedTheme.edit { putBoolean(KEY_IS_DARK_THEME, darkThemeEnabled) }
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

//    fun provideSettingsRepository(): SettingsRepository {
//        return SettingsRepositoryImpl(this, getSettingsLocalStorage())
//    }
//
//    private fun getSettingsLocalStorage(): SettingsLocalStorage {
//        return SettingsLocalStorageImpl(sharedTheme)
//    }
}