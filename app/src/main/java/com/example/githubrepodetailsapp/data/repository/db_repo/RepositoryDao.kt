package com.example.githubrepodetailsapp.data.repository.db_repo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubrepodetailsapp.data.model.responses.RepoModel

/**
 * Created by ramesh on 22-07-2021
 */
@Dao
interface RepositoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepo(repoModel: RepoModel?)

    @Query("select * from RepoModel order by rId desc limit 1")
    suspend fun getAllItems() : RepoModel?

}