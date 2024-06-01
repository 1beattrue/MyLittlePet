package edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.qrcode

import android.graphics.ImageFormat
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import java.nio.ByteBuffer

class QrCodeAnalyzer(
    private val onQrCodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val supportedImageFormats = listOf(
        ImageFormat.YUV_420_888,
        ImageFormat.YUV_422_888,
        ImageFormat.YUV_444_888
    )

    override fun analyze(image: ImageProxy) {
        if (image.format in supportedImageFormats) {
            if (image.planes.size == 3) {
                val rotatedImage =
                    RotatedImage(getLuminancePlaneData(image), image.width, image.height)
                rotateImageArray(rotatedImage, image.imageInfo.rotationDegrees)

                val source = PlanarYUVLuminanceSource(
                    rotatedImage.byteArray,
                    rotatedImage.width,
                    rotatedImage.height,
                    0,
                    0,
                    rotatedImage.width,
                    rotatedImage.height,
                    false
                )

                val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
                try {
                    val result = MultiFormatReader().apply {
                        setHints(
                            mapOf(
                                DecodeHintType.POSSIBLE_FORMATS to arrayListOf(
                                    BarcodeFormat.QR_CODE
                                )
                            )
                        )
                    }.decode(binaryBitmap)
                    onQrCodeScanned(result.text)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    image.close()
                }
            }
        }
    }

    private fun getLuminancePlaneData(image: ImageProxy): ByteArray {
        val plane = image.planes.first()
        val buf: ByteBuffer = plane.buffer
        val data = ByteArray(buf.remaining())
        buf.get(data)
        buf.rewind()
        val width = image.width
        val height = image.height
        val rowStride = plane.rowStride
        val pixelStride = plane.pixelStride

        val cleanData = ByteArray(width * height)
        for (y in 0 until height) {
            for (x in 0 until width) {
                cleanData[y * width + x] = data[y * rowStride + x * pixelStride]
            }
        }
        return cleanData
    }

    private fun rotateImageArray(imageToRotate: RotatedImage, rotationDegrees: Int) {
        if (rotationDegrees == 0) return
        if (rotationDegrees % 90 != 0) return

        val width = imageToRotate.width
        val height = imageToRotate.height

        val rotatedData = ByteArray(imageToRotate.byteArray.size)
        for (y in 0 until height) {
            for (x in 0 until width) {
                when (rotationDegrees) {
                    90 -> rotatedData[x * height + height - y - 1] =
                        imageToRotate.byteArray[x + y * width]

                    180 -> rotatedData[width * (height - y - 1) + width - x - 1] =
                        imageToRotate.byteArray[x + y * width]

                    270 -> rotatedData[y + x * height] =
                        imageToRotate.byteArray[y * width + width - x - 1]
                }
            }
        }

        imageToRotate.byteArray = rotatedData

        if (rotationDegrees != 180) {
            imageToRotate.height = width
            imageToRotate.width = height
        }
    }
}

private class RotatedImage(var byteArray: ByteArray, var width: Int, var height: Int)