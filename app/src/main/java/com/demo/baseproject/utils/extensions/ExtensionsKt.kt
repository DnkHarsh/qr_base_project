package com.demo.baseproject.utils.extensions

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.text.Spanned
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import java.io.Serializable

fun dpToPx(dp: Int): Int {
    return dp.toPx.toInt()
}

fun TextView.startDrawable(@DrawableRes id: Int?, sizeInDp: Int = 16) {
    if (id == null) {
        this.setCompoundDrawables(null, null, null, null)
    } else {
        val drawable = ContextCompat.getDrawable(context, id)
        drawable?.setBounds(0, 0, dpToPx(sizeInDp), dpToPx(sizeInDp))
        this.setCompoundDrawables(drawable, null, null, null)
    }
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

fun View.isGone(): Boolean {
    return this.visibility == View.GONE
}

fun Collection<View>.visible() {
    this.forEach {
        it.visible()
    }
}

fun View.addBorderDecor(fgId: Int?, bgId: Int?) {
    if (fgId != null) {
        this.foreground = ContextCompat.getDrawable(context, fgId)
    } else {
        this.foreground = null
    }
}

fun View.gone() {
    this.visibility = View.GONE
}

fun List<View>.gone() {
    this.forEach {
        it.gone()
    }
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun Any?.isNotNull(): Boolean {
    return this != null
}

fun Any?.isNull(): Boolean {
    return this == null
}

fun <T> List<T>.getItemInList(position: Int): T? {
    if (position < 0 || position >= this.size) {
        return null
    }
    return this[position]
}

fun <T> List<T>.indexOfFirstAndLast(predicate: (T) -> Boolean): Pair<Int, Int> {
    var firstIndex = -1
    var lastIndex = -1
    this.forEachIndexed { index, item ->
        if (predicate(item)) {
            if (firstIndex == -1) {
                firstIndex = index
            }
            lastIndex = index
        }
    }
    return Pair(firstIndex, lastIndex)
}

fun TextView.setTextAndVisibility(text: String, predicate: () -> Boolean) {
    if (predicate()) {
        setText(text)
        visible()
    } else {
        gone()
    }
}

fun TextView.setTextAndVisibility(text: Spanned, predicate: () -> Boolean) {
    if (predicate()) {
        setText(text)
        visible()
    } else {
        gone()
    }
}

fun Int?.defaultOrValue(): Int {
    return this ?: 0
}

fun Boolean?.defaultOrValue(): Boolean {
    return this ?: false
}

val Number.toPx
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

fun String.getFileNameFromUrl(): String {
    return this.substring(this.lastIndexOf('/') + 1).split('.')[0]
}

fun Iterable<String>.preCacheImages(context: Context) {
    this.forEach {
        Glide.with(context)
            .load(it)
            .preload()
    }
}

fun <T : Serializable> Bundle.getSerializableData(key: String, value: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, value)
    } else {
        getSerializable(key) as? T
    }
}

fun <T> Bundle.getParcelableData(key: String, value: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, value)
    } else {
        getParcelable(key) as? T

    }
}