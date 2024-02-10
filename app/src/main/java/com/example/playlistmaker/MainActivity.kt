package com.example.playlistmaker

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //установка обработчика нажатия для ПЕРВОЙ кнопки
        val buttonSearch = findViewById<Button>(R.id.button_search)
        val intentSearch = Intent(this, SearchActivity::class.java)
        val buttonSearchOnClickListener = object : OnClickListener {
            override fun onClick(p0: View?) {
                startActivity(intentSearch)
            }
        }
        buttonSearch.setOnClickListener(buttonSearchOnClickListener)
        //установка обработчика нажатия для ВТОРОЙ кнопки
        val buttonMedia = findViewById<Button>(R.id.button_media)
        buttonMedia.setOnClickListener {
            val intentMedia = Intent(this, MediaLibraryActivity::class.java)
            startActivity(intentMedia)
        }
        //установка обработчика нажатия для ТРЕТЬЕЙ кнопки
        val buttonSettings = findViewById<Button>(R.id.button_settings)
        buttonSettings.setOnClickListener {
            val intentSettings = Intent(this, SettingsActivity::class.java)
            startActivity(intentSettings)
        }
    }
}