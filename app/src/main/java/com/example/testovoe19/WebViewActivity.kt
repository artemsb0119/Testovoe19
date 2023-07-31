package com.example.testovoe19

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.Window
import android.view.WindowManager
import android.webkit.*
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WebViewActivity : AppCompatActivity() {

    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private var mCameraPhotoPath: String? = null

    private var webView: WebView? = null


    companion object {

        private const val INPUT_FILE_REQUEST_CODE = 1
    }

    override fun onSaveInstanceState(outState: Bundle) {
        webView!!.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        webView!!.restoreState(savedInstanceState)
        super.onRestoreInstanceState(savedInstanceState)
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val timeStamp = LocalDateTime.now().format(formatter)
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        return File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
        setContentView(R.layout.activity_web_view)

        val url = intent.getStringExtra("url")
        webView = findViewById(R.id.webView)
        webView!!.webViewClient = WebViewClient()
        webView!!.webChromeClient = ChromeClient()
        val webSettings = webView!!.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.databaseEnabled = true
        webSettings.setSupportZoom(false)
        webSettings.allowFileAccess = true
        webSettings.allowContentAccess = true
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH)

        if (url != null) {
            webView!!.loadUrl(url)
        }
        webView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                val intent = Intent(Intent.ACTION_VIEW, request.url)
                view.context.startActivity(intent)
                return true
            }
        }
        webView!!.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
            }
        }

    }

    inner class ChromeClient : WebChromeClient() {
        // For Android 5.0
        override fun onShowFileChooser(
            view: WebView,
            filePath: ValueCallback<Array<Uri>>,
            fileChooserParams: FileChooserParams
        ): Boolean {
            // Double check that we don't have any existing callbacks
            if (mFilePathCallback != null) {
                mFilePathCallback!!.onReceiveValue(null)
            }
            mFilePathCallback = filePath
            var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent!!.resolveActivity(packageManager) != null) {
                // Create the File where the photo should go
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath)
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.e("ErrorCreatingFile", "Unable to create Image File", ex)
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCameraPhotoPath = "file:" + photoFile.absolutePath
                    takePictureIntent.putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile)
                    )
                } else {
                    takePictureIntent = null
                }
            }
            val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
            contentSelectionIntent.type = "image/*"
            val intentArray: Array<Intent?> =
                takePictureIntent?.let { arrayOf(it) } ?: arrayOfNulls(0)
            val chooserIntent = Intent(Intent.ACTION_CHOOSER)
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE)
            return true
        }


    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }
        var results: Array<Uri>? = null

        // Check that the response is a good one
        if (resultCode == RESULT_OK) {
            if (data == null) {
                // If there is not data, then we may have taken a photo
                if (mCameraPhotoPath != null) {
                    results = arrayOf(Uri.parse(mCameraPhotoPath))
                }
            } else {
                val dataString = data.dataString
                if (dataString != null) {
                    results = arrayOf(Uri.parse(dataString))
                }
            }
        }
        mFilePathCallback!!.onReceiveValue(results)
        mFilePathCallback = null

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event!!.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (webView!!.canGoBack()) {
                        webView!!.goBack()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}