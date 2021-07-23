package com.example.githubrepodetailsapp.utils

import android.content.Context
import android.util.Log

/**
 * Created by ramesh on 20-07-2021
 */

private const val TAG = "ViewUtils"

fun Context.print(MESSAGE_TAG: String, message: String){
    Log.d(MESSAGE_TAG, message)
}

fun Context.error(MESSAGE_TAG: String, message: String){
    Log.e(MESSAGE_TAG, "error: $message")
}


