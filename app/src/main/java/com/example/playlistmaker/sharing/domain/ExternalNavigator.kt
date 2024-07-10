package com.example.playlistmaker.sharing.domain

import com.example.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {

    fun<T> openActivity(clazz: Class<T>)
    fun openLink(link: String)

    fun shareLink(sharedLink: String, sharedTitle: String = "" )

    fun openEmail(emailData: EmailData)
}