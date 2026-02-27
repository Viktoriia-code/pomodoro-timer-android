package com.example.pomodorotimer.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val timeFormatter =
    DateTimeFormatter.ofPattern("HH:mm")

fun Long.toTimeString(): String {

    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
        .format(timeFormatter)
}

private val fullDateFormatter =
    DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)

fun Long.toFullDateString(): String {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(fullDateFormatter)
}