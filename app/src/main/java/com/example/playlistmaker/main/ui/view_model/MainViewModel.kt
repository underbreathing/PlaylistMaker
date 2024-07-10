package com.example.playlistmaker.main.ui.view_model

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator

import com.example.playlistmaker.sharing.domain.SharingInteractor

class MainViewModel(private val sharingInteractor: SharingInteractor): ViewModel() {
    fun<T> goToActivity(activityClass: Class<T>) {
        sharingInteractor.openActivity(activityClass)
    }

    companion object{
        fun getMainViewModelFactory(activity: Activity): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(Creator.provideSharingInteractor(activity))
            }
        }
    }
}