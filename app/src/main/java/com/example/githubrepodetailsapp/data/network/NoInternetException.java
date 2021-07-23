package com.example.githubrepodetailsapp.data.network;

import java.io.IOException;

/**
 * Created by ramesh on 21-07-2021
 */
public class NoInternetException extends IOException{

    String errorMessage;
    public NoInternetException(String errMsg){
        super(errMsg);
        errorMessage=errMsg;
    }

    @Override
    public String getMessage() {
        return "No network available, please check your WiFi or Data connection";
    }

    public String getErrorMessage() {
        return errorMessage;
    }


}


