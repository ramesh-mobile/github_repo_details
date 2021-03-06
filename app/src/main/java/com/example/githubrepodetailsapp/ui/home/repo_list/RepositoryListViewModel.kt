package com.example.githubrepodetailsapp.ui.home.repo_list

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubrepodetailsapp.GithubProjectApplication
import com.example.githubrepodetailsapp.data.model.responses.ItemModel
import com.example.githubrepodetailsapp.utils.CorountinesUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by ramesh on 20-07-2021
 */
class RepositoryListViewModel : ViewModel() {
    private val TAG = "RepositoryListViewModel"
    var repositoryListListener : RepositoryListListener? = null

    fun getDatabaseRepository(){
        CorountinesUtils.main {
            //get Database object
            var repositoryDatabase = GithubProjectApplication.getDatabase()

            //get all items from database
            var repoModel = repositoryDatabase?.RepoDao()?.getAllItems()

            if(repoModel!=null)
                repositoryListListener?.onFetchSuccess(repoModel)
            else
                repositoryListListener?.onFetchFailure("Could not find any data")

        }
    }
}


