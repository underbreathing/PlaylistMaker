package com.example.playlistmaker.create_playlist.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.playlistmaker.create_playlist.domain.LocalFileStorage
import java.io.File
import java.io.FileOutputStream

class LocalFileStorageImpl(private val appContext: Context) : LocalFileStorage {
    override suspend fun saveImage(uri: Uri, fileName: String) {
        val pathToImages =
            File(appContext.filesDir, "PM_playlists")
        if (!pathToImages.exists()) {
            pathToImages.mkdirs()
        }
        BitmapFactory.decodeStream(appContext.contentResolver.openInputStream(uri))
            .compress(
                Bitmap.CompressFormat.JPEG,
                30,
                FileOutputStream(File(pathToImages, "${fileName}.jpg"))
            )
    }
}