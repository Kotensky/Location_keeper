package com.gemicle.locationkeeper.data.base

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gemicle.locationkeeper.data.location.LocationDao
import com.gemicle.locationkeeper.data.location.models.LocationDto

@Database(entities = [LocationDto::class], version = 1, exportSchema = false)
@TypeConverters(TypesConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun provideLocationDao(): LocationDao

}