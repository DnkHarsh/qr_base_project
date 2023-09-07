package com.demo.baseproject.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    private var _binding: B? = null
    private val binding get() = _binding!!
    private var hasInitializedRootView = false

    protected abstract fun getViewBinding(): B

    protected open val keepBindingRetained = false

    /**
     * Use to receive extras from fragment's arguments
     */
    protected open fun receiveExtras() {}

    /**
     * Only use to create/initialise views
     */
    protected open fun setUpViews() {}

    /**
     * Only use to register observers
     */
    protected open fun observeData() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiveExtras()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = getViewBinding()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!hasInitializedRootView && keepBindingRetained) {
            hasInitializedRootView = true
            setUpViews()
        } else if (!keepBindingRetained) {
            setUpViews()
        }
        observeData()
    }

    override fun onDestroyView() {
        if (!keepBindingRetained) {
            _binding = null
        }
        super.onDestroyView()
    }

    protected fun isBindingInitialized(): Boolean {
        return _binding != null
    }
}