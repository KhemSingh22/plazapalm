package com.example.plazapalm.imageutils

import android.app.DatePickerDialog
import android.content.Context
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

//TODO

object DateTimeUtil {
    const val UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val DD_MM_YYY_FORMAT = "dd-MM-yyyy"
    const val UTC = "UTC"

    fun changeTimeFormat(input: String, format: String): String {
        if (input.isBlank() || format.isBlank()) return ""
        return try {
            val date = SimpleDateFormat(UTC_FORMAT, Locale.getDefault()).parse(input)
            val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
            simpleDateFormat.timeZone = TimeZone.getTimeZone(UTC)
            simpleDateFormat.format(date!!)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getTimeStampFromMillis(milliSec: Long, format: String = UTC_FORMAT): String {
        return try {
            val simple = SimpleDateFormat(format, Locale.getDefault())
            simple.timeZone = TimeZone.getTimeZone(UTC)
            val result = Date(milliSec)
            simple.format(result)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getTimeAgo(date: String?): String {
        if (date.isNullOrEmpty())
            return ""
        val sdf = SimpleDateFormat(UTC_FORMAT, Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone(UTC)
        return try {
            val time = sdf.parse(date)?.time
            val now = System.currentTimeMillis()
            return when (val ago =
                DateUtils.getRelativeTimeSpanString(time!!, now, DateUtils.MINUTE_IN_MILLIS)) {
                "0 minutes ago" ->
                {
                    "few seconds ago"
                }
                "In 0 minutes" ->
                {
                    "few minutes ago"
                }
                else ->
                {
                    ago.toString()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
    fun parseDate(utcStringDate: String = UTC_FORMAT, date: String): Date? {
        return if (date.isNotEmpty()) {
            val inputFormat = SimpleDateFormat(utcStringDate, Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone(UTC)
            return inputFormat.parse(date)
        } else Date()
    }
    /**datePickerDialog*/
    fun Context.datePickerDialog(
        isMaxCalendar: Boolean = false,
        isMinCalendar: Boolean = false,
        returnDate: (Calendar) -> Unit
    ) {
        val calender: Calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calender.set(Calendar.YEAR, year)
                calender.set(Calendar.MONTH, month)
                calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                returnDate(calender)
            }, calender
                .get(Calendar.YEAR), calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        )
        if (isMaxCalendar) datePicker.datePicker.maxDate = System.currentTimeMillis()
        if (isMinCalendar) datePicker.datePicker.minDate = System.currentTimeMillis()
        datePicker.show()
    }
}