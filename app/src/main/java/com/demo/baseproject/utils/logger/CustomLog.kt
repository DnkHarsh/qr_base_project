package com.demo.baseproject.utils.logger

import android.util.Log
import com.demo.baseproject.BuildConfig

import java.io.PrintWriter
import java.io.StringWriter

/**
 * Method to print verbose information
 * @param tag key
 * @param message value to be printed
 */
fun verboseLog(tag: String, message: Any) {
    if (BuildConfig.DEBUG)
        Log.v(tag, message.toString())
}

/**
 * Method to print warning information
 * @param tag key
 * @param message value to be printed
 */
fun warningLog(tag: String, message: String) {
    if (BuildConfig.DEBUG)
        Log.w(tag, message)
}

/**
 * Method to print debug information
 * @param tag key
 * @param message value to be printed
 */
fun debugLog(tag: String, message: Any) {
    if (BuildConfig.DEBUG)
        Log.d(tag, message.toString())
}

/**
 * Method to print info information
 * @param tag key
 * @param message value to be printed
 */
fun infoLog(tag: String, message: String) {
    if (BuildConfig.DEBUG)
        Log.i(tag, message)
}

/**
 * Method to print error information
 * @param tag key
 * @param message value to be printed
 */
fun errorLog(tag: String, message: String) {
    if (BuildConfig.DEBUG)
        Log.e(tag, message)
}


/**
 * Method to print error  information with throwable
 * @param tag key
 * @param message value to be printed
 * @param throwable exception throwable
 */
fun errorLog(tag: String, message: String, throwable: Throwable) {
    if (BuildConfig.DEBUG)
        Log.e(tag, message, throwable)
}

/**
 * Method to print error information
 * @param tag key
 * @param exception value to be printed
 */
fun errorExceptionLog(tag: String, exception: Exception) {
    if (BuildConfig.DEBUG) {
        val sw = StringWriter()
        exception.printStackTrace(PrintWriter(sw))
        Log.e(tag, sw.toString())
    }
}

/**
 * Method to print error information
 * @param tag key
 * @param throwable value to be printed
 */
fun errorThrowableLog(tag: String, throwable: Throwable) {
    if (BuildConfig.DEBUG) {
        val sw = StringWriter()
        throwable.printStackTrace(PrintWriter(sw))
        Log.e(tag, sw.toString())
    }
}
