package com.caiozed.gotoplay.mainactivitypkg.fragments

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.databinding.SearchFragmentBinding
import com.caiozed.gotoplay.mainactivitypkg.MainActivity
import com.caiozed.gotoplay.mainactivitypkg.fragments.viewmodels.SearchViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SearchFragment : BottomSheetDialogFragment() {
    var previousFragment: Fragment? = null

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = DataBindingUtil.inflate<SearchFragmentBinding>(inflater, R.layout.search_fragment, container, false)
        var viewModel =
            SearchViewModel(
                binding
            )

        binding.viewModel = viewModel
        previousFragment = MainActivity.instance.currentFragment
        MainActivity.instance.currentFragment = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val bottomSheet =
                dialog.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet.layoutParams.height =
                ViewGroup.LayoutParams.MATCH_PARENT //Can write to the height you want
        }
        val view = view
        view!!.post {
            val parent = view.parent as View
            val params =
                parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val bottomSheetBehavior =
                behavior as BottomSheetBehavior<*>?
            bottomSheetBehavior!!.peekHeight = view.measuredHeight - 30
            parent.setBackgroundColor(Color.TRANSPARENT)
        }
        MainActivity.instance.window.navigationBarColor = Color.BLACK

    }

    override fun onCancel(dialog: DialogInterface) {
        MainActivity.instance.currentFragment = previousFragment
        MainActivity.instance.window.navigationBarColor = Color.WHITE
        super.onCancel(dialog)
    }
}
