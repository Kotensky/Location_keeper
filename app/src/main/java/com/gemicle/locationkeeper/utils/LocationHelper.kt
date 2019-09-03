package com.gemicle.locationkeeper.utils


import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*

class LocationHelper(
    context: Context,
    var onNewLocation: (location: Location) -> Unit
) {

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private var locationCallback: LocationCallback
    private var locationRequest: LocationRequest
    var lastLocation: Location? = null

    init {

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                locationResult?.lastLocation?.let {
                    Log.i(
                        TAG,
                        Utils.getLocationText(it)
                    )
                    onNewLocation(it)
                    lastLocation = it
                }
            }
        }

        locationRequest = LocationRequest()
        locationRequest.interval =
            UPDATE_INTERVAL
        locationRequest.fastestInterval =
            FASTEST_UPDATE_INTERVAL
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }


    fun startLocationUpdates() {
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback, Looper.myLooper()
            )

        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permission. Could not request updates. $unlikely")
        }
    }

    fun stopLocationUpdates() {
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permission. Could not remove updates. $unlikely")
        }
    }

    fun getLastLocation() {
        try {
            fusedLocationClient.lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        lastLocation = task.result
                    } else {
                        Log.w(TAG, "Failed to get location.")
                    }
                }
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permission.$unlikely")
        }
    }


    companion object {

        private const val TAG = "LocationHelper"
        private const val UPDATE_INTERVAL: Long = 10000
        private const val FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2


    }
}