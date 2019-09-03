package com.gemicle.locationkeeper.ui.main

import com.gemicle.locationkeeper.domain.GpsInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class LocationPresenter(private val gpsInteractor: GpsInteractor) {

    private var compositeDisposable = CompositeDisposable()

    fun tryUploadDataFromDb() {
        compositeDisposable.add(gpsInteractor
            .sendDataFromDb()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = {
                    it.printStackTrace()
                }
            )

        )
    }

}