package com.demo.baseproject.utils.extensions

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import com.demo.baseproject.R
import com.demo.baseproject.databinding.DialogBuyAdfreeBinding
import com.demo.baseproject.databinding.DialogForRateUsBinding
import com.demo.baseproject.databinding.DialogForceAppUpdateBinding
import com.demo.baseproject.databinding.DialogInternetConnectionBinding

/**
 * Method to show alert dialog with two/single button
 *
 * @param message               Message of dialog
 * @param posButtonName         Name of positive button
 * @param nagButtonName         Name of negative button
 * @param onPositiveButtonClick call back method of positive button
 * @param onNegativeButtonClick call back method of negative button
 */
fun Context.showCustomTwoButtonAlertDialog(
    title: String, message: String, posButtonName: String,
    nagButtonName: String, isOutSideCancelable: Boolean,
    onPositiveButtonClick: DialogInterface.OnClickListener,
    onNegativeButtonClick: DialogInterface.OnClickListener
) {
    showCustomTwoButtonAlertDialog(
        title,
        message,
        posButtonName,
        nagButtonName,
        false,
        isOutSideCancelable,
        onPositiveButtonClick,
        onNegativeButtonClick
    )

}

/**
 * Method to show alert dialog with single  positive  button
 *
 * @param message               Message of dialog
 * @param posButtonName         Name of positive button
 * @param onPositiveButtonClick call back method of positive button
 */
@Suppress("unused")
fun Context.showCustomTwoButtonAlertDialog(
    title: String, message: String, posButtonName: String,
    isOutSideCancelable: Boolean, onPositiveButtonClick: DialogInterface.OnClickListener
) {
    showCustomTwoButtonAlertDialog(
        title,
        message,
        posButtonName,
        "",
        false,
        isOutSideCancelable,
        onPositiveButtonClick,
        null
    )

}

/**
 * Method to show alert dialog with two/single button
 *
 * @param message               Message of dialog
 * @param posButtonName         Name of positive button
 * @param nagButtonName         Name of negative button
 * @param onPositiveButtonClick call back method of positive button
 * @param onNegativeButtonClick call back method of negative button
 */
private fun Context.showCustomTwoButtonAlertDialog(
    title: String, message: String, posButtonName: String,
    nagButtonName: String, changeButtonColor: Boolean, isOutSideCancelable: Boolean,
    onPositiveButtonClick: DialogInterface.OnClickListener?,
    onNegativeButtonClick: DialogInterface.OnClickListener?
) {
    try {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert)
        builder.setMessage(message)
        if (TextUtils.isEmpty(title)) {
            builder.setTitle(resources.getString(R.string.app_name))
        } else {
            builder.setTitle(title)
        }

        if (onPositiveButtonClick != null) {
            builder.setPositiveButton(posButtonName, onPositiveButtonClick)
        }
        if (onNegativeButtonClick != null) {
            builder.setNegativeButton(nagButtonName, onNegativeButtonClick)
        }

        builder.setCancelable(true)
        val alert = builder.create()
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (changeButtonColor) {
            try {
                alert.setOnShowListener {
                    alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        if (isOutSideCancelable) {
            alert.setCanceledOnTouchOutside(true)
        } else {
            alert.setCanceledOnTouchOutside(false)
        }
        alert.show()
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

/**
 * Method to show alertDialog with items array
 *
 * @param items               Array of data which are display in dialog
 * @param title               title of the alert dialog
 * @param onItemClickListener call back method of selected item
 */
@Suppress("unused")
fun Context.showCustomAlertDialogWithItems(
    items: Array<CharSequence>,
    title: String,
    onItemClickListener: DialogInterface.OnClickListener
) {
    try {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert)
        builder.setTitle(title)
        builder.setItems(items, onItemClickListener)
        val alert = builder.create()
        alert.show()
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

/**
 * This method show dialog when there is no connection
 */
fun Context.showDialogNoConnection() {
    val dialog = Dialog(this)
    val binding = DialogInternetConnectionBinding.inflate(LayoutInflater.from(this))
    dialog.setContentView(binding.root)
    dialog.window?.let {
        val windowManagerLayoutParams = WindowManager.LayoutParams()
        windowManagerLayoutParams.copyFrom(it.attributes)
        it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //This makes the dialog take up the full width
        windowManagerLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        windowManagerLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        windowManagerLayoutParams.width = SCREEN_WIDTH - SCREEN_WIDTH / 10
        it.attributes = windowManagerLayoutParams
    }
    binding.ivClose.setOnClickListener { dialog.dismiss() }
    dialog.show()
}

fun Context.showDialogBuyAdFree(onClickListener: View.OnClickListener) {
    val dialog = Dialog(this)
    val binding = DialogBuyAdfreeBinding.inflate(LayoutInflater.from(this))

    dialog.setContentView(binding.root)
    dialog.window?.let {
        val windowManagerLayoutParams = WindowManager.LayoutParams()
        windowManagerLayoutParams.copyFrom(it.attributes)
        it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //This makes the dialog take up the full width
        windowManagerLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        windowManagerLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        windowManagerLayoutParams.width = SCREEN_WIDTH - SCREEN_WIDTH / 5
        it.attributes = windowManagerLayoutParams
    }

    dialog.window?.attributes?.windowAnimations = R.anim.zoom_in


    binding.tvBuy.setOnClickListener { view ->
        onClickListener.onClick(view)
        dialog.dismiss()
    }
//    binding.tvCancel.setOnClickListener { dialog.dismiss() }
    dialog.show()
}

fun Context.showRateAppDialog(rateNowClickListener: View.OnClickListener) {
    val dialogRateApp = Dialog(this)
    val binding = DialogForRateUsBinding.inflate(LayoutInflater.from(this))
    dialogRateApp.setContentView(binding.root)
    dialogRateApp.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//    val animation = AnimationUtils.loadAnimation(this, R.anim.enter_from_left)
//    binding.ivRatePanel.clearAnimation()
//    binding.ivRatePanel.startAnimation(animation)
    dialogRateApp.window?.attributes?.windowAnimations = R.anim.zoom_in

    binding.ivRatePanel.setOnClickListener { view ->
        dialogRateApp.dismiss()
        rateNowClickListener.onClick(view)
    }
//    binding.tvAppName.clearAnimation()
    binding.tvNo.setOnClickListener { dialogRateApp.dismiss() }
    binding.tvRateNow.setOnClickListener { v: View? ->
        dialogRateApp.dismiss()
        rateNowClickListener.onClick(v)
    }
    dialogRateApp.show()
}


/**
 * This method shows permission dialog with permission detail and purpose of particular permission.
 * @param requestMessage requestMessage
 * @param purposeMessage purposeMessage
 * @param allowListener  call back method for allow button clicked
 * @param skipListener call skip method for allow button clicked
 */
fun Activity.showDialogWhenDeniedPermission(
    requestMessage: String,
    purposeMessage: String,
    allowListener: View.OnClickListener,
    skipListener: View.OnClickListener
) {
    val dialog = Dialog(this)
    dialog.setContentView(R.layout.dialog_external_permisions)


    val windowManagerLayoutParams = WindowManager.LayoutParams()
    val window = dialog.window

    window?.let {
        windowManagerLayoutParams.copyFrom(it.attributes)
        it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //This makes the dialog take up the full width
        windowManagerLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        windowManagerLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        windowManagerLayoutParams.width = SCREEN_WIDTH - SCREEN_WIDTH / 10
        it.attributes = windowManagerLayoutParams

    }

    val cvMain: RelativeLayout? = dialog.findViewById(R.id.cvMain)
    val tvAllow = dialog.findViewById<AppCompatTextView>(R.id.tvAllow)
    val tvSkip = dialog.findViewById<AppCompatTextView>(R.id.tvSkip)
    val tvPermissionMsg =
        dialog.findViewById<AppCompatTextView>(R.id.tvPermissionTitle)
    val tvPurposePermissionMsg =
        dialog.findViewById<AppCompatTextView>(R.id.tvPermissionMessage)

    tvPermissionMsg?.text = requestMessage
    if (purposeMessage.isBlank()) {
        tvPurposePermissionMsg?.visibility = View.GONE
    } else {
        tvPurposePermissionMsg?.text = purposeMessage
    }

    val expandIn = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom_coustom)
    cvMain?.startAnimation(expandIn)
    dialog.setCancelable(false)

    tvAllow?.setOnClickListener { v ->
        dialog.dismiss()
        allowListener.onClick(v)
    }
    tvSkip?.setOnClickListener { v ->
        val expandout = AnimationUtils.loadAnimation(this, R.anim.fade_out_transit)
        cvMain?.startAnimation(expandout)
        Handler(Looper.getMainLooper()).postDelayed({ dialog.dismiss() }, 206)
        skipListener.onClick(v)
    }
    dialog.show()
}

/**
 * This method show dialog when mandatory update
 */
fun Activity.showMandatoryUpdateDialog() {
    val dialog = Dialog(this)
    dialog.setCancelable(false)
    dialog.setCanceledOnTouchOutside(false)
    val binding = DialogForceAppUpdateBinding.inflate(LayoutInflater.from(this))
    dialog.setContentView(binding.root)
    dialog.window?.let {
        val windowManagerLayoutParams = WindowManager.LayoutParams()
        windowManagerLayoutParams.copyFrom(it.attributes)
        it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //This makes the dialog take up the full width
        windowManagerLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        windowManagerLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        windowManagerLayoutParams.width = SCREEN_WIDTH - SCREEN_WIDTH / 10
        it.attributes = windowManagerLayoutParams
    }
    binding.cancel.setOnClickListener {
        dialog.dismiss()
        finish()
    }
    binding.update.setOnClickListener {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
    dialog.show()
}