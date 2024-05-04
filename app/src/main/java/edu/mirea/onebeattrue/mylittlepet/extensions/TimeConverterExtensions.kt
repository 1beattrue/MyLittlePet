package edu.mirea.onebeattrue.mylittlepet.extensions

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun Long.convertMillisToLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}