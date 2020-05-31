package com.caiozed.gotoplay.mainactivitypkg

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import com.caiozed.gotoplay.BR
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.databinding.GameDetailsLayoutBinding
import com.caiozed.gotoplay.models.Game
import com.google.android.material.bottomsheet.BottomSheetBehavior


class GameDetailsActivity : AppCompatActivity() {

    var binding: GameDetailsLayoutBinding? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val game = intent.extras?.get("game") as Game

        binding = DataBindingUtil
            .setContentView<GameDetailsLayoutBinding>(this,
                R.layout.game_details_layout
            )

        var viewModel = GameDetailsViewModel(this)
        viewModel.game = game

        binding!!.setVariable(BR.viewModel, viewModel)
        viewModel.search()

    }

    override fun onStart() {
        super.onStart()

        var botomSheet = findViewById<View>(R.id.bottom_sheet_container)

        var sheetBehavior = BottomSheetBehavior.from(botomSheet);
        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        binding!!.root!!.post(Runnable {
            sheetBehavior!!.peekHeight = binding!!.root!!.measuredHeight/2
        })

            sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // React to state change
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        finishAfterTransition()
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }
}

