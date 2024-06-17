package edu.mirea.onebeattrue.mylittlepet.extensions

import android.content.Context
import android.net.Uri
import android.util.Base64
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageUtils {

    fun saveImageToInternalStorage(context: Context, uri: Uri): Uri {
        if (uri != Uri.EMPTY) {
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
        return Uri.EMPTY
    }

    fun saveImageToInternalStorage(
        context: Context,
        base64String: String,
        uniqueId: Int = 0
    ): Uri {
        return try {
            val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
            val fileName = "${System.currentTimeMillis()}_${uniqueId}.jpg"
            val file = File(context.filesDir, fileName)

            FileOutputStream(file).use { outputStream ->
                outputStream.write(imageBytes)
            }

            file.toUri()
        } catch (e: IOException) {
            e.printStackTrace()
            Uri.EMPTY
        }
    }

    fun uriToBase64(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()
            bytes?.let { Base64.encodeToString(it, Base64.DEFAULT) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}