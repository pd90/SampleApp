package com.sample.application

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.sample.BuildConfig
import com.squareup.leakcanary.LeakCanary.install
import com.squareup.leakcanary.LeakCanary.isInAnalyzerProcess
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : MultiDexApplication() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate() {
        super.onCreate()

        //init leak canary
        if(!BuildConfig.DEBUG) {
            if (isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return
            }
            install(this)
        }
        if (BuildConfig.DEBUG) {

        }

    }
}
