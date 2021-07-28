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
class RepositoryListActivity : AppCompatActivity() ,RepositoryListListener,RepositoryAdapter.RepoAdapterListener{
    private val TAG = "RepositoryListActivity"

    lateinit var viewModel : RepositoryListViewModel

    var mIntentFilterGeofence: IntentFilter? = null

    var repoList : MutableList<ItemModel?>? = mutableListOf()
    lateinit var repositoryAdapter: RepositoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //get view model object from ViewModelProviders
        viewModel = ViewModelProviders.of(this).get(RepositoryListViewModel::class.java)

        viewModel.repositoryListListener = this

        registerReciever()

        initialzeRecycler()

        viewModel.getDatabaseRepository()

        if(!isMyServiceRunning(FetchRepoService::class.java)) {
            val serviceIntent = Intent(this, FetchRepoService::class.java)
            serviceIntent.putExtra("inputExtra", "passing any text")
            ContextCompat.startForegroundService(this.applicationContext, serviceIntent)
        }
    }

    fun registerReciever(){
        mIntentFilterGeofence = IntentFilter()
        mIntentFilterGeofence?.addAction(mBroadcastGeofenceAction)
        registerReceiver(mReceiverGeofence, mIntentFilterGeofence)

    }

    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            return (serviceClass.name == service.service.className)
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiverGeofence)
    }

    companion object{
        val mBroadcastGeofenceAction = "com.avatarins.avatarvendormanagement.broadcast.string.for.geofence"
    }

    var mReceiverGeofence: BroadcastReceiver = object : BroadcastReceiver() {
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
        recycler_view.setHasFixedSize(true)
        recycler_view.setItemViewCacheSize(20)
        recycler_view.setDrawingCacheEnabled(true)
        recycler_view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)
        recycler_view.setItemAnimator(DefaultItemAnimator())
        recycler_view.addItemDecoration(
                DividerItemDecoration(this,
                        DividerItemDecoration.VERTICAL)
        )
        recycler_view.setLayoutManager(GridLayoutManager(this, 1))
        recycler_view.setAdapter(repositoryAdapter)
    }

    override fun onFetchSuccess(repoModel: RepoModel?) {
        repoList?.clear()
        print(TAG, "updated list is : ${repoModel}")
        repoList?.addAll(repoModel?.itemModel!!)
        repositoryAdapter.notifyDataSetChanged()
    }

    override fun onFetchFailure(errorMessage: String) {
        print(TAG, errorMessage)
    }

    override fun onItemClicked(itemModel: ItemModel?) {
        var intent = Intent(this, RepoDetailsActivity::class.java)
        intent.putExtra(Constants.ITEM_DETAILS, itemModel)
        startActivity(intent)
    }
}