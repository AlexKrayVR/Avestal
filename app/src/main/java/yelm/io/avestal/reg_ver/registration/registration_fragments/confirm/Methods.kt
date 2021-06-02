package yelm.io.avestal.reg_ver.registration.registration_fragments.confirm

import android.graphics.Bitmap
import android.util.Base64
import yelm.io.avestal.Logging
import java.io.ByteArrayOutputStream


//TODO
//recreate to MVP


fun convertingImageToBase64(bitmap: Bitmap): String {
    Logging.logDebug("ConvertingImageToBase64()")
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos)
    val imageBytes = baos.toByteArray()
    bitmap.recycle()
    Logging.logDebug("imageBytes - length " + imageBytes.size)
    val imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT)
    Logging.logDebug("Base64.encodeToString - imageString.length " + imageString.length)
    return imageString
}