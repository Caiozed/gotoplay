package com.caiozed.gotoplay.mainactivitypkg.fragments

import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.databinding.GameDetailsModalBinding
import com.caiozed.gotoplay.mainactivitypkg.GameDetailsActivity
import com.caiozed.gotoplay.mainactivitypkg.fragments.viewmodels.GameDetailsModalViewModel
import com.caiozed.gotoplay.models.Game
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class GameDetailsModalFragment(var game: Game, var parentView: GameDetailsActivity) : BottomSheetDialogFragment() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = DataBindingUtil.inflate<GameDetailsModalBinding>(inflater, R.layout.game_details_modal, container, false)
        var viewModel =
            GameDetailsModalViewModel(
                binding.root
            )
        viewModel.game = game

        binding.viewModel = viewModel
        viewModel.search()

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
            dialog?.window?.setDimAmount(0F)
        }
        val view = view
        view!!.post {
            val parent = view.parent as View
            val params =
                parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val bottomSheetBehavior =
                behavior as BottomSheetBehavior<*>?
            bottomSheetBehavior!!.peekHeight = view.measuredHeight/2
            parent.setBackgroundColor(Color.TRANSPARENT)
            parent.setPadding(0,0,0,0)
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        parentView.finishAfterTransition()
        super.onCancel(dialog)
    }
}
