package com.caiozed.gotoplay.mainactivitypkg.fragments.viewmodels

import android.opengl.Visibility
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.recyclerview.widget.LinearLayoutManager
import com.caiozed.gotoplay.BR
import com.caiozed.gotoplay.adapters.ImageAdapter
import com.caiozed.gotoplay.models.Game
import com.caiozed.gotoplay.models.Image
import com.caiozed.gotoplay.utils.IGDBService
import com.caiozed.gotoplay.utils.convertTimestampToString
import com.caiozed.gotoplay.utils.doAsyncSecondary
import kotlinx.android.synthetic.main.game_details_modal.view.*
import kotlinx.android.synthetic.main.progress_bar.view.*

class GameDetailsModalViewModel(var view: View) : BaseObservable() {
    @get:Bindable
    var game: Game? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.game)
        }

    @RequiresApi(Build.VERSION_CODES.O)
    fun search(){
        var screenShots: MutableList<Image?>? = mutableListOf();
        var gameFound: Game? = null
        var adapter = ImageAdapter(screenShots)
        view.screeshots_grid.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL ,false)
        view.screeshots_grid.adapter = adapter
        view.game_details_scrollview.visibility = View.INVISIBLE

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
            game?.releaseDate = convertTimestampToString(game?.first_release_date ?: 0)
            game?.screenshots?.toMutableList()?.let { adapter.addItems(it) }
        }).execute()
    }
}
