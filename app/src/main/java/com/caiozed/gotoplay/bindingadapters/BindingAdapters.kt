package com.caiozed.gotoplay.bindingadapters

import android.media.Image
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Placeholder
import androidx.databinding.BindingAdapter
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.models.Game
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.game_layout.view.*

@BindingAdapter("bindBackground")
fun bindBackground(view: TextView, game: Game) {
    var img = ImageView(view.context)
    Picasso.get()
        .load("https://images.igdb.com/igdb/image/upload/t_720p/${game.coverData?.image_id}.jpg")
        //.placeholder(TextDrawable(game.name))
        //.error(TextDrawable(game.name))
        .into(img,
            object: Callback {
                override fun onSuccess() {
                    //set animations here
                    view.background = img.drawable
                }

                override fun onError(e: Exception?) {
                    view.text = game.name
                    Log.d("Image Not Found", "Image for game ${game.name} not found")
                }
            })
}