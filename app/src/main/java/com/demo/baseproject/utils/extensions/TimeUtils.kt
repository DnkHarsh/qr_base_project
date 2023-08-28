package com.demo.baseproject.utils.extensions

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Method to get time in timestamp
 *
 * @param updateDate date in string format
 * @return date format as a string
 */
@Suppress("unused")
fun getDateInLong(updateDate: String): Long {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val date: Date
    return try {
        date = sdf.parse(updateDate) as Date
        date.time
    } catch (ex: Exception) {
        0
    }

}


/**
 * Method to get date from timestamp.
 * @param time time in timestamp (long)
 * @return returns date in string in dd MMMM yyyy format
 */
@Suppress("unused")
fun getDateInDayFormat(time: Long?): String {
    var date = ""
    try {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        date = time?.let { Date(it) }?.let { sdf.format(it) }.toString()
        return date
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return date
}

/**
 * Method to get date in dd/MM/yyyy HH:mm format
 *
 * @param time timestamp in long datatype
 * @return date format as a string
 */
@Suppress("unused")
fun getDateIn24HrsFormatInUTC(time: Long): String {
    var date = ""
    try {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        date = sdf.format(Date(time))
        return date
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return date
}

/**
 * Method to get time from current time.
 * This method will check Months, Weeks, days, Weeks, Days,Hours, Minutes, Sec different
 * @param timeStamp end date
 * @return time different in string
 */
@Suppress("unused")
fun getDateTimeDifference(timeStamp: Long): String {


    val currentDate = Date()
    var different = currentDate.time - timeStamp

    val secondsInMilli: Long = 1000
    val minutesInMilli = secondsInMilli * 60
    val hoursInMilli = minutesInMilli * 60
    val daysInMilli = hoursInMilli * 24
    val weeksInMilli = daysInMilli * 7
    val monthsInMilli = weeksInMilli * 4

    val elapsedMonths = different / monthsInMilli
    different %= monthsInMilli

    val elapsedWeeks = different / weeksInMilli
    different %= weeksInMilli

    val elapsedDays = different / daysInMilli
    different %= daysInMilli

    val elapsedHours = different / hoursInMilli
    different %= hoursInMilli

    val elapsedMinutes = different / minutesInMilli
    different %= minutesInMilli

    val elapsedSeconds = different / secondsInMilli

    var time: String? = null
    if (elapsedMonths > 0) {
        time = formatTime(elapsedMonths, elapsedWeeks, " month", " week")
    } else {
        if (elapsedWeeks > 0) {
            time = formatTime(elapsedWeeks, elapsedDays, " week", " day")
        } else {
            if (elapsedDays > 0) {
                time = formatTime(elapsedDays, elapsedHours, " day", " hour")
            } else {
                if (elapsedHours > 0) {
                    time = formatTime(elapsedHours, elapsedMinutes, " hour", " min")
                } else {
                    if (elapsedMinutes > 0) {
                        time = if (elapsedMinutes == 1L) {
                            "$elapsedMinutes min ago"
                        } else {
                            "$elapsedMinutes mins ago"
                        }
                    } else {
                        if (elapsedSeconds > 0) {
                            time = "$elapsedSeconds secs ago"
                        }
                    }
                }
            }
        }

    }

    time?.let {
        return time
    } ?: run {
        return "0"
    }
}

/**
 * Common method for checking single or multiple values in string and concat string
 * @param checkVar Main params
 * @param CompareVar sub params
 * @param mainString Main string
 * @param subString Sub String
 * @return String with whole text which will be displayed.
 */

private fun formatTime(
    checkVar: Long,
    CompareVar: Long,
    mainString: String,
    subString: String
): String {

    val time: String = if (checkVar == 1L) {
        if (CompareVar > 0) {
            "$checkVar$mainString$CompareVar$subString ago"
        } else {
            checkVar.toString() + subString + "s " + " ago"
        }
    } else {
        if (CompareVar > 0) {
            checkVar.toString() + mainString + "s " + CompareVar + subString + "s " + " ago"
        } else {
            checkVar.toString() + subString + "s " + " ago"
        }
    }
    return time
}


fun getDelayFromNowTime(): Long {
    val calendar = Calendar.getInstance()
    val calendar1 = Calendar.getInstance()
    val calendar2 = Calendar.getInstance()
    calendar1[Calendar.HOUR_OF_DAY] = notificationTime
    calendar1[Calendar.MINUTE] = 0
    calendar1[Calendar.SECOND] = 0
    calendar2[Calendar.HOUR_OF_DAY] = notificationTime + 12
    calendar2[Calendar.MINUTE] = 0
    calendar2[Calendar.SECOND] = 0
    val currentTime = calendar.time
    val d1 = calendar1.time
    val d2 = calendar2.time

    //in milliseconds
    var diff = d1.time - currentTime.time
    if (diff <= 0) diff = d2.time - currentTime.time
    return TimeUnit.MILLISECONDS.toMinutes(diff)
}

