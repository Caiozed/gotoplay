package com.caiozed.gotoplay.bindingadapters

import android.media.Image
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Placeholder
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.BindingAdapter
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.models.Game
import com.caiozed.gotoplay.utils.convertFromBase64String
import com.caiozed.gotoplay.utils.convertToBase64String
import com.caiozed.gotoplay.utils.processImage
import com.caiozed.gotoplay.utils.watchYoutubeVideo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.game_layout.view.*

@BindingAdapter("bindBackground")
fun bindBackground(view: TextView, game: Game) {
    processImage(game, view)
}