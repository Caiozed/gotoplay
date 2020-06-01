package com.caiozed.gotoplay.mainactivitypkg

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.models.Game
import com.caiozed.gotoplay.models.Image
import com.caiozed.gotoplay.utils.processImage
import kotlinx.android.synthetic.main.activity_image_view.*
import kotlinx.android.synthetic.main.game_layout.view.*

class ImageViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        val image = intent.extras?.get("image") as Image
        processImage(Game(0, "", cover = image), game_image)
        window.statusBarColor = Color.BLACK
        window.navigationBarColor = Color.WHITE
    }
}
