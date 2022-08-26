package com.example.githubrepodetailsapp.data.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast


/**
 * Created by ramesh on 21-07-2021
 */
class StartOnBootReceiver : BroadcastReceiver() {
    private val TAG = "StartOnBootReceiver"
    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {

            Log.d(TAG, "onReceive: boot completed!")
            Toast.makeText(context, "Boot Completed", Toast.LENGTH_LONG).show();

            //for Background Service
            /*val serviceIntent = Intent(context, FetchRepoService::class.java)
            serviceIntent.putExtra("inputExtra", "passing any text")
            ContextCompat.startForegroundService(context!!, serviceIntent)*/

        }
    }
}