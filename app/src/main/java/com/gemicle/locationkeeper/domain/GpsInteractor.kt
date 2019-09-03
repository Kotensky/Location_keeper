package com.gemicle.locationkeeper.domain

import io.reactivex.Completable

interface GpsInteractor {

    fun sendLocation(lat: Double, lng: Double): Completable

    fun storeDataToDb(lat: Double, lng: Double): Completable

    fun sendDataFromDb(): Completable

}