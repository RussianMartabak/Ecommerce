package com.martabak.ecommerce.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

class PhotoUriManager(private val appContext: Context) {


    fun buildNewUri(): Uri {
        val photosDir = File(appContext.cacheDir, PHOTOS_DIR)
        photosDir.mkdirs()
        val photoFile = File(photosDir, generateFilename())
        val authority = "${appContext.packageName}.$FILE_PROVIDER"

        return MyFileProvider.getUriForFile(appContext, authority, photoFile)
    }

    fun uriToFile(uri : Uri) : File {
        val photosDir = File(appContext.cacheDir, PHOTOS_DIR)
        photosDir.mkdirs()
        val photoFile = File(photosDir, generateFilename())
        val input = appContext.contentResolver.openInputStream(uri)
        val bytes = input!!.readBytes()
        photoFile.writeBytes(bytes)
        input.close()
        return photoFile

    }

    /**
     * Create a unique file name based on the time the photo is taken
     */
    private fun generateFilename() = "selfie-${System.currentTimeMillis()}.jpg"

    companion object {
        private const val PHOTOS_DIR = "photos"
        private const val FILE_PROVIDER = "fileprovider"
    }
}