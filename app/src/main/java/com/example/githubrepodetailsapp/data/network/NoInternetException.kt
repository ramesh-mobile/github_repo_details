package com.example.githubrepodetailsapp.data.network

import java.io.IOException

/**
 * Created by ramesh on 21-07-2021
 */
class NoInternetException(var errorMessage: String) : IOException(
    errorMessage
) {
    override val message: String
        get() = "No network available, please check your WiFi or Data connection"
}