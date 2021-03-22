package appdev.max.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.*

private val FORECAST_DATE_FORMAT = "dd.MM"

fun forecastDateFormatted(value: Long): String {
    return Date(value * 1000).let { date ->
        SimpleDateFormat(FORECAST_DATE_FORMAT, Locale.UK).format(date)
    }
}