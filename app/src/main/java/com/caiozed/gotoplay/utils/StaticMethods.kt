package com.caiozed.gotoplay.utils

import android.app.ActionBar
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.caiozed.gotoplay.models.Game
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.game_layout.view.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException

enum class GameStatus (val value: Int){
    Backlog(0),
    Playing(1),
    Played(2)
}

class doAsyncMain(val handler: () -> Unit) : AsyncTask<Void, Void, String>() {
    override fun doInBackground(vararg params: Void?): String? {
        Handler(Looper.getMainLooper()).post(Runnable {
            handler()
        })
        return null;
    }

    override fun onPreExecute() {
        super.onPreExecute()
        // ...
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        // ...
    }
}

class doAsyncSecondary(val handler: () -> Unit) : AsyncTask<Void, Void, String>() {
    override fun doInBackground(vararg params: Void?): String? {
        handler()
        return null;
    }

    override fun onPreExecute() {
        super.onPreExecute()
        // ...
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        // ...
    }
}


fun convertToBase64String(bitmap: Bitmap): String?{
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT);
}


fun convertFromBase64String(base64: String): Drawable?{
    var ret: Drawable? = null //from w ww.  ja  va  2  s  .  c  o  m

    if (!base64.equals("")) {
        val bais = ByteArrayInputStream(
            Base64.decode(base64.toByteArray(), Base64.DEFAULT)
        )
        ret = Drawable.createFromStream(
            bais,
            ""
        )
        try {
            bais.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    return ret
}


fun processImage(game: Game, view: View){
    var img = ImageView(view.context)
    if(game?.base64Image.isNullOrEmpty()){
        Picasso.get()
            .load("https://images.igdb.com/igdb/image/upload/t_720p/${game?.coverData?.image_id}.jpg")
            //.placeholder(TextDrawable(game.name))
            //.error(TextDrawable(game.name))
            .into(img,
                object: Callback {
                    override fun onSuccess() {
                        //set animations here
                        view.game_text.text = "";
                        if (game != null) {
                            game.base64Image = convertToBase64String(img.drawable.toBitmap())
                            view.game_text.background = game.base64Image?.let {
                                convertFromBase64String(
                                    it
                                )
                            }
                        }
                    }

                    override fun onError(e: Exception?) {
                        view.game_text.background = null
                        Log.d("Image Not Found", "Image for game ${game?.name} not found")
                    }
                })
    }else{
        view.game_text.background = game?.base64Image?.let {
            view.game_text.text = "";
            convertFromBase64String(
                it
            )
        }
    }
}
