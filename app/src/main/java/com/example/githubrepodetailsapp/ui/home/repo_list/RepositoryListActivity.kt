package com.example.githubrepodetailsapp.ui.home.repo_list

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.*
import androidx.work.*
import com.example.githubrepodetailsapp.R
import com.example.githubrepodetailsapp.data.model.responses.ItemModel
import com.example.githubrepodetailsapp.data.model.responses.RepoModel
import com.example.githubrepodetailsapp.data.services.FetchRepoWorker
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

    var repoList : MutableList<ItemModel?>? = mutableListOf()

    lateinit var repositoryAdapter: RepositoryAdapter

    var pageCount = 3
    var lastPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //get view model object from ViewModelProviders
        viewModel = ViewModelProviders.of(this).get(RepositoryListViewModel::class.java)

        initializeRecycler()

        setListeners()

        initializeWorker()

        viewModel.getDatabaseRepository()

        viewModel.getRepoLiveData().observe(this, Observer {
            if(it!=null)
                onFetchSuccess(it)
            else
                onFetchFailure(getString(R.string.no_data_available))
        })
    }

    private fun setListeners() {
        nsv.setOnScrollChangeListener{v: NestedScrollView, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > 0) {
                val layoutManager = recycler_view.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.findLastCompletelyVisibleItemPosition() + 1
                if (visibleItemCount == layoutManager.itemCount && pageCount < lastPage) {
                    pageCount += 1
                    initializeWorker()
                }
            }
        }
    }

    private fun initializeWorker() {
        val workManager = WorkManager.getInstance(applicationContext)

        var data : Data = Data.Builder().apply {
            putInt(Constants.PAGE_COUNT,pageCount)
        }.build()

        //val downloadRepoWorker : WorkRequest = PeriodicWorkRequestBuilder<FetchRepoWorker>(15,TimeUnit.MINUTES).setInputData(data).build()
        val downloadRepoWorker = OneTimeWorkRequestBuilder<FetchRepoWorker>().setInputData(data).build()
        workManager.run {

            //cancelling pending workers
            //cancelWorkById(downloadRepoWorker.id)

            //observer for work manager
            getWorkInfoByIdLiveData(downloadRepoWorker.id).observe(this@RepositoryListActivity,{
                when(it.state){
                    WorkInfo.State.ENQUEUED->{
                        print(TAG, "initializeWorker: work is enqueued")
                    }
                    WorkInfo.State.SUCCEEDED->{

                        print(TAG, "initializeWorker: work is succeed")
                        onDataReceived(it.outputData.getBoolean(Constants.DATA_UPDATED,false))
                    }
                    WorkInfo.State.FAILED->{
                        print(TAG, "initializeWorker: work is failed")
                    }
                }
            })

            //enqueueUniqueWork("myworker",ExistingWorkPolicy.REPLACE,downloadRepoWorker)
            enqueue(downloadRepoWorker)
        }
    }

    private fun onDataReceived(isDataUpdated:Boolean){
        if(isDataUpdated){
            Toast.makeText(applicationContext, "data is updated", Toast.LENGTH_LONG).show()
            viewModel.getDatabaseRepository()
            print(TAG, "Data is updated")
        }
    }

    private fun initializeRecycler() {
        repositoryAdapter = RepositoryAdapter(repoList as List<ItemModel?>, this, this)
        recycler_view?.apply {
            /*setHasFixedSize(true)
            setDrawingCacheEnabled(true)
            setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)*/
            addItemDecoration(DividerItemDecoration(this@RepositoryListActivity,DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter = repositoryAdapter
        }

    }

    private fun onFetchSuccess(repoModel: RepoModel?) {
        repoList?.clear()
        print(TAG, "updated list is : ${repoModel}")
        repoList?.addAll(repoModel?.itemModel!!)
        repositoryAdapter.notifyDataSetChanged()
    }

    private fun onFetchFailure(errorMessage: String) {
        print(TAG, errorMessage)
    }

    override fun onItemClicked(itemModel: ItemModel?) {
        Intent(this, RepoDetailsActivity::class.java).apply {
            putExtra(Constants.ITEM_DETAILS, itemModel)
            startActivity(this)
        }
    }
}