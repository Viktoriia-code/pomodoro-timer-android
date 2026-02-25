package com.example.pomodorotimer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val durationMinutes: Int,

    val completedAt: Long   // timestamp
)