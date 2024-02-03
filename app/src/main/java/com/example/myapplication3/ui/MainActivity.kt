package com.example.myapplication3.ui

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.myapplication3.coroutines.MyCoroutine
import com.example.myapplication3.extensions.log
import com.example.myapplication3.workManager.UploadWorker
import com.example.myapplication3.workManager.UploadWorkerCoroutine
import com.example.myapplication3.workManager.enqueueOneTimeWork
import com.example.myapplication3.workManager.enqueueProcessImageAndUpload
import com.example.myapplication3.workManager.observerWork
import kotlinx.coroutines.launch
import java.util.UUID


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.myapplication3.R.layout.activity_main)
        if (savedInstanceState == null) {
            // Fragment
            supportFragmentManager
                .beginTransaction()
                .replace(com.example.myapplication3.R.id.content_frame, MainFragment.getInstance())
                .commit()


            // Work Manager
            //workManager()

            coroutine()
        }
    }





    private fun workManager() {
        // Work Manager
        //startWorkerOneTime()
        startWorkerChaining()
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
        crLazy()
        //coroutinePrintTwice()
        //ensureCancel()
        //jobCancelChildren()
        //failJobChildren()
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
}
