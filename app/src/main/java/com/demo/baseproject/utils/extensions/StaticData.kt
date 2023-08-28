package com.demo.baseproject.utils.extensions

import com.demo.baseproject.R
import com.demo.baseproject.application.BaseApplication
import java.io.File

var SCREEN_HEIGHT = 0
var SCREEN_WIDTH = 0

internal var AD_FILE_NAME = "adDataFile"
var LAUNCHED_FROM_NOTIFICATION = "launchedFromNotification"

// TODO : Change this directory as per your requirement in app
var mainDirectory =
    BaseApplication.instance.getExternalFilesDirs(null)[0].absolutePath + File.separator + BaseApplication.instance.getString(
        R.string.app_name
    )
var imageDirectory = mainDirectory + File.separator + ""
var tempDirectory = mainDirectory + File.separator + ".temp"

//TODO :change notification time and range is 0 - 12
const val notificationTime = 9