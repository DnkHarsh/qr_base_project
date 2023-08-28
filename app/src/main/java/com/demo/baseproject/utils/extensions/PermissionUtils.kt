package com.demo.baseproject.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * Method to check requested permissions are granted or not.
 * @param permissions requested permissions array
 * @return returns true if all requested permissions are granted else false.
 */
fun Context.hasPermissions(permissions: Array<String>): Boolean {
    for (permission in permissions) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
    }
    return true
}

/**
 * Method to check requested single permission are granted or not.
 * @param permission requested permission
 * @return returns true if requested permission is granted else false.
 */
@Suppress("unused")
fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

/**
 * Gets whether you should show UI with rationale for requesting permissions.
 *
 * @param permissions The permissions your app wants to request.
 * @return Whether you can show permission rationale UI.
 */
@Suppress("unused")
fun Fragment.shouldShowRequestPermissions(permissions: Array<String>): Boolean {
    for (permission in permissions) {
        if (!this.shouldShowRequestPermissionRationale(permission)) {
            return false
        }
    }
    return true
}

/**
 * Gets whether you should show UI with rationale for requesting permissions.
 * @param permissions The permissions your app wants to request.
 * @return Whether you can show permission rationale UI.
 */
@Suppress("unused")
fun Activity.shouldShowRequestPermissions(permissions: Array<String>): Boolean {
    for (permission in permissions) {
        if (!this.shouldShowRequestPermissionRationale(permission)) {
            return false
        }
    }
    return true
}


/**
 * This is public static method  to check should show permission dialog or not by calling
 * shouldShowRequestPermissionRationale of  ActivityCompat java class.
 * @param permission requested permission in string
 * @return true if should show permission else false if user has selected "Never ask" option earlier.
 */
@Suppress("unused")
fun Activity.shouldShowRequestPermission(permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
}

/**
 * This is public static method  to check should show permission dialog or not by calling
 * shouldShowRequestPermissionRationale of  ActivityCompat java class.
 * @param permission requested permission in string
 * @return true if should show permission else false if user has selected "Never ask" option earlier.
 */
@Suppress("unused")
fun Fragment.shouldShowRequestPermission(permission: String): Boolean {
    return this.shouldShowRequestPermissionRationale(permission)
}

/**
 * Method ro request permission with list of permissions.
 *
 * @param permissions The permissions your app wants to request.
 * @param reqId reqId which will return in call back of permission in  onRequestPermissionsResult method in activity
 */
fun Activity.requestPermission(permissions: Array<String>, reqId: Int) {
    ActivityCompat.requestPermissions(this, permissions, reqId)
}

/**
 * Method to get required permission string from permission string array
 * @param permissions String array of required permission
 * @return returns empty if not required permission else required permission string.
 */
private fun Context.getRequiredPermissionString(permissions: Array<String>): String {
    for (permission in permissions) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return permission
        }
    }
    return ""
}


/**
 * Method to check user checked "Never asked" option or not.
 * @param permissions String array of required permission
 * @return returns true if neverAsked selected else returns false.
 */
fun Activity.hasPermissionDenied(permissions: Array<String>): Boolean {
    val permission = getRequiredPermissionString(permissions)
    return !TextUtils.isEmpty(permission) && shouldShowRequestPermissionRationale(permission)
}
