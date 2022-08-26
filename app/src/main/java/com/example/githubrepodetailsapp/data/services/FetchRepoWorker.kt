package com.example.githubrepodetailsapp.data.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.githubrepodetailsapp.GithubProjectApplication
import com.example.githubrepodetailsapp.R
import com.example.githubrepodetailsapp.data.model.responses.RepoModel
import com.example.githubrepodetailsapp.data.network.NetworkSinglton
import com.example.githubrepodetailsapp.data.network.NoInternetException
import com.example.githubrepodetailsapp.data.repository.db_repo.RepositoryDatabase
import com.example.githubrepodetailsapp.ui.home.repo_list.RepositoryListActivity
import com.example.githubrepodetailsapp.utils.Constants
import kotlinx.coroutines.coroutineScope
import retrofit2.Response
import java.util.*

class FetchRepoWorker(val context:Context, workerParameters: WorkerParameters) : CoroutineWorker(context,workerParameters) {

    private val TAG = "FetchRepoWorker"

    private var repositoryDatabase : RepositoryDatabase? = null
    private val map: MutableMap<String?, String?> = HashMap()

    var fetchPage = "1"
    override suspend fun doWork(): Result {
        return try {
            fetchPage = inputData.getInt(Constants.PAGE_COUNT,1).toString()
            fetchRepo()
            Result.success(Data.Builder().apply {
                putBoolean(Constants.DATA_UPDATED,true)
            }.build())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private suspend fun fetchRepo() {
        print("initializeWorker: in worker")
        startForegroundNotification()
        getRepository()
    }

    private fun initMap() {
        map.apply {
            put("q", "created:>2021-07-20")
            put("sort", "stars")
            put("order", "desc")
            put("page", fetchPage)
            put("limit", "20")
        }
    }

    private fun startForegroundNotification(msg: String = "Service is running") {
        val notificationIntent = Intent(context, RepositoryListActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context,0, notificationIntent, 0)

        val notification: Notification = NotificationCompat.Builder(context,GithubProjectApplication.CHANNEL_ID)
            .setContentTitle("Github Repository Details")
            .setContentText(msg)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .build()

        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                GithubProjectApplication.CHANNEL_ID,
                GithubProjectApplication.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mNotificationManager.createNotificationChannel(channel)
            NotificationCompat.Builder(context, GithubProjectApplication.CHANNEL_ID)
        }
        mNotificationManager.notify(1, notification)
    }

    suspend fun getRepository(){
        initMap()
        //CorountinesUtils.io {  }
        coroutineScope {
            try {
                var response : Response<RepoModel>? = null
                response = NetworkSinglton.instance?.getRepos(map as Map<String?, String?>?)
                if(response?.isSuccessful == true){
                    Log.d(TAG, "getRepository: response is successful")

                    val repositoryModel : RepoModel? = response.body()
                    repositoryDatabase?.RepoDao()?.insertRepo(repositoryModel)
                }
                else{
                    Log.e(TAG, "getRepository: response is failed ${response?.errorBody().toString()}")
                }
            }catch (ne : NoInternetException){
                Log.e(TAG, "getRepository: ${ne.errorMessage}")
            }catch (e : Exception){
                Log.e(TAG, "getRepository: ${e.message}")}
        }
    }
}