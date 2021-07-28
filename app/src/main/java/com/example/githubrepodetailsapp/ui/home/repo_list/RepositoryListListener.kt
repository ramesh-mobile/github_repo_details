package com.example.githubrepodetailsapp.ui.home.repo_list

import com.example.githubrepodetailsapp.data.model.responses.RepoModel

/**
 * Created by ramesh on 21-07-2021
 */
interface RepositoryListListener  {
    fun onFetchSuccess(repositoryModel : RepoModel?)
    fun onFetchFailure(errorMessage : String)
}