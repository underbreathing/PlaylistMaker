package com.example.playlistmaker

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.ui.SearchActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //установка обработчика нажатия для ПЕРВОЙ кнопки
        val buttonSearch = findViewById<Button>(R.id.main_button_search)
        buttonSearch.setOnClickListener {
            val intentSearch = Intent(this, SearchActivity::class.java)
            startActivity(intentSearch)
        }
        //установка обработчика нажатия для ВТОРОЙ кнопки
        val buttonMedia = findViewById<Button>(R.id.main_button_media)
        buttonMedia.setOnClickListener {
            val intentMedia = Intent(this, MediaLibraryActivity::class.java)
            startActivity(intentMedia)
        }
        //установка обработчика нажатия для ТРЕТЬЕЙ кнопки
        val buttonSettings = findViewById<Button>(R.id.main_button_settings)
        buttonSettings.setOnClickListener {
            val intentSettings = Intent(this, SettingsActivity::class.java)
            startActivity(intentSettings)
        }

    }
}