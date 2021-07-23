package com.example.githubrepodetailsapp

import android.app.Application
import androidx.room.Room
import com.example.githubrepodetailsapp.data.repository.db_repo.RepositoryDatabase


/**
 * Created by ramesh on 19-07-2021
 */
class GithubProjectApplication : Application(){

    private val TAG = "GithubProjectApplicatio"

    companion object {
        lateinit var baseContext : GithubProjectApplication

        val CHANNEL_ID = "autoStartServiceChannel"
        val CHANNEL_NAME = "Auto Start Service Channel"

        var db: RepositoryDatabase? = null
        fun getDatabase(): RepositoryDatabase? {
            return db
        }
    }


    override fun onCreate() {
        super.onCreate()
        GithubProjectApplication.baseContext = this
        db= Room.databaseBuilder(
            applicationContext,
            RepositoryDatabase::class.java,
            "repomodel"/*"RepositoryDatabase"*/
        ).allowMainThreadQueries().build()
    }
}