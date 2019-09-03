package com.gemicle.locationkeeper.data.location

import com.gemicle.locationkeeper.data.base.ApiFactory
import com.gemicle.locationkeeper.data.location.models.LocationEntity
import io.reactivex.Completable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface LocationApi {

    @POST("/clients/{client_id}/locations")
    fun sendLocation(@Path("client_id") clientId: String = ApiFactory.CLIENT_ID, @Body location: LocationEntity): Completable

}