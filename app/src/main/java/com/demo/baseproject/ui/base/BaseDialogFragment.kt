package com.demo.baseproject.ui.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.google.firebase.crashlytics.FirebaseCrashlytics

abstract class BaseDialogFragment<B : ViewBinding> : DialogFragment() {

    private var _binding: B? = null
    private val binding get() = _binding!!

    protected abstract fun getViewBinding(): B

    protected open var cancellable = true
    protected open fun setUpViews() {}
    protected open fun observeData() {}

    /**
     * Use to receive extras from fragment's arguments
     */
    protected open fun receiveExtras() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiveExtras()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = cancellable

        setUpViews()
        observeData()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!manager.isDestroyed && !manager.isStateSaved) {
            super.show(manager, tag)
        } else {
            val cause = if (manager.isDestroyed) "because activity is destroyed"
            else if (manager.isStateSaved) "because fragment manager state is already saved" else ""
            FirebaseCrashlytics.getInstance()
                .recordException(Exception("Can't perform commit for showing $tag $cause"))
        }
    }
}