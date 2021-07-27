package com.example.githubrepodetailsapp.ui.home.repo_list

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubrepodetailsapp.R
import com.example.githubrepodetailsapp.data.model.responses.ItemModel
import com.example.githubrepodetailsapp.data.model.responses.RepoModel
import com.example.githubrepodetailsapp.data.services.FetchRepoService
import com.example.githubrepodetailsapp.ui.home.repo_details.RepoDetailsActivity
import com.example.githubrepodetailsapp.utils.Constants
import com.example.githubrepodetailsapp.utils.print
import kotlinx.android.synthetic.main.recycler_list.*

/**
 * Created by ramesh on 20-07-2021
 */
class RepositoryListActivity : AppCompatActivity() ,RepositoryAdapter.RepoAdapterListener{
    private val TAG = "RepositoryListActivity"

    lateinit var viewModel : RepositoryListViewModel

    var mIntentFilterRepoService: IntentFilter? = null

    var repoList : MutableList<ItemModel?>? = mutableListOf()

    lateinit var repositoryAdapter: RepositoryAdapter

    companion object{
        const val mBroadcastGitRepoServiceAction = "com.avatarins.avatarvendormanagement.broadcast.string.for.git.repo.service"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //get view model object from ViewModelProviders
        viewModel = ViewModelProviders.of(this).get(RepositoryListViewModel::class.java)

        initialzeRecycler()

        mIntentFilterRepoService = IntentFilter()
        mIntentFilterRepoService?.addAction(mBroadcastGitRepoServiceAction)

        viewModel.getDatabaseRepository()

        viewModel.dataMutableLiveData.observe(this, Observer {
            if(it!=null)
                onFetchSuccess(it)
            else
                onFetchFailure(getString(R.string.no_data_available))
        })

        if(!isMyServiceRunning(FetchRepoService::class.java)) {
            Intent(this, FetchRepoService::class.java).apply {
                putExtra("inputExtra", "passing any text")
                ContextCompat.startForegroundService(this@RepositoryListActivity.applicationContext, this)
            }
        }
    }

    fun registerReciever(){
        registerReceiver(mReceiverGitRepoService, mIntentFilterRepoService)
    }

    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            return (serviceClass.name == service.service.className)
        }
        return false
    }

    override fun onStart() {
        super.onStart()
        registerReciever()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(mReceiverGitRepoService)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    var mReceiverGitRepoService: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var isDataUpdated = intent.getBooleanExtra(Constants.DATA_UPDATED, false)
            if(isDataUpdated){
                Toast.makeText(context, "data is updated", Toast.LENGTH_LONG).show()
                viewModel.getDatabaseRepository()
                print(TAG, "Data is updated")
            }
        }
    }

    private fun initialzeRecycler() {
        repositoryAdapter = RepositoryAdapter(repoList as List<ItemModel?>, this, this)
        recycler_view?.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            setDrawingCacheEnabled(true)
            setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)
            setItemAnimator(DefaultItemAnimator())
            addItemDecoration(DividerItemDecoration(this@RepositoryListActivity,DividerItemDecoration.VERTICAL))
            setLayoutManager(GridLayoutManager(this@RepositoryListActivity, 1))
            setAdapter(repositoryAdapter)
        }
    }

    fun onFetchSuccess(repoModel: RepoModel?) {
        repoList?.clear()
        print(TAG, "updated list is : ${repoModel}")
        repoList?.addAll(repoModel?.itemModel!!)
        repositoryAdapter.notifyDataSetChanged()
    }

    fun onFetchFailure(errorMessage: String) {
        print(TAG, errorMessage)
    }

    override fun onItemClicked(itemModel: ItemModel?) {
        Intent(this, RepoDetailsActivity::class.java).apply {
            putExtra(Constants.ITEM_DETAILS, itemModel)
            startActivity(this)
        }
    }
}