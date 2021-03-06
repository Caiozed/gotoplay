package com.caiozed.gotoplay.mainactivitypkg.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil

import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.databinding.HomeFragmentBinding
import com.caiozed.gotoplay.mainactivitypkg.fragments.viewmodels.HomeViewModel

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = DataBindingUtil.inflate<HomeFragmentBinding>(inflater, R.layout.home_fragment, container, false)
        var viewModel =
            HomeViewModel(
                binding
            )

        binding.viewModel = viewModel
        this.context?.let { viewModel.startSearch(it) }
        postponeEnterTransition()
        binding.root!!.doOnPreDraw { startPostponedEnterTransition() }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
