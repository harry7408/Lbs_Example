package com.sesac.lbshomework

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationServices
import com.sesac.lbshomework.model.LatLang

class LocationService : Service() {

    private lateinit var subThread: BackgroundSubThread
    private var handler: Handler = Handler(Looper.getMainLooper())

    private var latLang: LatLang = LatLang(0.0, 0.0)
    private val locationLiveData = MutableLiveData<LatLang>()

    val locationData: LiveData<LatLang>
        get() = locationLiveData

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, getString(R.string.service_start_text), Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (!::subThread.isInitialized) {
            subThread = BackgroundSubThread("Location Thread")
            subThread.start()
        }

        if (!subThread.isAlive) {
            subThread.start()
        }

        LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener {
            latLang = LatLang(it.longitude, it.latitude)
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (subThread.isAlive) {
            subThread.interrupt()
        }
        stopSelf()
    }

    inner class BackgroundSubThread(name: String) : Thread(name) {
        override fun run() {
            while (!isInterrupted) {
                try {
                    handler.post {
                        locationLiveData.postValue(latLang)
                        Toast.makeText(applicationContext, latLang.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                    sleep(5000L)
                } catch (e: InterruptedException) {
                    currentThread().interrupt()
                }
            }
        }
    }
}