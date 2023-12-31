package com.google.android.ads.nativetemplates

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

/**
 * Base class for a template view. *
 */
class TemplateView : FrameLayout {
    private var templateType = 0
    private var styles: NativeTemplateStyle? = null
    private var nativeAd: NativeAd? = null
    private var nativeAdView: NativeAdView? = null
    private var primaryView: TextView? = null
    private var secondaryView: TextView? = null
    private var ratingBar: RatingBar? = null
    private var tertiaryView: TextView? = null
    private var iconView: ImageView? = null
    private var mediaView: MediaView? = null
    private var callToActionView: Button? = null
    private var background: ConstraintLayout? = null

    constructor(context: Context?) : super(context!!)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs)
    }

    fun setStyles(styles: NativeTemplateStyle?) {
        this.styles = styles
        applyStyles()
    }

    private fun applyStyles() {
        val mainBackground: Drawable? = styles?.mainBackgroundColor
        if (mainBackground != null) {
            background?.background = mainBackground
            if (primaryView != null) {
                primaryView?.background = mainBackground
            }
            if (secondaryView != null) {
                secondaryView?.background = mainBackground
            }
            if (tertiaryView != null) {
                tertiaryView?.background = mainBackground
            }
        }
        val primary = styles?.primaryTextTypeface
        if (primary != null && primaryView != null) {
            primaryView?.typeface = primary
        }
        val secondary = styles?.secondaryTextTypeface
        if (secondary != null && secondaryView != null) {
            secondaryView?.typeface = secondary
        }
        val tertiary = styles?.tertiaryTextTypeface
        if (tertiary != null && tertiaryView != null) {
            tertiaryView?.typeface = tertiary
        }
        val ctaTypeface = styles?.callToActionTextTypeface
        if (ctaTypeface != null && callToActionView != null) {
            callToActionView?.typeface = ctaTypeface
        }
        val primaryTypefaceColor = styles?.primaryTextTypefaceColor ?: 0
        if (primaryTypefaceColor > 0 && primaryView != null) {
            primaryView?.setTextColor(primaryTypefaceColor)
        }
        val secondaryTypefaceColor = styles?.secondaryTextTypefaceColor ?: 0
        if (secondaryTypefaceColor > 0 && secondaryView != null) {
            secondaryView?.setTextColor(secondaryTypefaceColor)
        }
        val tertiaryTypefaceColor = styles?.tertiaryTextTypefaceColor ?: 0
        if (tertiaryTypefaceColor > 0 && tertiaryView != null) {
            tertiaryView?.setTextColor(tertiaryTypefaceColor)
        }
        val ctaTypefaceColor = styles?.callToActionTypefaceColor ?: 0
        if (ctaTypefaceColor > 0 && callToActionView != null) {
            callToActionView?.setTextColor(ctaTypefaceColor)
        }
        val ctaTextSize = styles?.callToActionTextSize ?: 0f
        if (ctaTextSize > 0 && callToActionView != null) {
            callToActionView?.textSize = ctaTextSize
        }
        val primaryTextSize = styles?.primaryTextSize ?: 0f
        if (primaryTextSize > 0 && primaryView != null) {
            primaryView?.textSize = primaryTextSize
        }
        val secondaryTextSize = styles?.secondaryTextSize ?: 0f
        if (secondaryTextSize > 0 && secondaryView != null) {
            secondaryView?.textSize = secondaryTextSize
        }
        val tertiaryTextSize = styles?.tertiaryTextSize ?: 0f
        if (tertiaryTextSize > 0 && tertiaryView != null) {
            tertiaryView?.textSize = tertiaryTextSize
        }
        val ctaBackground: Drawable? = styles?.callToActionBackgroundColor
        if (ctaBackground != null && callToActionView != null) {
            callToActionView?.background = ctaBackground
        }
        val primaryBackground: Drawable? = styles?.primaryTextBackgroundColor
        if (primaryBackground != null && primaryView != null) {
            primaryView?.background = primaryBackground
        }
        val secondaryBackground: Drawable? = styles?.secondaryTextBackgroundColor
        if (secondaryBackground != null && secondaryView != null) {
            secondaryView?.background = secondaryBackground
        }
        val tertiaryBackground: Drawable? = styles?.tertiaryTextBackgroundColor
        if (tertiaryBackground != null && tertiaryView != null) {
            tertiaryView?.background = tertiaryBackground
        }
        invalidate()
        requestLayout()
    }

    private fun adHasOnlyStore(nativeAd: NativeAd): Boolean {
        val store = nativeAd.store
        val advertiser = nativeAd.advertiser
        return !TextUtils.isEmpty(store) && TextUtils.isEmpty(advertiser)
    }

    fun setNativeAd(nativeAd: NativeAd) {
        this.nativeAd = nativeAd
        val store = nativeAd.store
        val advertiser = nativeAd.advertiser
        val headline = nativeAd.headline
        val body = nativeAd.body
        val cta = nativeAd.callToAction
        val starRating = nativeAd.starRating
        val icon = nativeAd.icon
        val secondaryText: String
        nativeAdView?.callToActionView = callToActionView
        nativeAdView?.headlineView = primaryView
        nativeAdView?.mediaView = mediaView
        nativeAdView?.mediaView?.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
        secondaryView?.visibility = VISIBLE
        if (adHasOnlyStore(nativeAd)) {
            nativeAdView?.storeView = secondaryView
            secondaryText = store!!
        } else if (!TextUtils.isEmpty(advertiser)) {
            nativeAdView?.advertiserView = secondaryView
            secondaryText = advertiser!!
        } else {
            secondaryText = ""
        }
        primaryView?.text = headline
        callToActionView?.text = cta
        //  Set the secondary view to be the star rating if available.
        if (starRating != null && starRating > 0) {
            secondaryView?.visibility = GONE
            ratingBar?.visibility = VISIBLE
            ratingBar?.rating = starRating.toFloat()
            nativeAdView?.starRatingView = ratingBar
        } else {
            secondaryView?.text = secondaryText
            secondaryView?.visibility = VISIBLE
            ratingBar?.visibility = GONE
        }
        if (icon != null) {
            iconView?.visibility = VISIBLE
            iconView?.setImageDrawable(icon.drawable)
        } else {
//            iconView?.visibility = GONE
        }
        if (tertiaryView != null) {
            tertiaryView?.text = body
            nativeAdView?.bodyView = tertiaryView
        }
        nativeAdView?.setNativeAd(nativeAd)
        nativeAdView?.setOnClickListener {
            nativeAdView?.callToActionView?.performClick()
        }
        iconView?.setOnClickListener {
            nativeAdView?.callToActionView?.performClick()
        }

        secondaryView?.setOnClickListener {
            nativeAdView?.callToActionView?.performClick()
        }
        primaryView?.setOnClickListener {
            nativeAdView?.callToActionView?.performClick()
        }

    }

    /**
     * To prevent memory leaks, make sure to destroy your ad when you don't need it anymore. This
     * method does not destroy the template view.
     * https://developers.google.com/admob/android/native-unified#destroy_ad
     */
    fun destroyNativeAd() {
        if (nativeAd != null) nativeAd?.destroy()
    }

    private fun initView(context: Context, attributeSet: AttributeSet?) {
        val attributes =
            context.theme.obtainStyledAttributes(attributeSet, R.styleable.TemplateView, 0, 0)
        templateType = try {
            attributes.getResourceId(
                R.styleable.TemplateView_gnt_template_type, R.layout.gnt_medium_template_view
            )
        } finally {
            attributes.recycle()
        }
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(templateType, this)
    }

    public override fun onFinishInflate() {
        super.onFinishInflate()
        nativeAdView = findViewById(R.id.native_ad_view)
        primaryView = findViewById(R.id.primary)
        secondaryView = findViewById(R.id.secondary)
        tertiaryView = findViewById(R.id.body)
        ratingBar = findViewById(R.id.rating_bar)
        ratingBar?.isEnabled = false
        callToActionView = findViewById(R.id.cta)
        iconView = findViewById(R.id.icon)
        mediaView = findViewById(R.id.media_view)
        background = findViewById(R.id.background)
    }
}