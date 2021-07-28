package com.example.githubrepodetailsapp.data.repository.db_repo

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.githubrepodetailsapp.data.model.responses.ItemModel
import com.example.githubrepodetailsapp.data.model.responses.LicenseModel
import com.example.githubrepodetailsapp.data.model.responses.OwnerModel
import com.example.githubrepodetailsapp.data.model.responses.RepoModel

/**
 * Created by ramesh on 22-07-2021
 */
@Database(entities = [RepoModel::class,ItemModel::class, OwnerModel::class,LicenseModel::class], version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class RepositoryDatabase : RoomDatabase() {
    abstract fun RepoDao(): RepositoryDao?
}