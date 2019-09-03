package com.gemicle.locationkeeper.service

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.util.Log
import com.gemicle.locationkeeper.LocationKeeperApplication
import com.gemicle.locationkeeper.domain.GpsInteractor
import com.gemicle.locationkeeper.utils.LocationHelper
import com.gemicle.locationkeeper.utils.NotificationHelper
import com.gemicle.locationkeeper.utils.Utils.getLocationText
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class LocationUpdatesService : Service() {

    private lateinit var notificationHelper: NotificationHelper
    private lateinit var gpsInteractor: GpsInteractor
    private lateinit var locationHelper: LocationHelper

    override fun onCreate() {
        setup()
        locationHelper.getLastLocation()
        startForeground(
            NotificationHelper.NOTIFICATION_ID, notificationHelper.buildNotification(
            getLocationText(locationHelper.lastLocation)
        ))
    }

    private fun setup() {
        notificationHelper = NotificationHelper(this)
        locationHelper = LocationHelper(
            this,
            onNewLocation = {
                notificationHelper.notifyLocation(getLocationText(locationHelper.lastLocation))
                sendLocation(it)
            })

        gpsInteractor = (application as LocationKeeperApplication).gpsinteractor
        sendDataFromDb()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        locationHelper.startLocationUpdates()
        return START_STICKY
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        locationHelper.stopLocationUpdates()
        super.onDestroy()
    }

    private fun sendLocation(location: Location) {
        gpsInteractor
            .sendLocation(location.latitude, location.longitude)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onComplete = {
                    sendDataFromDb()
                },
                onError = {
                    Log.e(TAG, it.message)
                    storeLocation(location)
                }
            )
    }

    private fun storeLocation(location: Location) {
        gpsInteractor
            .storeDataToDb(location.latitude, location.longitude)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = {
                    Log.e(TAG, it.message)
                }
            )
    }

    private fun sendDataFromDb() {
        gpsInteractor
            .sendDataFromDb()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = {
                    Log.e(TAG, it.message)
                }
            )
    }

    companion object {

        const val TAG = "LOCATION_SERVICE"

    }

}
