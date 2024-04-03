package com.example.myapplication3.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.example.myapplication3.coroutines.MyCoroutine
import com.example.myapplication3.extensions.log
import com.example.myapplication3.ui.dialog.MyDialogFragment
import com.example.myapplication3.workManager.ImageUploadWorker
import com.example.myapplication3.workManager.enqueueProcessImageAndUpload


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.myapplication3.R.layout.activity_main)
        if (savedInstanceState == null) {

            // lifeCycleTransparent()

//            supportFragmentManager
//            .beginTransaction()
//            .replace(com.example.myapplication3.R.id.content_frame, MainFragment.getInstance())
//            .commit()

            // Work Manager
            //workManager()

            // Coroutine
            coroutine()
        }
    }

    private fun workManager() {
        // Work Manager
        //startWorkerOneTime()
        //startWorkerChaining()
        uploadImageForeground()
    }

    private fun uploadImageForeground(){
        ImageUploadWorker.View().startWork(this)
        ImageUploadWorker.View().observer(this, this)
    }

    private fun startWorkerOneTime() {
        //enqueueOneTimeWork<UploadWorker>(this,"My File Path")
        //val id = enqueueOneTimeWork<UploadWorkerCoroutine>(this,"My File Path")
        //observerWork(this, this, id)
    }

    private fun startWorkerChaining() {
        enqueueProcessImageAndUpload(this,this,"My File Path")
    }
    override fun onRestart() {
        super.onRestart()
        log(object {}.javaClass.enclosingMethod?.name ?: "")
    }

    private fun coroutine() {
        //crLazy()
        //coroutinePrintTwice()
        //ensureCancel()
        //jobCancelChildren()
        //failJobChildren()
        //MyCoroutine().asyncExampleWithoutSupervisor()
        //MyCoroutine().asyncExampleWithSuperVisorScope()
        //MyCoroutine().cehExample()
        MyCoroutine().cehExampleAsync()
    }

    private fun crLazy(){
        MyCoroutine().crLazy()
    }
    private fun coroutinePrintTwice(){
        MyCoroutine().cooperative(lifecycleScope)
    }

    private fun ensureCancel(){
        MyCoroutine().ensure(lifecycleScope)
    }

    private fun jobCancelChildren(){
        MyCoroutine().cancelJobAndChildren()
    }

    private fun failJobChildren(){
        //MyCoroutine().failJobAndChildren()
        //MyCoroutine().failJobAndChildren2()
        //MyCoroutine().asyncLaunchException()
        //MyCoroutine().cehExample()
    }







    fun lifeCycleTransparent() {
        //checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 111)
        //createChooser()
        //MyDialogFragment().show(supportFragmentManager,"")
    }

    /*
    onPause will be called
    */

    private fun createChooser() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse("http://www.stackoverflow.com"))
        val title = "Chooser"
        val chooser = Intent.createChooser(intent, title)
        startActivity(chooser)
    }

    /*
     onPause will be called
     */

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
        } else {
            Toast.makeText(this@MainActivity, "Permission already granted", Toast.LENGTH_SHORT)
                .show()
        }
    }
}
