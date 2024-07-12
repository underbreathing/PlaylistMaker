package com.example.playlistmaker.settings.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.example.playlistmaker.settings.ui.model.EmailData
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MYY", "activity rebuild")
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, SettingsViewModel.getSettingsViewModelFactory()
        )[SettingsViewModel::class.java]

        val switchTheme: SwitchMaterial = binding.settingsSwitchTheme
        viewModel.getThemeSettingsLiveData().observe(this) {
            Log.d("MYY", "getFromViewModel")
            switchTheme.isChecked = it.isDarkThemeEnabled
        }

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            Log.d("MYY", "switch")
            viewModel.switchTheme(ThemeSettings(isChecked))
        }

        binding.settingsButtonBack.setOnClickListener {
            finish()
        }

        binding.settingsButtonShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT, getString(R.string.settings_share_link)
            )

            startActivity(
                Intent.createChooser(
                    shareIntent, getString(R.string.settings_share_title)
                )
            )
        }

        binding.settingButtonWriteToSupport.setOnClickListener {
            startActivity(
                getEmailIntent(
                    EmailData(
                        getString(R.string.settings_email_address),
                        getString(R.string.settings_support_message),
                        getString(R.string.settings_support_subject)
                    )
                )
            )
        }

        binding.settingsButtonAgreement.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse(getString(R.string.settings_agreement_link))
                )
            )
        }

    }

    private fun getEmailIntent(emailData: EmailData): Intent {
        return Intent(Intent.ACTION_SENDTO).apply {
            this.data = Uri.parse("mailto:")
            this.putExtra(
                Intent.EXTRA_EMAIL, arrayOf(emailData.emailAddress)
            )
            this.putExtra(
                Intent.EXTRA_TEXT, emailData.message
            )
            this.putExtra(
                Intent.EXTRA_SUBJECT, emailData.subject
            )
        }

    }
}