package com.example.pomodorotimer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SessionEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun sessionDao(): SessionDao
}