package com.example.myapplication3.workManager

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleOwner
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.myapplication3.R
import com.example.myapplication3.ui.MainActivity
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

class ImageUploadWorker(private val context: Context, private val workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    private val TAG = "ImageUploadWorker"
    private val dispatcher = Dispatchers.IO + SupervisorJob()

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result = coroutineScope {
        val imagePaths = workerParams.inputData.getStringArray(KEY_IMAGE_PATHS) ?: return@coroutineScope Result.failure()
        val imageIds = workerParams.inputData.getIntArray(KEY_IMAGE_IDS) ?: return@coroutineScope Result.failure()

        val uploadResults = mutableListOf<UploadResult>()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val foregroundNotification = createForegroundNotification(context)
        setForegroundAsync(ForegroundInfo(KEY_NOTIFICATION_ID, foregroundNotification))

        val deferredUploads = imagePaths.mapIndexed { index, imagePath ->
            async(dispatcher) {
                setProgress(workDataOf(KEY_UPLOAD_PROGRESS to 0))
                val result = uploadImage(imageIds[index],imagePath)
                notificationManager.notify(KEY_NOTIFICATION_ID, createNotification(context, result).build())
                setProgress(workDataOf(KEY_UPLOAD_PROGRESS to 100))
                result
            }
        }

        uploadResults.addAll(deferredUploads.awaitAll())
        return@coroutineScope Result.success()
    }

    private suspend fun uploadImage(imageId: Int, imagePath: String): UploadResult {
        val file = File(imagePath)
        val imageData = withContext(Dispatchers.IO) {
            //FileInputStream(file).readBytes()
            imagePath
        }

        // Replace with your actual server upload logic
        val response = try {

            /*

            val uploadUrl = "url"
            val requestBody = MultipartBody.Part.createFormData("image", file.name, RequestBody.create(
                MediaType.parse("image/*"), imageData))
            val response = Retrofit.Builder()
                .baseUrl(uploadUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UploadApiService::class.java)
                .uploadImage(requestBody)
                .await()*/

             */

            //setProgressAsync(Data.Builder().putInt(KEY_UPLOAD_PROGRESS, uploaded).build())

            val response = fakeNetworkCall(imageId, imageData).await()
            if (response.success) {
                UploadResult.Success(imageId)
            } else {
                UploadResult.Failure(imageId, 1000 .toString())
            }
        } catch (e: Exception) {
            UploadResult.Failure(imageId, e.localizedMessage ?: "Unknown error")
        }

        return response
    }

    private suspend fun fakeNetworkCall(imageId : Int, imageData: String) : Deferred<UploadResponse> {
        return withContext(dispatcher){
            async {
                delay(imageId.toLong())
                UploadResponse(true,"Success")
            }
        }
    }

    private fun createNotification(
        context: Context,
        result: UploadResult
    ): NotificationCompat.Builder {
        val title = if (result.isSuccess) "Image Upload Successful" else "Image Upload Failed"
        val message = if (result.isSuccess) "Image uploaded successfully ${result.imageId}" else "Failed to upload image."

        val intent = Intent(context, MainActivity::class.java) // Replace with your activity
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background) // Replace with your icon
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createForegroundNotification(context: Context): Notification {
        val channel = NotificationChannel(CHANNEL_ID, "Image Upload", NotificationManager.IMPORTANCE_LOW)
        channel.description = "Notification for ongoing image upload"
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)

        val pendingIntent = PendingIntent.getActivity(context, 0, Intent(), PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Uploading Images")
            .setContentText("Uploading...")
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    companion object {
        const val KEY_IMAGE_PATHS = "image_paths"
        const val KEY_IMAGE_IDS = "image_ids"
        const val CHANNEL_ID = "image_upload_channel"
        const val KEY_UPLOAD_PROGRESS = "image_upload_progress"
        const val KEY_UPLOAD_TAG = "image_upload"
        const val KEY_NOTIFICATION_ID = 12345

        @SuppressLint("EnqueueWork")
        fun enqueue(context: Context, imageIds: List<Int>, imagePaths: List<String>) {
            val workRequest = OneTimeWorkRequest.Builder(ImageUploadWorker::class.java)
                .addTag(KEY_UPLOAD_TAG)
                .setInputData(workDataOf(
                    KEY_IMAGE_IDS to imageIds.toTypedArray(),
                    KEY_IMAGE_PATHS to imagePaths.toTypedArray()))
                .build()
            //WorkManager.getInstance(context).enqueue(workRequest)
            WorkManager.getInstance(context).beginUniqueWork(KEY_UPLOAD_TAG, ExistingWorkPolicy.REPLACE, workRequest).enqueue()
        }
    }


    class View {

        fun startWork(context: Context) {
            enqueue(context, listOf(5000,10000,15000), listOf("A","B", "C"))
        }

        fun observer(context: Context, lifecycleOwner: LifecycleOwner) {
            val liveWorkInfos = WorkManager.getInstance(context)
                .getWorkInfosByTagLiveData(KEY_UPLOAD_TAG)

            liveWorkInfos.observe(lifecycleOwner) { workInfos ->
                if (workInfos != null) {
                    for (workInfo in workInfos) {
                        val imageId = workInfo.id.toString()
                        if (workInfo.state == WorkInfo.State.RUNNING) {
                            val progress = workInfo.progress.getInt(KEY_UPLOAD_PROGRESS, 0)
                            Toast.makeText(context, "Progress $progress", Toast.LENGTH_SHORT).show()
                        } else if (workInfo.state == WorkInfo.State.SUCCEEDED || workInfo.state == WorkInfo.State.FAILED) {
                            Toast.makeText(context, "$imageId ${workInfo.state}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }
    }
}

data class UploadResult(val imageId: Int, val isSuccess: Boolean, val errorMessage: String? = null) {

    companion object {
        fun Success(imageId: Int): UploadResult {
            return UploadResult(imageId, true)
        }

        fun Failure(imageId: Int, errorMessage: String): UploadResult {
            return UploadResult(imageId, false, errorMessage)
        }
    }
}

interface UploadApiService {

    @Multipart
    @POST("/upload")
    suspend fun uploadImage(@Part("image") image: MultipartBody.Part): Deferred<Response<UploadResponse>>

}

data class UploadResponse(val success: Boolean, val message: String?, val imageId: String? = null)

