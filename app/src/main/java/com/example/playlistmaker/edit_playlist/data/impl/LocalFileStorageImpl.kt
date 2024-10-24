package com.example.playlistmaker.edit_playlist.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import com.example.playlistmaker.edit_playlist.domain.api.LocalFileStorage
import java.io.File
import java.io.FileOutputStream

class LocalFileStorageImpl(private val appContext: Context) : LocalFileStorage {
    override suspend fun saveImage(uri: Uri, playlistId: Long) {
        val pathToImages =
            File(appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "PM_playlists")
        if (!pathToImages.exists()) {
            pathToImages.mkdirs()
        }
        val outputFile = File(pathToImages, "${playlistId}.jpg")
        Log.d("MYY", "$playlistId")

        appContext.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it)
                .compress(
                    Bitmap.CompressFormat.JPEG,
                    30,
                    FileOutputStream(outputFile)
                )
        }
    }
}