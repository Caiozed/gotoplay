package com.caiozed.gotoplay.mainactivitypkg

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.recyclerview.widget.LinearLayoutManager
import com.caiozed.gotoplay.BR
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.adapters.ImageAdapter
import com.caiozed.gotoplay.models.Game
import com.caiozed.gotoplay.models.Image
import com.caiozed.gotoplay.utils.IGDBService
import com.caiozed.gotoplay.utils.convertTimestampToString
import com.caiozed.gotoplay.utils.doAsyncSecondary
import com.caiozed.gotoplay.utils.watchYoutubeVideo
import kotlinx.android.synthetic.main.game_details_layout.*
import kotlinx.android.synthetic.main.game_details_modal.*
import kotlinx.android.synthetic.main.progress_bar.*
import kotlinx.android.synthetic.main.tag_layout.view.*

class GameDetailsViewModel(var view: GameDetailsActivity) : BaseObservable() {

    @get:Bindable
    var game: Game? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.game)
        }

    fun openVideo(){
        watchYoutubeVideo(view, game!!.videos?.get(0)!!.video_id)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun search() {
        var screenShots: MutableList<Image?>? = mutableListOf();
        var gameFound: Game? = null
        var adapter = ImageAdapter(screenShots)
        view.screeshots_grid.layoutManager = LinearLayoutManager(view, LinearLayoutManager.HORIZONTAL ,false)
        view.screeshots_grid.adapter = adapter
        view.game_details_scrollview.visibility = View.INVISIBLE
        view.game_rating_tag.visibility = View.INVISIBLE

        doAsyncSecondary({
            gameFound = IGDBService.getGames(
                """fields name, id, cover.url, cover.image_id, rating, first_release_date, storyline, rating, summary,
                cover.url, cover.image_id,
                genres.name,
                screenshots.image_id, screenshots.url,
                videos.*,
                platforms.abbreviation, platforms.platform_logo.image_id, platforms.platform_logo.url,
                involved_companies.company.name, involved_companies.company.logo.image_id, involved_companies.company.logo.url;
            where id = ${ game!!.id };
            limit 1;
            """
            )?.first()
        }, {
            view.progress_bar_indefinite.visibility = View.VISIBLE
        }, {
            view.progress_bar_indefinite.visibility = View.INVISIBLE
            view.game_details_scrollview.visibility = View.VISIBLE

            game = gameFound ?: game
            game?.releaseDate = convertTimestampToString(game?.first_release_date)
            game?.screenshots?.toMutableList()?.let { adapter.addItems(it) }

            if(game?.platforms?.size ?: 0 > 0){
                createTags(game?.platforms!!.map { it.abbreviation }, view.platforms_container)
            }

            if(game?.genres?.size ?: 0 > 0){
                createTags(game?.genres!!.map { it.name }, view.genres_container)
            }

            if(game?.rating!! > 0){
                createRating(game?.rating!!.toInt())
            }
        }).execute()
    }

    private fun createTags(texts: List<String>, container: LinearLayout) {
        for (text in texts) {
            if (!text.isNullOrEmpty()) {
                val tagView: View =
                    LayoutInflater.from(view).inflate(R.layout.tag_layout, null)
                tagView.tag_text.text = text
                container.addView(tagView)
            }
        }
    }

    private fun createRating(rating: Int) {
        view.game_rating_tag.tag_text.text = rating.toString()
        view.game_rating_tag.visibility = View.VISIBLE

        var color = when (rating) {
            in 75..100 -> R.color.green
            in 50..74 -> R.color.yellow
            else -> R.color.red
        }

        var textColor = when (rating) {
            in 50..74 -> Color.BLACK
            else -> Color.WHITE
        }

        view.game_rating_tag.tag_text_container.backgroundTintList = ContextCompat.getColorStateList(view, color)
        view.game_rating_tag.tag_text.setTextColor(textColor)
    }
}

