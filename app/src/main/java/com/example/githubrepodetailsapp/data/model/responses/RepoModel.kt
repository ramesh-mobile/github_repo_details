package com.example.githubrepodetailsapp.data.model.responses

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.githubrepodetailsapp.data.repository.db_repo.DataConverter
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * Created by ramesh on 22-07-2021
 */
@Parcelize
@Entity
data class RepoModel(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "rId")
    var rid: Long = 1,

    @TypeConverters(DataConverter::class)
    @SerializedName("items")
    @Expose
    val itemModel : List<ItemModel?>? = null,

) : Parcelable