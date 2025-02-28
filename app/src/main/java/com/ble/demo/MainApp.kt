package com.ble.demo

import android.app.Application
import android.content.Context
import com.ble.mylibrary.LansManager
import com.ble.mylibrary.outdevice.LScaleHelper

class MainApp : Application() {
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = baseContext
        LansManager.getInstance().setBleAutoConnect(true).setBleDisAutoConnect(true).init(this);

    }


}
































