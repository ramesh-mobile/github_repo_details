package com.example.githubrepodetailsapp.data.services

import android.R
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.githubrepodetailsapp.GithubProjectApplication
import com.example.githubrepodetailsapp.data.model.responses.RepoModel
import com.example.githubrepodetailsapp.data.network.NetworkSinglton
import com.example.githubrepodetailsapp.data.network.NoInternetException
import com.example.githubrepodetailsapp.data.repository.db_repo.RepositoryDatabase
import com.example.githubrepodetailsapp.ui.home.repo_list.RepositoryListActivity
import com.example.githubrepodetailsapp.utils.CorountinesUtils
import com.example.githubrepodetailsapp.utils.Constants
import retrofit2.Response
import java.util.*


/**
 * Created by ramesh on 20-07-2021
 */
class FetchRepoService :Service() {

    private val TAG = "FetchRepoService"

    private var broadcastIntent: Intent? = null

    private var repositoryDatabase : RepositoryDatabase? = null
    private val map: MutableMap<String?, String?>? = HashMap()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun initMap() {
        map?.apply {
            put("q", "created:>2021-07-20")
            put("sort", "stars")
            put("order", "desc")
            put("page", "1")
        }
    }

    var timer = Timer()
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        startForeground("Service is running")
        repositoryDatabase = GithubProjectApplication.getDatabase()
        broadcastIntent = Intent(RepositoryListActivity.mBroadcastGitRepoServiceAction)
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                getRepository()
            }
        }, 0, Constants.TIME_TO_SYNC)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    fun getRepository(){
        initMap()
        CorountinesUtils.io {
            var response : Response<RepoModel>?  = null
            try {
                response = NetworkSinglton.instance?.getRepos(map as Map<String?, String?>?)

                if(response?.isSuccessful == true){
                    Log.d(TAG, "getRepository: response is successsfull")

                    var repositoryModel : RepoModel? = response.body()

                    repositoryDatabase?.RepoDao()?.insertRepo(repositoryModel)

                    broadcastIntent?.putExtra(Constants.DATA_UPDATED, true)
                    sendBroadcast(broadcastIntent)
                }
                else{
                    Log.d(TAG, "getRepository: resopnse is failed ${response?.errorBody().toString()}")
                }
            }catch (ne : NoInternetException){
                Log.e(TAG, "getRepository: ${ne.errorMessage}")
            }catch (e : Exception){Log.e(TAG, "getRepository: ${e.message}")}

        }
    }

    private fun startForeground(msg: String) {
        val notificationIntent = Intent(this, RepositoryListActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0)

        val notification: Notification = NotificationCompat.Builder(
            this,
            GithubProjectApplication.CHANNEL_ID
        )
            .setContentTitle("Github Repository Details")
            .setContentText(msg)
            .setSmallIcon(R.drawable.radiobutton_on_background)
            .setContentIntent(pendingIntent)
            .build()

        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                GithubProjectApplication.CHANNEL_ID,
                GithubProjectApplication.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mNotificationManager.createNotificationChannel(channel)
            NotificationCompat.Builder(this, GithubProjectApplication.CHANNEL_ID)
        }
        startForeground(1, notification);
    }



}