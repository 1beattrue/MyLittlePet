package edu.mirea.onebeattrue.mylittlepet.extensions

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

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
        imageBytes: ByteArray,
        uniqueId: Int = 0
    ): Uri {
        return try {
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

    fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
        var inputStream: InputStream? = null
        return try {
            inputStream = context.contentResolver.openInputStream(uri)
            val byteBuffer = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len: Int
            while (inputStream!!.read(buffer).also { len = it } != -1) {
                byteBuffer.write(buffer, 0, len)
            }
            byteBuffer.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            inputStream?.close()
        }
    }
}