package com.example.playlistmaker.sharing.domain

import com.example.playlistmaker.sharing.domain.model.EmailData

interface SharingInteractor {

    fun<T> openActivity(clazz: Class<T>)
    fun shareApp(sharedLink: String, sharedTitle: String = "")
    fun openTerms(link: String)
    fun openSupport(emailData: EmailData)
}