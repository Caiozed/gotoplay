package com.caiozed.gotoplay.mainactivitypkg.fragments

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caiozed.gotoplay.BR
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.databinding.HomeFragmentBinding
import com.caiozed.gotoplay.models.Game
import com.caiozed.gotoplay.utils.GridLoadAsyncTask
import com.caiozed.gotoplay.utils.IGDBService

class HomeViewModel(
    var homeFragment: HomeFragmentBinding
) : BaseObservable() {

    var latestPage: Int = 0
    var upComingPage: Int = 0

    @get:Bindable
    var games: MutableList<Game>? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.games)
        }


    private fun searchForLatestGames(): MutableList<Game>?{
        games = IGDBService.getGames(
            """fields name, id, cover, rating;
                sort popularity desc;
                limit 10;
                offset ${latestPage * 10};"""
        )

        games = IGDBService.findCovers(games!!)
        latestPage++
        return games
    }


    private fun searchForUpcomingReleases(): MutableList<Game>?{
        games = IGDBService.getGames(
            """fields *; 
                where first_release_date >= ${  System.currentTimeMillis() / 1000 };
                sort first_release_date;
                limit 10;
                offset ${upComingPage * 10};
                """
        )

        games = IGDBService.findCovers(games!!)
        upComingPage++
        return games
    }

    fun startSearch(context: Context) {
        var gridLatest = homeFragment.root!!.findViewById<RecyclerView>(R.id.latestGridView);
        var gridReleases = homeFragment.root!!.findViewById<RecyclerView>(R.id.monthReleasesGridView);

        GridLoadAsyncTask(gridLatest, LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
        ) { return@GridLoadAsyncTask searchForLatestGames() }.execute()

        GridLoadAsyncTask(gridReleases, LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
        ) { return@GridLoadAsyncTask searchForUpcomingReleases() }.execute()
    }

}

