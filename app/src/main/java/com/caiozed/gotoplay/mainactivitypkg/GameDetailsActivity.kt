package com.caiozed.gotoplay.mainactivitypkg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.BR
import com.caiozed.gotoplay.databinding.GameDetailsLayoutBinding
import com.caiozed.gotoplay.models.Game

class GameDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val game = intent.extras?.get("game") as Game

        val binding = DataBindingUtil
            .setContentView<GameDetailsLayoutBinding>(this,
                R.layout.game_details_layout
            )

        var viewModel = GameDetailsViewModel(this)
        viewModel.game = game

        binding.setVariable(BR.viewModel, viewModel)
    }
}
