package edu.mirea.onebeattrue.mylittlepet.extensions

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream

fun saveImageToInternalStorage(context: Context, uri: Uri): Uri {
    val bytes = context.contentResolver.openInputStream(uri)?.use { inputStream ->
        inputStream.readBytes()
    }
    val fileName = "${System.currentTimeMillis()}.jpg"
    val file = File(context.filesDir, fileName)

    FileOutputStream(file).use { outputStream ->
        outputStream.write(bytes)
    }

    return file.toUri()
}