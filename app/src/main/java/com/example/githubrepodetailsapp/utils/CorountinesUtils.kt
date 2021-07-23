package com.example.githubrepodetailsapp.utils

/**
 * Created by ramesh on 20-07-2021
 */
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object CorountinesUtils {
    private const val TAG = "CorountinesUtils"
    fun main(work : suspend(()-> Unit) ){
        CoroutineScope(Dispatchers.Main).launch {
            Log.d(TAG, "main checkVersion dispatch")
            work()
        }
    }

    fun back(work : suspend(()-> Unit) ){
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "main checkVersion dispatch")
            work()
        }
    }


}