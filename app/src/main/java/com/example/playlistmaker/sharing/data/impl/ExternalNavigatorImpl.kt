package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.media_library.ui.MediaLibraryActivity
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun <T> openActivity(clazz: Class<T>) {
        val intentMedia = Intent(context,clazz)
        context.startActivity(intentMedia)
    }

    override fun openLink(link: String) {
        val url = Uri.parse(link)
        val agreementIntent = Intent(Intent.ACTION_VIEW, url)
        context.startActivity(agreementIntent)
    }

    override fun shareLink(sharedLink: String, sharedTitle: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            sharedLink
        )

        context.startActivity(
            Intent.createChooser(
                shareIntent,
                sharedTitle
            )
        )
    }

    override fun openEmail(emailData: EmailData) {
        context.startActivity(getEmailIntent(emailData))
    }

    private fun getEmailIntent(emailData: EmailData): Intent {
        return Intent(Intent.ACTION_SENDTO).apply {
            this.data = Uri.parse("mailto:")
            this.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(emailData.emailAddress)
            )
            this.putExtra(
                Intent.EXTRA_TEXT,
                emailData.message
            )
            this.putExtra(
                Intent.EXTRA_SUBJECT,
                emailData.subject
            )
        }

    }
}