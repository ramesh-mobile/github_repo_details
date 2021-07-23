package com.example.githubrepodetailsapp.data.model.responses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by ramesh on 22-07-2021
 */
@Entity
public data class OwnerModel(

    @PrimaryKey(autoGenerate = false)
    var rId : Int,

    @SerializedName("login") @Expose
    var name: String? = null,

    @SerializedName("avatar_url")
    @Expose
    var avatarUrl: String? = null,

    @SerializedName("html_url")
    @Expose
    @ColumnInfo(name ="html_url")
    var htmlUrl: String? = null,

) : Serializable {
constructor() : this(1,"ramesh","","")

}