package com.example.plazapalm.imageutils

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.exifinterface.media.ExifInterface
import com.example.plazapalm.MainActivity
import com.example.plazapalm.R
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

//TODO
object FilePathUtil {
    fun getMediaFilePathFor(
        context: Context,
        uri: Uri,
        compression: Boolean = true,
        width: Int = 512,
        height: Int = 512
    ): File {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)
        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val file = File(context.filesDir, name)
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read: Int
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable = inputStream!!.available()
            val bufferSize = bytesAvailable.coerceAtMost(maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream.read(buffers).also { read = it } != -1) {
                outputStream.write(buffers, 0, read)
            }
            inputStream.close()
            outputStream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        returnCursor.close()
        return if (compression) {
            val compressed = getCompressed(context, file.path, width, height)
            deleteFile(file)
            compressed
        } else
            file
    }

    fun getPathFromUri(context: Context, uri: Uri): String? {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri: Uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                when (type) {
                    "image" -> {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                    "video" -> {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }
                    "audio" -> {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?,
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean =
        "com.android.externalstorage.documents" == uri.authority

    private fun isDownloadsDocument(uri: Uri): Boolean =
        "com.android.providers.downloads.documents" == uri.authority

    private fun isMediaDocument(uri: Uri): Boolean =
        "com.android.providers.media.documents" == uri.authority

    private fun isGooglePhotosUri(uri: Uri): Boolean =
        "com.google.android.apps.photos.content" == uri.authority

    /*fun convertFileToContentUri(context: Context, file: File,returnUri:(Uri?)->Unit) {
        val values = ContentValues()
        values.put(MediaStore.Video.Media.TITLE, "Title1")
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
        values.put(MediaStore.Video.Media.DATA, file.absolutePath)
        val uri: Uri = cr.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
    }*/

    fun saveImage(
        context: Context,
        myBitmap: Bitmap,
        compression: Boolean = true,
        width: Int = 512,
        height: Int = 512
    ): File? {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val wallpaperDirectory = File(context.cacheDir.absolutePath + "/" + context.getString(R.string.app_name))
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }

        try {
            val f = File(wallpaperDirectory, ((Calendar.getInstance().timeInMillis).toString() + ".jpg"))
            f.createNewFile()
            f.writeBytes(bytes.toByteArray())
            MediaScannerConnection.scanFile(context, arrayOf(f.path), arrayOf("image/jpeg"), null)
            return if (compression) {
                val file = getCompressed(context, f.absolutePath, width = width, height = height)
                deleteFile(f)
                file
            } else
                f
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return null
    }

    private fun getCompressed(context: Context?, path: String?, width: Int, height: Int): File {
        if (context == null) throw NullPointerException("Context must not be null.")
        //getting device external cache directory, might not be available on some devices,
        // so our code fall back to internal storage cache directory, which is always available but in smaller quantity
        val cacheDir = context.cacheDir
//        if (cacheDir == null) //fall back
//            cacheDir = context.cacheDir
        val rootDir = cacheDir!!.absolutePath + "/" + context.getString(R.string.app_name)
        val root = File(rootDir)
        //Create ImageCompressor folder if it doesnt already exists.
        if (!root.exists()) root.mkdirs()
        //decode and resize the original bitmap from @param path.
        val bitmap = decodeImageFromFiles(
            path,  /* your desired width*/
            width,  /*your desired height*/
            height
        )
        //create placeholder for the compressed image file
        val compressed = File(
            root,
            SimpleDateFormat(DateTimeUtil.UTC_FORMAT, Locale.getDefault()).format(Date())
                .toString() + ".png"
        )
        //convert the decoded bitmap to stream
        val byteArrayOutputStream = ByteArrayOutputStream()
        /*compress bitmap into byteArrayOutputStream
        Bitmap.compress(Format, Quality, OutputStream)
        Where Quality ranges from 1 - 100.
     */bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream)
        /*
    Right now, we have our bitmap inside byteArrayOutputStream Object, all we need next is to write it to the compressed file we created earlier,
    java.io.FileOutputStream can help us do just That!
     */
        val fileOutputStream = FileOutputStream(compressed)
        fileOutputStream.write(byteArrayOutputStream.toByteArray())
        fileOutputStream.flush()
        fileOutputStream.close()
        //File written, return to the caller. Done!
        Log.e("After Compression Size", compressed.length().toString())
        return compressed
    }

    private fun decodeImageFromFiles(path: String?, width: Int, height: Int): Bitmap {
        val scaleOptions = BitmapFactory.Options()
        scaleOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, scaleOptions)
        var scale = 1
        while (scaleOptions.outWidth / scale / 2 >= width
            && scaleOptions.outHeight / scale / 2 >= height
        ) {
            scale *= 2
        }
        // decode with the sample size
        val outOptions = BitmapFactory.Options()
        outOptions.inSampleSize = scale
        return BitmapFactory.decodeFile(path, outOptions)
    }

    fun String.getFileType(): String = try {
        File(this).extension
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveMediaFile2Gallery(
        filePath: String?,
        isVideo: Boolean,
        fileName: String,
        returnData: (Uri?) -> Unit,
    ) {
        filePath?.let {
            val context = MainActivity.context.get()
            val values = ContentValues().apply {
                val folderName = if (isVideo) {
                    Environment.DIRECTORY_MOVIES
                } else {
                    Environment.DIRECTORY_PICTURES
                }
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(
                    MediaStore.Images.Media.MIME_TYPE,
                    MimeUtils.guessMimeTypeFromExtension(getExtension(fileName))
                )
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    folderName + "/${context?.getString(R.string.app_name)}/"
                )
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
            val pathtemp = "/storage/emulated/0/Movies/${
                context?.getString(R.string.app_name)
            }/$fileName"
            val file = File(pathtemp)
            if (file.exists()) {
                Log.e("PRevue", file.name)
                file.delete()
            }
            val collection = if (isVideo) {
                MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            }
            val fileUri = context?.contentResolver?.insert(collection, values)

            fileUri?.let {
                if (isVideo) {
                    context.contentResolver?.openFileDescriptor(fileUri, "w")
                        .use { descriptor ->
                            descriptor?.let {
                                FileOutputStream(descriptor.fileDescriptor).use { out ->
                                    val videoFile = File(filePath)
                                    FileInputStream(videoFile).use { inputStream ->
                                        val buf = ByteArray(8192)
                                        while (true) {
                                            val sz = inputStream.read(buf)
                                            if (sz <= 0) break
                                            out.write(buf, 0, sz)
                                        }
                                    }
                                    out.close()
                                }
                            }
                        }
                } else {
                    context.contentResolver?.openOutputStream(fileUri).use { out ->
                        val bmOptions = BitmapFactory.Options()
                        val bmp = BitmapFactory.decodeFile(filePath, bmOptions)
                        bmp.compress(Bitmap.CompressFormat.JPEG, 90, out)
                        bmp.recycle()
                        out?.close()
                    }
                }
                values.clear()
                values.put(
                    if (isVideo) MediaStore.Video.Media.IS_PENDING else MediaStore.Images.Media.IS_PENDING,
                    0
                )
                context.contentResolver?.update(fileUri, values, null, null)
                Log.e("Path", fileUri.toString())
                Log.e("Path", fileUri.path.toString())
                returnData(fileUri)
            }
        }
    }

    private fun deleteFile(file: File) {
        if (file.exists())
            file.delete()
    }

    private fun getExtension(fileName: String): String {
        return fileName.substring(fileName.lastIndexOf("."))
    }

    fun rotateImageIfRequired(
        selectedImage: Uri,
        img: Bitmap,
    ): Bitmap {

        val ei = ExifInterface(selectedImage.path ?: "")
        val orientation: Int = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        var rotatedBitmap: Bitmap = img
        when (orientation) {
            ExifInterface.ORIENTATION_NORMAL -> {
                Log.e("orientation is", "orientation 0")
                rotatedBitmap = rotateImage(img, 0)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                Log.e("orientation is", "orientation 90")
                rotatedBitmap = rotateImage(img, 90)
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                Log.e("orientation is", "orientation 180")
                rotatedBitmap = rotateImage(img, 180)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                Log.e("orientation is", "orientation 270")
                rotatedBitmap = rotateImage(img, 270)
            }
        }
        return rotatedBitmap
    }

    private fun rotateImage(bitmap: Bitmap, degrees: Int): Bitmap =
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, Matrix().apply {
            postRotate(degrees.toFloat())
        }, true)
}