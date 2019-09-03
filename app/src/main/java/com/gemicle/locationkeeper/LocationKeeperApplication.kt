package com.gemicle.locationkeeper

import android.app.Application
import androidx.room.Room
import com.gemicle.locationkeeper.data.base.AppDatabase
import com.gemicle.locationkeeper.data.location.LocationDao
import com.gemicle.locationkeeper.data.base.ApiFactory
import com.gemicle.locationkeeper.data.location.LocationApi
import com.gemicle.locationkeeper.domain.GpsInteractor
import com.gemicle.locationkeeper.domain.GpsInteractorImpl
import com.gemicle.locationkeeper.ui.main.LocationPresenter

class LocationKeeperApplication : Application() {

    private lateinit var locationApi: LocationApi
    private lateinit var locationDao: LocationDao
    lateinit var gpsinteractor: GpsInteractor
    lateinit var locationPresenter: LocationPresenter

    private lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        database = Room
            .databaseBuilder(this, AppDatabase::class.java, "app_database")
            .build()
        locationDao = database.provideLocationDao()
        locationApi = ApiFactory.apiRequestService
        gpsinteractor = GpsInteractorImpl(locationApi, locationDao)
        locationPresenter = LocationPresenter(gpsinteractor)


    }
}