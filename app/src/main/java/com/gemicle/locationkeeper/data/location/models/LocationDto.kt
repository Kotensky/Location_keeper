package com.gemicle.locationkeeper.data.location.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tmpLocations")
data class LocationDto(
    val lat: Double,
    val lng: Double,
    @PrimaryKey
    val time: Date = Date()
)