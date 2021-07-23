package com.example.githubrepodetailsapp.data.model.responses

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.githubrepodetailsapp.data.repository.db_repo.DataConverter
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by ramesh on 22-07-2021
 */
@Entity
data class LicenseModel(
    @PrimaryKey(autoGenerate = false)
    var lid : Int,

    @TypeConverters(DataConverter::class)
    @SerializedName("name")
    @Expose
    val name: String? = null

): Serializable{
    constructor(): this(1,"Not Available")
}