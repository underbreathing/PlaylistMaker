package com.example.playlistmaker


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val switchTheme = findViewById<SwitchCompat>(R.id.settings_switch_theme)
        switchTheme.setOnCheckedChangeListener{ _,isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val buttonBack = findViewById<TextView>(R.id.settings_button_back)
        buttonBack.setOnClickListener {
            finish()
        }
        val buttonShare = findViewById<FrameLayout>(R.id.settings_button_share)
        buttonShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.settings_share_link)
            ) // здесь указываете вашу ссылку

            startActivity(Intent.createChooser(shareIntent, getString(R.string.settings_share_title)))
        }
        val buttonWriteToSupport = findViewById<FrameLayout>(R.id.setting_button_write_to_support)
        buttonWriteToSupport.setOnClickListener {
            val writeToSupportIntent = Intent(Intent.ACTION_SENDTO)
            writeToSupportIntent.data = Uri.parse("mailto:")
            writeToSupportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.settings_email_address)))
            writeToSupportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_support_message))
            writeToSupportIntent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.settings_support_subject))
            startActivity(writeToSupportIntent)
        }
        val buttonAgreement = findViewById<FrameLayout>(R.id.settings_button_agreement)
        buttonAgreement.setOnClickListener{
            val url = Uri.parse(getString(R.string.settings_agreement_link))
            val agreementIntent = Intent(Intent.ACTION_VIEW,url)
            startActivity(agreementIntent)

        }

    }
}