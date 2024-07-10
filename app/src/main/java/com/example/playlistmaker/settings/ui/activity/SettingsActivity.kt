package com.example.playlistmaker.settings.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.example.playlistmaker.sharing.domain.model.EmailData
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MYY", "activity rerere")
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, SettingsViewModel.getSettingsViewModelFactory(this)
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
            viewModel.shareApp(
                getString(R.string.settings_share_link), getString(R.string.settings_share_title)
            )
        }

        binding.settingButtonWriteToSupport.setOnClickListener {
            viewModel.writeToSupport(
                EmailData(
                    getString(R.string.settings_email_address),
                    getString(R.string.settings_support_message),
                    getString(R.string.settings_support_subject)
                )
            )
        }

        binding.settingsButtonAgreement.setOnClickListener {
            viewModel.openTerms(getString(R.string.settings_agreement_link))
        }

    }
}