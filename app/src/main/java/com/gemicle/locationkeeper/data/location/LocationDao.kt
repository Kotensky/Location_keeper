package com.gemicle.locationkeeper.data.location

import androidx.room.*
import com.gemicle.locationkeeper.data.location.models.LocationDto
import io.reactivex.Single

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(location: LocationDto)

    @Delete
    fun delete(location: LocationDto)

    @Query("SELECT * FROM tmpLocations")
    fun getAll(): Single<List<LocationDto>>

}