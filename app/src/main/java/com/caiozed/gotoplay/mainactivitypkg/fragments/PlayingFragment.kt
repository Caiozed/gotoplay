package com.caiozed.gotoplay.mainactivitypkg.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.databinding.PlayingLayoutBinding
import com.caiozed.gotoplay.mainactivitypkg.fragments.viewmodels.PlayingViewModel

class PlayingFragment : Fragment() {
    companion object {
        fun newInstance() = PlayingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = DataBindingUtil.inflate<PlayingLayoutBinding>(inflater, R.layout.playing_layout, container, false)
        var viewModel =
            PlayingViewModel(
                binding
            )

        binding.viewModel = viewModel
        viewModel.startSearch()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


}