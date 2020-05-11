package com.caiozed.gotoplay.mainactivitypkg.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.databinding.BacklogLayoutBinding
import com.caiozed.gotoplay.databinding.PlayedLayoutBinding
import com.caiozed.gotoplay.databinding.PlayingLayoutBinding

class PlayedFragment : Fragment() {
    companion object {
        fun newInstance() = PlayedFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = DataBindingUtil.inflate<PlayedLayoutBinding>(inflater, R.layout.played_layout, container, false)
        var viewModel = PlayedViewModel(binding)

        binding.viewModel = viewModel
        viewModel.startSearch()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


}