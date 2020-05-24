package com.caiozed.gotoplay.mainactivitypkg.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.databinding.BacklogLayoutBinding
import com.caiozed.gotoplay.mainactivitypkg.fragments.viewmodels.BacklogViewModel

class BacklogFragment : Fragment() {
    companion object {
        fun newInstance() = BacklogFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = DataBindingUtil.inflate<BacklogLayoutBinding>(inflater, R.layout.backlog_layout, container, false)
        var viewModel =
            BacklogViewModel(
                binding
            )

        binding.viewModel = viewModel
        viewModel.startSearch()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun setExitSharedElementCallback(callback: SharedElementCallback?) {
        super.setExitSharedElementCallback(callback)
    }
}