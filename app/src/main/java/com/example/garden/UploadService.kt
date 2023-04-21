package com.example.garden

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import com.example.garden.domain.model.VideoDetails
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class UploadService : Service() {

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.IO)


    @Inject
    @Named("feed_ref")
    lateinit var dbRef: DatabaseReference

    @Inject
    lateinit var storageRef: StorageReference
    private lateinit var notificationManager: NotificationManager
    private var notificationBuilder: Notification.Builder? = null

    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        println(dbRef)
        println(storageRef)
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(NotificationChannel(
            "upload",
            "Upload",
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = ""
        })
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val title = intent.getStringExtra("title")!!
        val videoLink = intent.getStringExtra("videoLink")!!
        val description = intent.getStringExtra("description")!!
        val images = intent.getStringArrayListExtra("images")!!

        val key = dbRef.push().key!!
        val downloads = mutableListOf<String>()

        uploadFile(storageRef.child(key), images.map { Uri.parse(it) }, 0, downloads)

        showNotification("Saving to database")
        println(downloads)

        dbRef.child(key).setValue(
            VideoDetails(
                title = title,
                videoLink = videoLink,
                description = description,
                images = downloads.toList(),
                thumb = downloads.firstOrNull() ?: ""
            )
        ).addOnCompleteListener {
//            stopSelf()
        }

        ///[KARTIKEY, SAHU, 0]


//        startForeground(NOTIFICATION_ID, buildNotification())
//
//
////        coroutineScope.launch(Dispatchers.IO) {
////        try {
//        val stRef = storageRef.child("wefjifow")
//        val downloads = mutableListOf<String>()
//
//        images.forEachIndexed { i, it ->
        val fileName = UUID.randomUUID().toString()
//            val fileRef = stRef.child(fileName)
//
//            fileRef.putFile(Uri.parse(it)).addOnProgressListener {
//                val progress = (100.0 * it.bytesTransferred / it.totalByteCount).toInt()
//                updateNotification(progress, i, images.size)
////                notification.setProgress(100, progress, false)
////                notificationManager.notify(1, notification.build())
//            }.addOnSuccessListener {
//                fileRef.downloadUrl.addOnSuccessListener { download ->
//                    downloads.add(download.toString())
//                }
//            }
//        }
//
////            downloads.addAll(uploads.awaitAll())
////        }
////        } finally {
//        stopSelf()
////        }
////        }
//
        return START_STICKY
    }

//    private fun buildNotification(): Notification {
//        notificationBuilder = Notification.Builder(this, "upload").setSmallIcon(R.drawable.ic_secure)
//            .setContentTitle("Uploading").setContentText("").setProgress(100, 0, false)
//            .setOngoing(true)
//
//        return notification.build()
//    }

//    private fun updateNotification(progress: Int, i: Int, n: Int) {
//        notification.setProgress(100, progress, false).setContentText("$progress% complete").setContentTitle("Uploading $i of $n")
//        notificationManager.notify(NOTIFICATION_ID, notification.build())
//    }

    private fun uploadFile(
        ref: StorageReference,
        uris: List<Uri>,
        index: Int,
        downloads: MutableList<String>,
    ) {
        if (index >= uris.size) {
//            stopSelf()
            return
        }

        val uri = uris[index]

//        showNotification("Uploading file ${index + 1} of ${uris.size}")

        // Upload the file to Firebase Storage

        val fileName = UUID.randomUUID().toString()
//        val stRef = storageRef.child("wefjifow")
        val fileRef = ref.child(fileName)
        val uploadTask = fileRef.putFile(uri)

        // Monitor the upload progress
        uploadTask.addOnProgressListener { taskSnapshot ->
            val progress =
                (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()

            // Update the notification with the upload progress
            notificationBuilder?.setContentText("$progress%")
            notificationBuilder?.setContentTitle("Uploading ${index + 1} / ${uris.size}")
            notificationBuilder?.setProgress(100, progress, false)
            startForeground(1, notificationBuilder?.build())
        }

        // Handle the upload completion or failure
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }

            fileRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // File uploaded successfully, move on to the next one
                downloads.add(task.result.toString())
                uploadFile(ref, uris, index + 1, downloads)
            } else {
                // File upload failed, show an error notification and stop the service
                showNotification("Upload failed")
//                stopSelf()
            }
        }
    }

    private fun showNotification(text: String) {
        if (notificationBuilder == null) {
            notificationBuilder = Notification.Builder(this, "upload")
                .setSmallIcon(R.drawable.ic_menu_upload_you_tube).setContentTitle("Uploading")
                .setOnlyAlertOnce(true).setContentText("").setProgress(100, 0, false)
        } else {
            notificationBuilder?.setContentText(text)
        }

        startForeground(1, notificationBuilder?.build())
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        private const val NOTIFICATION_ID = 1
    }
}