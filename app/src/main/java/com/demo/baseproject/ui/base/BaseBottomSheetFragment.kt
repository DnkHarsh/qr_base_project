package com.demo.baseproject.ui.base

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.demo.baseproject.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment<B : ViewBinding> :
    BottomSheetDialogFragment() {

    private var _binding: B? = null
    private val binding get() = _binding!!

    protected abstract fun getViewBinding(): B

    protected open val state = BottomSheetBehavior.STATE_COLLAPSED
    protected open fun setUpViews() {}
    protected open fun observeData() {}

    /**
     * Use to receive extras from fragment's arguments
     */
    protected open fun receiveExtras() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetStyle)
        receiveExtras()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            try {
                val bottomSheetDialog = dialog as BottomSheetDialog
                val bottomSheet = bottomSheetDialog.findViewById<View>(
                    com.google.android.material.R.id.design_bottom_sheet
                ) as FrameLayout
                BottomSheetBehavior.from(bottomSheet).state = state
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return dialog
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
        (view.parent as? View)?.setBackgroundColor(Color.TRANSPARENT)
        setUpViews()
        observeData()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected fun showAllowingStateLoss(manager: FragmentManager, tag: String?) {
        val ft = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    protected fun isBindingInitialized(): Boolean {
        return _binding != null
    }
}