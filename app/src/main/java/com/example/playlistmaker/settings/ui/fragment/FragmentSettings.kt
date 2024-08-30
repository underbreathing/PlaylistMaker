package com.example.playlistmaker.settings.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.example.playlistmaker.settings.ui.model.EmailData
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentSettings : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switchTheme: SwitchMaterial = binding.settingsSwitchTheme
        viewModel.getThemeSettingsLiveData().observe(viewLifecycleOwner) {
            switchTheme.isChecked = it.isDarkThemeEnabled
        }

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchTheme(ThemeSettings(isChecked))
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}