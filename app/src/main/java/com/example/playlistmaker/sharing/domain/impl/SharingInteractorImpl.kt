package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator): SharingInteractor {
    override fun <T> openActivity(clazz: Class<T>) {
        externalNavigator.openActivity(clazz)
    }

    override fun shareApp(sharedLink: String, sharedTitle: String) {
        externalNavigator.shareLink(sharedLink,sharedTitle)
    }

    override fun openTerms(link: String) {
        externalNavigator.openLink(link)
    }

    override fun openSupport(emailData: EmailData) {
        externalNavigator.openEmail(emailData)
    }


}