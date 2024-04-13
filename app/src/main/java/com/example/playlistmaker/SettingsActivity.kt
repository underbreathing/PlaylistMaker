package com.example.playlistmaker


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedTheme = getSharedPreferences(KEY_THEME_FILE, MODE_PRIVATE)
        val switchTheme = findViewById<SwitchMaterial>(R.id.settings_switch_theme)
        switchTheme.isChecked = sharedTheme.getBoolean(KEY_IS_DARK_THEME, false)

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
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

            startActivity(
                Intent.createChooser(
                    shareIntent,
                    getString(R.string.settings_share_title)
                )
            )
        }
        val buttonWriteToSupport = findViewById<FrameLayout>(R.id.setting_button_write_to_support)
        buttonWriteToSupport.setOnClickListener {
            val writeToSupportIntent = Intent(Intent.ACTION_SENDTO)
            writeToSupportIntent.data = Uri.parse("mailto:")
            writeToSupportIntent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(getString(R.string.settings_email_address))
            )
            writeToSupportIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.settings_support_message)
            )
            writeToSupportIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.settings_support_subject)
            )
            startActivity(writeToSupportIntent)
        }
        val buttonAgreement = findViewById<FrameLayout>(R.id.settings_button_agreement)
        buttonAgreement.setOnClickListener {
            val url = Uri.parse(getString(R.string.settings_agreement_link))
            val agreementIntent = Intent(Intent.ACTION_VIEW, url)
            startActivity(agreementIntent)

        }

    }
}