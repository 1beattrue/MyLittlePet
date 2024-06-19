package edu.mirea.onebeattrue.mylittlepet.data.mapper

import android.app.Application
import android.net.Uri
import androidx.core.net.toUri
import edu.mirea.onebeattrue.mylittlepet.extensions.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImageMapper @Inject constructor(
    private val application: Application
) {
    suspend fun mapBase64ToUri(base64String: String?, uniqueId: Int): String {
        return withContext(Dispatchers.IO) {
            (base64String?.let { ImageUtils.saveImageToInternalStorage(application, it, uniqueId) }
                ?: Uri.EMPTY).toString()
        }
    }

    suspend fun mapUriToBase64(uriString: String): String? {
        return withContext(Dispatchers.IO) {
            val uri = uriString.toUri()
            ImageUtils.uriToBase64(application, uri)
        }
    }
}