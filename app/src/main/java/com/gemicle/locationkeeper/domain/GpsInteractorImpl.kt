package com.gemicle.locationkeeper.domain

import android.util.Log
import com.gemicle.locationkeeper.data.location.LocationApi
import com.gemicle.locationkeeper.data.location.LocationDao
import com.gemicle.locationkeeper.data.location.models.LocationDto
import com.gemicle.locationkeeper.data.location.models.LocationEntity
import com.gemicle.locationkeeper.utils.Utils
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.*

class GpsInteractorImpl constructor(
    private val locationApi: LocationApi,
    private val locationDao: LocationDao
) : GpsInteractor {

    companion object {
        const val TAG = "GPS_INTERACTOR"
    }

    override fun sendLocation(lat: Double, lng: Double): Completable {
        return locationApi
            .sendLocation(location = LocationEntity(lat, lng, Utils.formatTime(Date())))
    }

    override fun storeDataToDb(lat: Double, lng: Double): Completable {
        return Completable.fromAction { locationDao.insert(LocationDto(lat, lng)) }
    }

    override fun sendDataFromDb(): Completable {
        return locationDao
            .getAll()
            .flatMapObservable { Observable.fromIterable(it) }
            .flatMapCompletable {
                Log.i(TAG, it.toString())
                locationApi
                    .sendLocation(location = LocationEntity.map(it))
                    .andThen(Completable.fromAction {
                        locationDao.delete(it)
                    })

            }
    }
}