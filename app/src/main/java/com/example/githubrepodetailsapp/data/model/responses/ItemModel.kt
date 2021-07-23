package com.example.githubrepodetailsapp.data.model.responses

import androidx.room.ColumnInfo
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
data class ItemModel(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "rid")
    var rid : Int,

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    var name: String? = null,

    @SerializedName("description")
    @Expose
    @ColumnInfo(name = "description")
    var description: String? = null,

    @SerializedName("language")
    @Expose
    @ColumnInfo(name = "language")
    var language: String? = null,

    @SerializedName("stargazers_count")
    @Expose
    @ColumnInfo(name = "stargazers_count")
    var starCount: Int? = null,

    @TypeConverters(DataConverter::class)
    @SerializedName("license")
    @Expose
    @ColumnInfo(name = "license")
    var licenses: LicenseModel? = null,

    @TypeConverters(DataConverter::class)
    @SerializedName("owner")
    @Expose
    @ColumnInfo(name = "owner")
    var ownerModel : OwnerModel? = null

) : Serializable
