package com.example.myapplication3.workManager

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay
import java.util.UUID
import java.util.concurrent.TimeUnit

// Solution for `Deferrable` `Guaranteed` execution
/*

                   Exact Timing
      ThreadPool    |   Foreground Services
                    |
Best Effort-------------------Guaranteed Execution
                    |   [ JobSc, JobD , AM, BCR ] together created as WorkManager
      ThreadPool    |
                 Deferrable
 */
class UploadWorker(context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {
    override fun doWork(): Result {
        val filePath = inputData.getString("filePath")
        Log.d(WORK_MANAGER_TAG, "Upload Success $filePath ${Thread.currentThread()}") // Thread[androidx.work-1,5,main]
        return Result.success()
    }

}

class UploadWorkerCoroutine(context: Context, workerParams: WorkerParameters): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val uriPath = inputData.getString("uri")
        Log.d(WORK_MANAGER_TAG, "Upload Started $uriPath ${Thread.currentThread()}") // Thread[DefaultDispatcher-worker-2,5,main]
        delay(2000)
        Log.d(WORK_MANAGER_TAG, "Upload Success $uriPath")
        return Result.success(workDataOf("status" to "DONE"))
    }
}

class ProcessImageWorker(context: Context, workerParams: WorkerParameters): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val filePath = inputData.getString("filePath")
        Log.d(WORK_MANAGER_TAG, "Image Process $filePath")
        delay(2000)
        Log.d(WORK_MANAGER_TAG, "Image Process Success $filePath")
        return Result.success(workDataOf("uri" to "content://"))
    }

}

const val WORK_MANAGER_TAG = "My Work Manager"
inline fun <reified WC: ListenableWorker> enqueueOneTimeWork(context: Context, filePath: String) : UUID{
    val work  = getOneTimeWorkerRequest<WC>(filePath)
    WorkManager.getInstance(context).enqueue(work)
    return work.id
}

inline fun <reified WC: ListenableWorker> getOneTimeWorkerRequest(filePath: String) : WorkRequest {
    return OneTimeWorkRequestBuilder<WC>()
        .setInputData(workDataOf("filePath" to filePath))
        .setInitialDelay(1, TimeUnit.SECONDS)
        .setConstraints(Constraints(NetworkType.CONNECTED))
        // Linear, Exponential // minimum 10 seconds is limit at least
        .setBackoffCriteria(BackoffPolicy.LINEAR,  10, TimeUnit.SECONDS)
        .build()
}

fun enqueueProcessImageAndUpload(lifecycleOwner: LifecycleOwner, context: Context, filePath: String) {
    WorkManager.getInstance(context)
        .beginWith(getProcessImagedWork(filePath))// Return Work Continuation Object can pass Around
        .then(getImagedUploadWork(filePath)) // Return Work Continuation Object  can pass Around
        .enqueue()

    val wc1 = WorkManager.getInstance(context)
        .beginWith(getProcessImagedWork(filePath))
    wc1.workInfosLiveData.observe(lifecycleOwner) {
        it.forEach {info ->
            Log.d(WORK_MANAGER_TAG, ": ${info.tags} : ${info.state}")
        }
    }
    val wc2 = wc1.then(getImagedUploadWork(filePath))
    wc2.workInfosLiveData.observe(lifecycleOwner) {
        it.forEach { info ->
            Log.d(WORK_MANAGER_TAG, ": ${info.tags} : ${info.state}")
        }
    }
    wc2.enqueue()
}

fun getProcessImagedWork(filePath: String) : OneTimeWorkRequest {
    return OneTimeWorkRequestBuilder<ProcessImageWorker>()
        .setInputData(workDataOf("filePath" to filePath))
        .addTag("tag_process_image")
        .setConstraints(Constraints.Builder().setRequiresStorageNotLow(true).build())
        .setInitialDelay(1, TimeUnit.SECONDS)
        // Linear, Exponential // minimum 10 seconds is limit at least
        .setBackoffCriteria(BackoffPolicy.LINEAR,  10, TimeUnit.SECONDS)
        .build()
}
fun getImagedUploadWork(filePath: String) : OneTimeWorkRequest {
    return OneTimeWorkRequestBuilder<UploadWorkerCoroutine>()
        .setInputData(workDataOf("filePath" to filePath))
        .addTag("tag_upload_image")
        .setInitialDelay(1, TimeUnit.SECONDS)
        .setConstraints(Constraints(NetworkType.CONNECTED))
        // Linear, Exponential // minimum 10 seconds is limit at least
        .setBackoffCriteria(BackoffPolicy.LINEAR,  10, TimeUnit.SECONDS)
        .build()
}

// WorkInfo.mState: ENQUEUED, RUNNING, FAILED, CANCELLED, SUCCEEDED, BLOCKED
// Tags Should be used to schedule work for more human readable and understanding rather than random UUID
// Tags namespace your work, but also namespace using library or modules when we move from one version to another
// we cancel the old work. Hence tags are useful.
fun observerWork(context: Context, lifecycleOwner: LifecycleOwner, id: UUID) {
    val status : LiveData<WorkInfo> = WorkManager.getInstance(context).getWorkInfoByIdLiveData(id)
    status.observe(lifecycleOwner) {
        if (it.state.isFinished) {
            Toast.makeText(context, "status ${it.outputData.getString("status")}", Toast.LENGTH_LONG).show()
        }
    }
}

// Best Practices -> For tasks that can survive process death
/*
OK : Upload media to Server
OK : Parse Data and store in DB
NOT OK : Extract color and update view (Use Thread, RX, Cr)
NOT OK : Parse Data and Update View (Use Thread , RX)
NOT OK : Payment Transaction (Use Foreground Service)
 */
