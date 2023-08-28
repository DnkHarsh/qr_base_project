package com.demo.baseproject.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.viewbinding.ViewBinding
import com.android.billingclient.api.*
import com.demo.baseproject.R
import com.demo.baseproject.storage.AppPref
import com.demo.baseproject.utils.extensions.getCountryBasedOnSimCardOrNetwork
import com.demo.baseproject.utils.extensions.showCustomTwoButtonAlertDialog

abstract class BaseActivity<B : ViewBinding>(val bindingFactory: (LayoutInflater) -> B) :
    AppCompatActivity(), PurchasesResponseListener {

    lateinit var binding: B

    private lateinit var billingClient: BillingClient
    private var paymentListener: PaymentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
    }

    fun setListener(listener: PaymentListener) {
        paymentListener = listener
    }

    fun removeListener() {
        paymentListener = null
    }

    private fun getProductId(): String {
        val locale = getCountryBasedOnSimCardOrNetwork()
        return if (locale == "IN") getString(R.string.PRODUCT_ID_IN) else getString(R.string.PRODUCT_ID_ROW)
    }

    fun inAppProductProcess() {
        if (!this::billingClient.isInitialized)
            billingClient = BillingClient.newBuilder(this).setListener(purchaseUpdatedListener)
                .enablePendingPurchases().build()
        if (billingClient.isReady) {
            val params = QueryProductDetailsParams.newBuilder()
            val productList = listOf(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(getProductId())
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            )
            params.setProductList(productList)
            billingClient.queryProductDetailsAsync(params.build()) { billingResult, productDetailsList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    for (productDetails in productDetailsList) {
                        if (getProductId() == productDetails.productId) {
                            launchInAppPurchase(productDetails)
                        }
                    }
                }
            }
        } else {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        // The BillingClient is ready. You can query purchases here.
                        inAppProductProcess()
                    }
                }

                override fun onBillingServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            })
        }
    }

    private fun launchInAppPurchase(productDetails: ProductDetails) {
        val productDetailsParamsList =
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .build()
            )

        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()
        billingClient.launchBillingFlow(this, flowParams)
    }

    override fun onQueryPurchasesResponse(p0: BillingResult, list: MutableList<Purchase>) {
        if (list.isEmpty()) {
            paymentListener?.onContinue()
            return
        }
        handlePurchases(list)
    }


    open fun queryPurchases() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build(), this
        )
    }

    private val ackPurchase = AcknowledgePurchaseResponseListener {
        if (it.responseCode == BillingClient.BillingResponseCode.OK) {
            //if purchase is acknowledged
            // Grant entitlement to the user. and restart activity
            AppPref.getInstance().setValue(AppPref.IS_PREMIUM_PURCHASED, true)
            paymentListener?.onComplete()
        }
    }

    private fun handlePurchases(purchases: List<Purchase>) {
        for (purchase in purchases) {
            //if item is purchased
            if (purchase.products.contains(getProductId()) && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                //if item is purchased and not acknowledged
                if (!purchase.isAcknowledged) {
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams, ackPurchase)
                } else {
                    // Grant entitlement to the user on item purchase
                    AppPref.getInstance().setValue(AppPref.IS_PREMIUM_PURCHASED, true)
                    paymentListener?.onComplete()
                }
            } else if (purchase.products.contains(getProductId()) && purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                AppPref.getInstance().setValue(AppPref.IS_PREMIUM_PURCHASED, false)
                paymentListener?.onContinue()
            } else if (purchase.products.contains(getProductId()) && purchase.purchaseState == Purchase.PurchaseState.PENDING) {
                AppPref.getInstance().setValue(AppPref.IS_PREMIUM_PURCHASED, false)
                paymentListener?.onContinue()
            }
        }
    }

    fun checkInappPurchasedForConfirm() {
        billingClient = BillingClient.newBuilder(this).setListener(purchaseUpdatedListener)
            .enablePendingPurchases().build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    queryPurchases()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    private val purchaseUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        //if item newly purchased
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.let {
                    handlePurchases(purchases)
                }
            }

            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                queryPurchases()
            }

            else -> {
                paymentListener?.onContinue()
            }
        }
    }

    /**
     * Common method to navigate in different activity class
     *
     * @param nextScreenIntent    object of Intent
     * @param view                view which will be animate
     * @param sharedElementName   transition name
     * @param isAnimate           boolean for screen animation
     * @param finishActivity      boolean set true if want to finish current activity
     * @param startAnimation      this is start animation use full if isAnimate is true
     * @param endAnimation        this is end animation use full if isAnimate is true
     */
    fun navigateToDifferentScreen(
        nextScreenIntent: Intent,
        view: View? = null,
        sharedElementName: String = "",
        isAnimate: Boolean = true,
        finishActivity: Boolean = false,
        startAnimation: Int = R.anim.enter_from_right,
        endAnimation: Int = R.anim.exit_to_left
    ) {
        try {
            view?.let {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    view,
                    sharedElementName
                )
                startActivity(nextScreenIntent, options.toBundle())
                if (finishActivity) {
                    finish()
                }
            } ?: run {
                startActivity(nextScreenIntent)
                if (isAnimate) {
                    overridePendingTransition(startAnimation, endAnimation)
                }

                if (finishActivity) {
                    finish()
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * This is common override method on activity's onBackPress method.
     * You have to call this method in activity's device's onBackPressed method and back button.
     */
    fun onBackPressFromActivity() {
//        TODO("NEED to check and redesign dialog")
        try {
            showCustomTwoButtonAlertDialog(getString(R.string.app_name),
                getString(R.string.title_exit_dialog),
                getString(R.string.yes),
                getString(R.string.no),
                false,
                { dialog, _ ->
                    dialog.dismiss()
                    finish()
                },
                { dialog, _ -> dialog.dismiss() })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Method to finish current foreground activity and launch next activity.
     *
     * @param nextScreenIntent nextScreenIntent with extra parameters.
     */
    private fun navigateToExitActivity(nextScreenIntent: Intent?) {
        startActivity(nextScreenIntent)
        finishActivity()
    }

    /**
     * Method to finish activity in normal way and with transition animation if current OS is equal or above lollipop.
     */
    private fun finishActivity() {
        try {
            supportFinishAfterTransition()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}