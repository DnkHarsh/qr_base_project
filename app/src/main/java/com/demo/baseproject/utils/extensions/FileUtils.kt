package com.demo.baseproject.utils.extensions

import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.Glide
import com.demo.baseproject.BuildConfig
import com.demo.baseproject.utils.logger.infoLog
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.net.URLConnection

/**
 * Method to get mimeType from url string.
 * In this method we will get url extension from url and then if it is not null will get
 * mimeType from MimeTypeMap class.
 * @return returns mimeType in string format
 */
fun String.getMimeType(): String? {
    var type: String? = null
    val extension = MimeTypeMap.getFileExtensionFromUrl(this)
    extension?.let {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
    return type
}

/**
 * Method to calculate inSampleSize to resize source image.
 * Here we check output height and width are greater then requested image height and width
 * then outHeight and outWidth reduced half of it and calculate inSampleSize.
 * If reqWidth and redHeight are less than outHeight and outWidth then inSampleSize return with 1.
 *
 * @param options   object of BitmapFactory.Options to get outHeight and outWidth.
 * @param reqWidth  reqWidth in int
 * @param reqHeight reqHeight in int
 * @return returns value in integer. default value is 1.
 */
private fun calculateInSampleSize(
    options: BitmapFactory.Options,
    reqWidth: Int,
    reqHeight: Int
): Int {
    // Raw height and width of image
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {

        val halfHeight = height / 2
        val halfWidth = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}

/**
 * Method to get bitmap from resource file.
 * Here we will decode resource file and calculate inSampleSize and then
 * decode image from resource by using BitmapFactory android class.
 * @param res       source file as a resource
 * @param resId     resource file id
 * @param reqWidth  reqWidth in int
 * @param reqHeight reqHeight in int
 * @return returns bitmap after resize source image.
 */
fun decodeSampledBitmapFromResource(
    res: Resources,
    resId: Int,
    reqWidth: Int,
    reqHeight: Int
): Bitmap {

    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(res, resId, options)

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeResource(res, resId, options)
}

/**
 * Method to notify gallery from path
 * @param path of file
 */
fun Context.notifyMediaScannerService(path: String) {
    MediaScannerConnection.scanFile(
        this, arrayOf(path), null, null
    )
}

/**
 * Method to get bitmap from uri file(local file or url string).
 * Here we will decode  file and calculate inSampleSize and then
 * decode image from uri file by using BitmapFactory android class.
 * @param reqWidth  reqWidth in int
 * @param reqHeight reqHeight in int
 * @return returns bitmap after resize source image.
 */
fun Uri.decodeSampledBitmapFromUri(reqWidth: Int, reqHeight: Int): Bitmap {

    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(this.path?.let { File(it).toString() }, options)

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeFile(this.path?.let { File(it).toString() }, options)
}

/**
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
private fun Uri.isExternalStorageDocument(): Boolean {
    return "com.android.externalstorage.documents" == authority
}

/**
 * @return Whether the Uri authority is MediaProvider.
 */
private fun Uri.isMediaDocument(): Boolean {
    return "com.android.providers.media.documents" == authority
}

/**
 * @return Whether the Uri authority is Google Photos.
 */
private fun Uri.isGooglePhotosUri(): Boolean {
    return "com.google.android.apps.photos.content" == authority
}

/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.
 *
 * @param uri           The Uri to query.
 * @param selection     (Optional) Filter used in the query.
 * @param selectionArgs (Optional) Selection arguments used in the query.
 * @return The value of the _data column, which is typically a file path.
 */
private fun Context.getDataColumn(
    uri: Uri, selection: String, selectionArgs: Array<String>
): String? {

    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)

    try {
        cursor = contentResolver.query(uri, projection, selection, selectionArgs, null)

        if (cursor != null && cursor.moveToFirst()) {
//            if (BuildConfig.DEBUG) DatabaseUtils.dumpCursor(cursor)

            val columnIndex = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(columnIndex)
        }
    } finally {
        cursor?.close()
    }
    return null
}

/**
 * Method to get bitmap from camera api.
 * We check manufacturer is samsung then wi will rotate image in 90 degree and save that image at same
 * location by replace existing image.
 * In this method image size will reduce because we are using inSampleSize to 8.
 * If it is samsung manufacturer then will get bitmap by decodeFile.
 * else get bitmap from FileInputStream.
 * @param IMAGE_CAPTURE_URI uri
 * @return bitmap
 */
fun Context.getImageFromCamera(IMAGE_CAPTURE_URI: Uri): Bitmap? {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    val profileImageSize = 450

    //for samsung manufacturer
    if (manufacturer.equals("samsung", ignoreCase = true) || model.equals(
            "samsung",
            ignoreCase = true
        )
    ) {
        val rotation =
            IMAGE_CAPTURE_URI.path?.let { getCameraPhotoOrientation(IMAGE_CAPTURE_URI, it) }
        infoLog("Rotate", rotation.toString())
        val matrix = Matrix()
        rotation?.toFloat()?.let { matrix.postRotate(it) }
        val options = BitmapFactory.Options()
        options.inSampleSize = 8

        val bitmap = BitmapFactory.decodeFile(IMAGE_CAPTURE_URI.path, options)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        //}

    } else {

        //For all manufacturer
        val options = BitmapFactory.Options()
        options.inSampleSize = 8

        return if (options.outWidth < profileImageSize && options.outHeight < profileImageSize) {
            var bitmap: Bitmap? = null
            try {
                val inputStream = FileInputStream(IMAGE_CAPTURE_URI.path?.let { File(it) })
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            bitmap
        } else {
            val bitmap = BitmapFactory.decodeFile(IMAGE_CAPTURE_URI.path, options)
            Bitmap.createBitmap(bitmap, 0, 0, profileImageSize, profileImageSize)
        }
    }
}


/**
 * Method to get new image path after rotate image if device manufacturer is samsung.
 * else it returns same image path and skip re-save operation.
 *
 * @param filePath file path of current image.
 * @return returns new path if image rotated else return same path which received as a input parameter.
 */
fun Context.rotateImageAndGetNewPath(filePath: String): String? {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    var finalFilePath: String?
    val imageCaptureUri = Uri.parse(filePath)
    if (manufacturer.equals("samsung", ignoreCase = true) || model.equals(
            "samsung",
            ignoreCase = true
        ) || manufacturer.equals(
            "Xiaomi", ignoreCase = true
        )
    ) {
        val rotation = imageCaptureUri.path?.let { getCameraPhotoOrientation(imageCaptureUri, it) }
        infoLog("Rotate", rotation.toString())
        val matrix = Matrix()
        rotation?.toFloat()?.let { matrix.postRotate(it) }
        val bitmap = BitmapFactory.decodeFile(imageCaptureUri.path)
        bitmap ?: run {
            finalFilePath = imageCaptureUri.path
            return finalFilePath
        }
        val rotatedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        finalFilePath = saveBitmap(rotatedBitmap, false)
        return finalFilePath

    } else {
        finalFilePath = imageCaptureUri.path
        return finalFilePath
    }
}

/**
 * This method is used get orientation of camera photo
 * @param imageUri  This parameter is Uri type
 * @param imagePath This parameter is String type
 * @return rotate
 */
private fun Context.getCameraPhotoOrientation(imageUri: Uri?, imagePath: String): Int {
    var rotate = 0
    try {
        imageUri?.let {
            contentResolver.notifyChange(imageUri, null)
            val imageFile = File(imagePath)
            val exif = ExifInterface(imageFile.absolutePath)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
                ExifInterface.ORIENTATION_NORMAL -> rotate = 0
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return rotate
}

/**
 * Method to save image in internal storage.
 * If any file is exist in this location then it will be deleted .
 * extension of image which will be saved is png.
 *
 * @param bitmap            image object which is stored in internal storage
 * @param IMAGE_CAPTURE_URI Uri where want to save image object.
 * @return file object after image save in requested uri.
 */
private fun Context.saveImageToSDCard(bitmap: Bitmap, IMAGE_CAPTURE_URI: Uri): File? {
    try {
        val current: String = IMAGE_CAPTURE_URI.path?.let { File(it).name } ?: ""


        val file = File(current.let { IMAGE_CAPTURE_URI.path?.replace(it, "") }, current)

        if (file.exists()) file.delete()

        val fOut = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
        fOut.flush()
        fOut.close()
        MediaScannerConnection.scanFile(this, arrayOf(file.path), null, null)

        return file
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }

}

/**
 * To check is it image from extension of input string path.
 * @return returns true if contains .jpg / .jpeg / .png / .JPG / .JPEG / .gif
 */
fun String.isImageFromUri(): Boolean {
    return !TextUtils.isEmpty(this) && (this.lowercase().contains(".jpg") || this.lowercase()
        .contains(".jpeg") || this.lowercase()
        .contains(".png") || this.contains("gif"))
}

/**
 * Method to check path is of image or not.
 * We get mimeType by using URLConnection class and then check from mimeType index.
 * @return true if url is of image else false.
 */
fun String.isImageFile(): Boolean {
    val mimeType = URLConnection.guessContentTypeFromName(this)
    return mimeType != null && mimeType.indexOf("image") == 0
}

/**
 * Method to check path is of video or not.
 * We get mimeType by using URLConnection class and then check from mimeType index.
 * @return true if url is of video else false.
 */
fun String.isVideoFile(): Boolean {
    val mimeType = URLConnection.guessContentTypeFromName(this)
    return mimeType != null && mimeType.indexOf("video") == 0
}

/**
 * Common method to load image in glide for server advertisement data.
 * @param ivAds    glide load container
 * @param url      image path in string
 * @param appName  appName in string
 * @param tvAds    appName display container
 */
fun Context.loadImageFromPath(ivAds: ImageView?, url: String?, appName: String?, tvAds: TextView?) {
    if (tvAds != null && ivAds != null) {
        tvAds.text = appName
        try {
            Glide.with(this)
                .load(url)
                .into(ivAds)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}

/**
 * Method to write json response file in local text file which will be created in app's data location (data/data/appName/adDataFile).
 * text file will be created with adDataFile name.
 *
 * @param mJsonResponse mJsonResponse response of server ad in string
 */
fun Context.writeJsonFileToStoreAdResponse(mJsonResponse: String) {
    try {
        val file = FileWriter(applicationInfo.dataDir + "/" + AD_FILE_NAME)
        file.write(mJsonResponse)
        file.flush()
        file.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }

}

/**
 * Method to read text file from data location  (data/data/appName/adDataFile) where it has been created.
 * @return returns json  data by reading from adDataFile file in string
 */

fun Context.readeDataFromFile(): String {
    var mResponse = ""
    try {
        val file = File(applicationInfo.dataDir + "/" + AD_FILE_NAME)
        val fileInputStream = FileInputStream(file)
        val size = fileInputStream.available()
        val buffer = ByteArray(size)
        fileInputStream.read(buffer)
        fileInputStream.close()
        mResponse = String(buffer)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return mResponse
}

/**
 * Method to deleted adDataFile file from data location (data/data/appName/adDataFile).
 */
fun Context.deleteDataFile() {

    val file = File(applicationInfo.dataDir + "/" + AD_FILE_NAME)

    if (file.exists()) {
        file.delete()
    }
}

/**
 * Method to get URI from from file
 */
private fun File.getUriFromFile(context: Context): Uri {
    val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", this)
    } else {
        Uri.fromFile(this)
    }
    return uri
}

/**
 * Method to get URI from from file path
 */
private fun Context.getUriFromPath(path: String): Uri? {
    val file = File(path)
    if (file.length() == 0L) {
        return null
    }
    val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file)
    } else {
        Uri.fromFile(file)
    }
    return uri
}

/**
 * save bitmap to storage with name
 * if image is temporary then save image in temp folder
 * else save image in image folder of app
 * @param bitmap
 * @param isImage
 * @return
 */
fun Context.saveBitmap(bitmap: Bitmap, isImage: Boolean): String {
    val main = File(mainDirectory)
    if (!main.exists()) {
        main.mkdir()
    }
    val imageDir = File(imageDirectory)
    if (!imageDir.exists()) {
        imageDir.mkdir()
    }
    val tempDir = File(tempDirectory)
    if (!tempDir.exists()) {
        tempDir.mkdir()
    }
    val file: File = if (isImage) {
        File(imageDir, "img" + System.currentTimeMillis() + ".png")
    } else {
        File(tempDir, "temp" + System.currentTimeMillis() + ".jpg")
    }

    try {
        val outputStream = FileOutputStream(file)
        val quality = 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        outputStream.flush()
        outputStream.close()
    } catch (_: Exception) {
    }

    MediaScannerConnection.scanFile(
        this, arrayOf(file.path),
        null, null
    )

    return file.path
}
