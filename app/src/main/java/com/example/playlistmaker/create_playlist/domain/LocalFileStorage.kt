package com.example.playlistmaker.create_playlist.domain

import android.net.Uri

interface LocalFileStorage {

   suspend fun saveImage(uri: Uri, fileName: String)
}