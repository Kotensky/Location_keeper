package com.gemicle.locationkeeper.data.location.models

import com.gemicle.locationkeeper.utils.Utils

data class LocationEntity(
    val lat: Double,
    val lng: Double,
    val timestamp: String
) {
    companion object {

        fun map(locationDto: LocationDto): LocationEntity {
            return LocationEntity(
                locationDto.lat,
                locationDto.lng,
                Utils.formatTime(locationDto.time)
            )
        }
    }
}