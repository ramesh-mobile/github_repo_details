package com.example.githubrepodetailsapp.data.repository.db_repo

import androidx.room.TypeConverter
import com.example.githubrepodetailsapp.data.model.responses.ItemModel
import com.example.githubrepodetailsapp.data.model.responses.LicenseModel
import com.example.githubrepodetailsapp.data.model.responses.OwnerModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.lang.reflect.Type


/**
 * Created by ramesh on 22-07-2021
 */
class DataConverter : Serializable{

    companion object{
        val gson : Gson = Gson()
    }

    @TypeConverter
    fun objectToString(someObjects: Object): String? {
        return gson.toJson(
            someObjects
        )
    }

    @TypeConverter
    fun stringToGroupModel(data: String?): LicenseModel? {
        if (data == null) {
            return LicenseModel()
        }
        val listType = object : TypeToken<Collection<LicenseModel?>?>() {}.type
        return gson.fromJson(data, listType )
    }

    @TypeConverter
    fun stringToOwner(data: String?): OwnerModel? {
        if (data == null) {
            return OwnerModel()
        }
        val listType = object : TypeToken<Collection<OwnerModel?>?>() {}.type
        return gson.fromJson(data, listType )
    }

    @TypeConverter
    fun itemModelListToString(itemModels : List<ItemModel?>?) : String?{
        if(itemModels==null) return null
        val type: Type = object : TypeToken<List<ItemModel>>() {}.type
        val jsonString : String = gson.toJson(itemModels, type)
        return jsonString
    }


    @TypeConverter
    fun stringToItemModelList(data: String?): List<ItemModel?>? {
        if (data == null) {
            return emptyList<ItemModel>()
        }
        val listType =
            object : TypeToken<Collection<ItemModel?>?>() {}.type
        return gson.fromJson( data, listType )
    }
}