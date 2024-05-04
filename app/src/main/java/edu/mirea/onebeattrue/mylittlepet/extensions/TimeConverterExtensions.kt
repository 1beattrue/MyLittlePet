package edu.mirea.onebeattrue.mylittlepet.extensions

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun Long.convertMillisToLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

fun Long.convertMillisToYearsAndMonths(): Pair<Int, Int> {
    val birthDate = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    val currentDate = LocalDate.now()

    var years = birthDate.until(currentDate).years.toInt()
    var months = birthDate.until(currentDate).months.toInt()

    return Pair(years, months)
}
