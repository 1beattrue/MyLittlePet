package edu.mirea.onebeattrue.mylittlepet.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import edu.mirea.onebeattrue.mylittlepet.R
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId

fun Long.convertMillisToLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

fun Long.convertMillisToLocalDateTime(): LocalDateTime {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}

fun Long.convertMillisToYearsAndMonths(): Pair<Int, Int> {
    val birthDate = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    val currentDate = LocalDate.now()

    val years = birthDate.until(currentDate).years
    val months = birthDate.until(currentDate).months

    return Pair(years, months)
}

fun Long.convertMillisToDayMonthYear(): Triple<Int, Month, Int> {
    val birthDate = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    val currentDate = LocalDate.now()

    val day = birthDate.dayOfMonth
    val year = birthDate.year
    val month = birthDate.month

    return Triple(day, month, year)
}

@Composable
fun Month.getName() =
    when (this) {
        Month.JANUARY -> stringResource(R.string.january)
        Month.FEBRUARY -> stringResource(R.string.february)
        Month.MARCH -> stringResource(R.string.march)
        Month.APRIL -> stringResource(R.string.april)
        Month.MAY -> stringResource(R.string.may)
        Month.JUNE -> stringResource(R.string.june)
        Month.JULY -> stringResource(R.string.july)
        Month.AUGUST -> stringResource(R.string.august)
        Month.SEPTEMBER -> stringResource(R.string.september)
        Month.OCTOBER -> stringResource(R.string.october)
        Month.NOVEMBER -> stringResource(R.string.november)
        Month.DECEMBER -> stringResource(R.string.december)
    }
